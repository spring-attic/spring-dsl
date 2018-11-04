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
import org.springframework.dsl.lsp.client.LspClient.Builder;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * Default implementation of a {@link Builder} currently building instances of a
 * {@link NettyTcpClientLspClient}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultLspClientBuilder implements Builder {

	private String host;
	private Integer port;
	private BiFunction<NettyInbound, NettyOutbound, Mono<Void>> function;
	private Processor<ByteBuf, LspClientResponse> processor;

	@Override
	public Builder host(String host) {
		this.host = host;
		return this;
	}

	@Override
	public Builder port(Integer port) {
		this.port = port;
		return this;
	}

	@Override
	public Builder function(BiFunction<NettyInbound, NettyOutbound, Mono<Void>> function) {
		this.function = function;
		return this;
	}

	@Override
	public Builder processor(Processor<ByteBuf, LspClientResponse> processor) {
		this.processor = processor;
		return this;
	}

	@Override
	public LspClient build() {
		return new NettyTcpClientLspClient(host, port, function, processor);
	}

}
