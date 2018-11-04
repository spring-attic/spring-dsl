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
package org.springframework.dsl.lsp.server.websocket;

import java.nio.charset.Charset;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
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
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link WebSocketHandler} dispatching messages to a {@link RpcHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspWebSocketHandler implements WebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(LspWebSocketHandler.class);
	private final RpcHandler rpcHandler;
	private final ObjectMapper objectMapper;
	private final Function<String, JsonRpcRequest> requestDecoder;
	private final Function<String, JsonRpcResponse> responseDecoder;

	/**
	 * Instantiates a new lsp web socket handler.
	 *
	 * @param rpcHandler the rpc handler
	 * @param objectMapper the object mapper
	 */
	public LspWebSocketHandler(RpcHandler rpcHandler, ObjectMapper objectMapper) {
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
	public Mono<Void> handle(WebSocketSession session) {
		WebSocketBoundedLspClient lspClient = new WebSocketBoundedLspClient(session, objectMapper);
		JsonRpcSessionCustomizer customizer = s -> s.getAttributes()
				.put(LspSystemConstants.SESSION_ATTRIBUTE_LSP_CLIENT, lspClient);

		// can read payload only once so need to share it
		Flux<String> shared = session
				.receive()
				.map(WebSocketMessage::getPayloadAsText)
				.share();

		// push stuff to lsp client
		shared
			.map(responseDecoder)
			.filter(response -> response.getResult() != null || response.getError() != null)
			.subscribe(bb -> {
					lspClient.getResponses().onNext(
							LspClientResponse.create(lspClient.getJsonRpcExtractorStrategies()).response(bb).build());
				});

		// return normal rpc handling
		return shared
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

				JsonRpcOutputMessage adaptedResponse = new WebSocketJsonRpcOutputMessage(session, session.bufferFactory());

				rpcHandler.handle(inputMessage, adaptedResponse, customizer)
					.doOnError(ex -> {
						log.error("Handling completed with error", ex);
						String error = "{\"jsonrpc\":\"2.0\", \"id\":" + request.getId() + ", \"error\":{\"code\":-32603, \"message\": \"internal server error\"}}";
						DataBuffer buffer = session.bufferFactory().wrap(error.getBytes(Charset.defaultCharset()));
						Flux<DataBuffer> body = Flux.just(buffer);
						adaptedResponse.writeWith(body).subscribe();
						adaptedResponse.setComplete().subscribe();
					})
					.doOnSuccess(aVoid -> log.debug("Handling completed with success"))
					.subscribe();
			})
			.then();
	}

	private static class WebSocketJsonRpcOutputMessage extends AbstractJsonRpcOutputMessage {

		private WebSocketSession session;

		public WebSocketJsonRpcOutputMessage(WebSocketSession session, DataBufferFactory dataBufferFactory) {
			super(dataBufferFactory);
			this.session = session;
		}

		@Override
		protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {

			Flux<WebSocketMessage> messages = Flux.from(body)
				.map(bodyBuffer -> {
					return new WebSocketMessage(WebSocketMessage.Type.TEXT, bodyBuffer);
				});
			return session.send(messages);
		}

		@Override
		protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
			// TODO: not sure if this is needed right now
			return Mono.empty();
		}
	}
}
