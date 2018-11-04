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
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.dsl.DslException;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.support.AbstractJsonRpcOutputMessage;
import org.springframework.dsl.lsp.client.AbstractLspClient;
import org.springframework.dsl.lsp.client.ExchangeNotificationFunction;
import org.springframework.dsl.lsp.client.ExchangeRequestFunction;
import org.springframework.dsl.lsp.client.LspClient;
import org.springframework.dsl.lsp.client.LspClientResponse;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link LspClient} bound to websocket session.
 *
 * @author Janne Valkealahti
 *
 */
public class WebSocketBoundedLspClient extends AbstractLspClient {

	private WebSocketSession session;
	private final Function<JsonRpcRequest, String> requestDecoder;
	private final EmitterProcessor<LspClientResponse> responses = EmitterProcessor.create();

	/**
	 * Instantiates a new web socket bounded lsp client.
	 *
	 * @param session the session
	 * @param objectMapper the object mapper
	 */
	public WebSocketBoundedLspClient(WebSocketSession session, ObjectMapper objectMapper) {
		Assert.notNull(session, "WebSocketSession must be set");
		Assert.notNull(objectMapper, "ObjectMapper must be set");
		this.session = session;
		this.requestDecoder = s -> {
			try {
				return objectMapper.writeValueAsString(s);
			} catch (Exception e) {
				throw new DslException("Unable to convert json rpc request", e);
			}
		};
	}

	@Override
	public RequestSpec request() {
		return new DefaultRequestSpec(new DefaultExchangeRequestFunction());
	}

	@Override
	public NotificationSpec notification() {
		return new DefaultNotificationSpec(new DefaultExchangeNotificationFunction());
	}

	public EmitterProcessor<LspClientResponse> getResponses() {
		return responses;
	}

	private class DefaultExchangeRequestFunction implements ExchangeRequestFunction {

		@Override
		public Mono<LspClientResponse> exchange(JsonRpcRequest request) {
			return Mono.defer(() -> {
				JsonRpcOutputMessage adaptedResponse = new WebSocketJsonRpcOutputMessage(session, session.bufferFactory());
				Mono.just(request)
					.map(requestDecoder)
					.map(r -> session.bufferFactory().wrap(r.getBytes(Charset.defaultCharset())))
					.doOnNext(r -> {
						adaptedResponse.writeWith(Mono.just(r)).subscribe();
						adaptedResponse.setComplete().subscribe();
					})
					.subscribe();
				return Mono.from(responses)
						.filter(r -> ObjectUtils.nullSafeEquals(r.response().getId(), request.getId()));
			});
		}
	}

	private class DefaultExchangeNotificationFunction implements ExchangeNotificationFunction {

		@Override
		public Mono<Void> exchange(JsonRpcRequest request) {
			return Mono.defer(() -> {
				JsonRpcOutputMessage adaptedResponse = new WebSocketJsonRpcOutputMessage(session, session.bufferFactory());
				return Mono.just(request)
					.map(requestDecoder)
					.map(r -> session.bufferFactory().wrap(r.getBytes(Charset.defaultCharset())))
					.doOnNext(r -> {
						adaptedResponse.writeWith(Mono.just(r)).subscribe();
						adaptedResponse.setComplete().subscribe();
					})
					.then();
			});
		}
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
			return null;
		}
	}
}
