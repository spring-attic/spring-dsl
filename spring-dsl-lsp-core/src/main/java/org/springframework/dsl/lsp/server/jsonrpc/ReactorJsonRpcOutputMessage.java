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
package org.springframework.dsl.lsp.server.jsonrpc;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.dsl.jsonrpc.support.AbstractJsonRpcOutputMessage;
import org.springframework.util.Assert;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyOutbound;

public class ReactorJsonRpcOutputMessage extends AbstractJsonRpcOutputMessage {

	private final NettyOutbound response;

	/**
	 * Instantiates a new reactor json rpc output message.
	 *
	 * @param response the response
	 * @param bufferFactory the buffer factory
	 */
	public ReactorJsonRpcOutputMessage(NettyOutbound response, NettyDataBufferFactory bufferFactory) {
		super(bufferFactory);
		Assert.notNull(response, "NettyOutbound must be set");
		this.response = response;
	}

	@Override
	protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> publisher) {
		Publisher<ByteBuf> body = toByteBufs(publisher);
		return this.response.send(body).then();
	}

	@Override
	protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> publisher) {
		Publisher<Publisher<ByteBuf>> body = Flux.from(publisher)
				.map(ReactorJsonRpcOutputMessage::toByteBufs);
		return this.response.sendGroups(body).then();
	}

	private static Publisher<ByteBuf> toByteBufs(Publisher<? extends DataBuffer> dataBuffers) {
		return Flux.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf);
	}
}
