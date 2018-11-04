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
package org.springframework.dsl.lsp.client;

import java.nio.charset.Charset;
import java.util.function.Function;

import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.dsl.DslException;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.lsp.server.jsonrpc.ReactorJsonRpcOutputMessage;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyOutbound;

/**
 * {@link LspClient} bound to existing netty channels.
 *
 * @author Janne Valkealahti
 *
 */
public class NettyBoundedLspClient extends AbstractLspClient {

	private NettyOutbound out;
	private final Function<JsonRpcRequest, String> requestDecoder;
	private EmitterProcessor<LspClientResponse> responses = EmitterProcessor.create();

	public NettyBoundedLspClient(NettyOutbound out, ObjectMapper objectMapper) {
		this.out = out;
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
				NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(out.alloc());
				JsonRpcOutputMessage adaptedResponse = new ReactorJsonRpcOutputMessage(out, bufferFactory);
				Mono.just(request)
					.map(requestDecoder)
					.map(r -> bufferFactory.wrap(r.getBytes(Charset.defaultCharset())))
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
				NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(out.alloc());
				JsonRpcOutputMessage adaptedResponse = new ReactorJsonRpcOutputMessage(out, bufferFactory);
				return Mono.just(request)
					.map(requestDecoder)
					.map(r -> bufferFactory.wrap(r.getBytes(Charset.defaultCharset())))
					.doOnNext(r -> {
						adaptedResponse.writeWith(Mono.just(r)).subscribe();
						adaptedResponse.setComplete().subscribe();
					})
					.then();
			});
		}
	}
}
