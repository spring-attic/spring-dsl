/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.lsp.server.websocket.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.DslException;
import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession.JsonRpcSessionCustomizer;
import org.springframework.dsl.jsonrpc.support.AbstractJsonRpcOutputMessage;
import org.springframework.dsl.lsp.LspSystemConstants;
import org.springframework.dsl.lsp.client.LspClientResponse;
import org.springframework.dsl.lsp.server.jsonrpc.RpcHandler;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servlet based websocket adapter for a {@link RpcHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspServletWebSocketHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(LspServletWebSocketHandler.class);
	private final RpcHandler rpcHandler;
	private final ObjectMapper objectMapper;
	private final Function<String, JsonRpcRequest> requestDecoder;
	private final Function<String, JsonRpcResponse> responseDecoder;
	private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

	public LspServletWebSocketHandler(RpcHandler rpcHandler, ObjectMapper objectMapper) {
		Assert.notNull(rpcHandler, "RpcHandler must be set");
		Assert.notNull(objectMapper, "ObjectMapper must be set");
		this.rpcHandler = rpcHandler;
		this.objectMapper = objectMapper;
		this.requestDecoder = s -> {
			try {
				return objectMapper.readValue(s, JsonRpcRequest.class);
			} catch (Exception e) {
				throw new DslException("Unable to convert json to rpc request", e);
			}
		};
		this.responseDecoder = s -> {
			try {
				return objectMapper.readValue(s, JsonRpcResponse.class);
			} catch (Exception e) {
				throw new DslException("Unable to convert json to rpc response", e);
			}
		};
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.debug("Handling message {}", message);

		WebSocketBoundedLspClient lspClient = new WebSocketBoundedLspClient(session, objectMapper);
		JsonRpcSessionCustomizer customizer = s -> s.getAttributes()
				.put(LspSystemConstants.SESSION_ATTRIBUTE_LSP_CLIENT, lspClient);


		Flux<String> shared = Flux.just(message.getPayload()).share();

		shared
			.map(responseDecoder)
			.filter(response -> response.getResult() != null || response.getError() != null)
			.subscribe(bb -> {
				lspClient.getResponses().onNext(
						LspClientResponse.create(lspClient.getJsonRpcExtractorStrategies()).response(bb).build());
			});

		shared
			.map(requestDecoder)
			.filter(request -> request.getMethod() != null)
			.doOnNext(request -> {

				JsonRpcInputMessage inputMessage = new JsonRpcInputMessage() {

					@Override
					public Mono<String> getJsonrpc() {
						return Mono.justOrEmpty(request.getJsonrpc());
					}

					@Override
					public Mono<String> getId() {
						return Mono.justOrEmpty(request.getId());
					}

					@Override
					public Mono<String> getMethod() {
						return Mono.justOrEmpty(request.getMethod());
					}

					@Override
					public Mono<String> getParams() {
						return Mono.justOrEmpty(request.getParams().toString());
					}

					@Override
					public Mono<String> getSessionId() {
						return Mono.fromSupplier(() -> session.getId());

					}
				};

				JsonRpcOutputMessage adaptedResponse = new WebSocketJsonRpcOutputMessage(session, bufferFactory);

				rpcHandler.handle(inputMessage, adaptedResponse, customizer)
					.doOnError(ex -> {
						log.error("Handling completed with error", ex);
						String error = "{\"jsonrpc\":\"2.0\", \"id\":" + request.getId() + ", \"error\":{\"code\":-32603, \"message\": \"internal server error\"}}";
						DataBuffer buffer = bufferFactory.wrap(error.getBytes(Charset.defaultCharset()));
						Flux<DataBuffer> body = Flux.just(buffer);
						adaptedResponse.writeWith(body).subscribe();
						adaptedResponse.setComplete().subscribe();
					})
					.doOnSuccess(aVoid -> log.debug("Handling completed with success"))
					.subscribe();
			})
			.blockFirst();
	}

	private static class WebSocketJsonRpcOutputMessage extends AbstractJsonRpcOutputMessage {

		private WebSocketSession session;

		public WebSocketJsonRpcOutputMessage(WebSocketSession session, DataBufferFactory dataBufferFactory) {
			super(dataBufferFactory);
			this.session = session;
		}

		@Override
		protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {
			return Flux.from(body)
				.map(bodyBuffer -> {
					ByteBuffer byteBuffer = bodyBuffer.asByteBuffer();
					byte[] byteArray = new byte[byteBuffer.remaining()];
					byteBuffer.get(byteArray);
					return new TextMessage(byteArray);
				})
				.doOnNext(m -> {
					try {
						session.sendMessage(m);
					} catch (IOException e) {
						throw new DslException(e);
					}
				})
				.then();
		}

		@Override
		protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
			// TODO: not sure if this is needed right now
			return Mono.empty();
		}
	}
}
