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

import java.util.function.BiFunction;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.support.DefaultJsonRpcResponse;
import org.springframework.dsl.jsonrpc.support.DefaultJsonRpcResponseJsonDeserializer;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.BlockingNettyContext;
import reactor.ipc.netty.tcp.TcpClient;

/**
 * {@link LspClient} connecting to a known {@code Language Server} address.
 *
 * @author Janne Valkealahti
 *
 */
public class NettyTcpClientLspClient extends AbstractLspClient {

	private static final Logger log = LoggerFactory.getLogger(NettyTcpClientLspClient.class);
	private final String host;
	private final Integer port;
	private BlockingNettyContext blockingNettyContext;
	private BiFunction<NettyInbound, NettyOutbound, Mono<Void>> function;
	private Processor<ByteBuf, LspClientResponse> processor;

	/**
	 * Instantiates a new netty tcp client lsp client.
	 *
	 * @param host the host
	 * @param port the port
	 * @param function the function
	 * @param processor the processor
	 */
	public NettyTcpClientLspClient(String host, int port, BiFunction<NettyInbound, NettyOutbound, Mono<Void>> function,
			Processor<ByteBuf, LspClientResponse> processor) {
		this.host = host;
		this.port = port;
		this.function = function;
		this.processor = processor;
	}

	@Override
	public void doStart() {
		if (blockingNettyContext == null) {
			init();
		}
	}

	@Override
	public void doStop() {
		if (blockingNettyContext != null) {
			blockingNettyContext.shutdown();
		}
		blockingNettyContext = null;
	}

	@Override
	public RequestSpec request() {
		return new DefaultRequestSpec(new DefaultExchangeRequestFunction(processor, processor));
	}

	@Override
	public NotificationSpec notification() {
		return new DefaultNotificationSpec(new DefaultExchangeNotificationFunction(processor));
	}

	private void init() {
		blockingNettyContext = TcpClient.create(host, port).start(function);
	}

	private class DefaultExchangeRequestFunction implements ExchangeRequestFunction {

		final Subscriber<ByteBuf> requests;
		final Publisher<LspClientResponse> responses;
		ObjectMapper mapper;

		public DefaultExchangeRequestFunction(Subscriber<ByteBuf> requests, Publisher<LspClientResponse> responses) {
			this.requests = requests;
			this.responses = responses;

			SimpleModule module = new SimpleModule();
			module.addDeserializer(DefaultJsonRpcResponse.class, new DefaultJsonRpcResponseJsonDeserializer());
			mapper = new ObjectMapper();
			mapper.registerModule(module);
		}

		@Override
		public Mono<LspClientResponse> exchange(JsonRpcRequest request) {

			return Mono.defer(() -> {
					String r = null;
					try {
						r = mapper.writeValueAsString(request);
					} catch (JsonProcessingException e) {
						log.error("Mapper error", e);
						throw new RuntimeException(e);
					}
					requests.onNext(Unpooled.copiedBuffer(r.getBytes()));
					return Mono.empty();
				})
				.then(Mono.from(Flux.from(responses).filter(r -> {
					return ObjectUtils.nullSafeEquals(r.response().getId(), request.getId());
				})));
		}
	}

	private class DefaultExchangeNotificationFunction implements ExchangeNotificationFunction {

		final Subscriber<ByteBuf> requests;
		ObjectMapper mapper;

		public DefaultExchangeNotificationFunction(Subscriber<ByteBuf> requests) {
			this.requests = requests;

			SimpleModule module = new SimpleModule();
			module.addDeserializer(DefaultJsonRpcResponse.class, new DefaultJsonRpcResponseJsonDeserializer());
			mapper = new ObjectMapper();
			mapper.registerModule(module);
		}

		@Override
		public Mono<Void> exchange(JsonRpcRequest request) {

			return Mono.defer(() -> {
					String r = null;
					try {
						r = mapper.writeValueAsString(request);
					} catch (JsonProcessingException e) {
						log.error("Mapper error", e);
						throw new RuntimeException(e);
					}
					requests.onNext(Unpooled.copiedBuffer(r.getBytes()));
					return Mono.empty();
				});
		}
	}
}
