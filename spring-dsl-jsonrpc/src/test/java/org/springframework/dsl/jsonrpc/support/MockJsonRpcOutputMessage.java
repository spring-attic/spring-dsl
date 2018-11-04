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
package org.springframework.dsl.jsonrpc.support;

import java.nio.charset.Charset;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MockJsonRpcOutputMessage extends AbstractJsonRpcOutputMessage {

	private Flux<DataBuffer> body = Flux.error(new IllegalStateException(
			"No content was written nor was setComplete() called on this response."));

	private Function<Flux<DataBuffer>, Mono<Void>> writeHandler;

	public MockJsonRpcOutputMessage() {
		super(new DefaultDataBufferFactory());
		this.writeHandler = body -> {
			this.body = body.cache();
			return this.body.then();
		};
	}

	@Override
	protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {
		return this.writeHandler.apply(Flux.from(body));
	}

	@Override
	protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
		return this.writeHandler.apply(Flux.from(body).concatMap(Flux::from));
	}

	@Override
	public Mono<Void> setComplete() {
		return doCommit(() -> Mono.defer(() -> this.writeHandler.apply(Flux.empty())));
	}

	public Flux<DataBuffer> getBody() {
		return this.body;
	}

	public Mono<String> getBodyAsString() {
//		Charset charset = Optional.ofNullable(getHeaders().getContentType()).map(MimeType::getCharset)
//				.orElse(StandardCharsets.UTF_8);

		Charset charset = Charset.defaultCharset();

		return getBody().reduce(bufferFactory().allocateBuffer(), (previous, current) -> {
			previous.write(current);
			DataBufferUtils.release(current);
			return previous;
		}).map(buffer -> bufferToString(buffer, charset));
	}

	private static String bufferToString(DataBuffer buffer, Charset charset) {
		Assert.notNull(charset, "'charset' must not be null");
		byte[] bytes = new byte[buffer.readableByteCount()];
		buffer.read(bytes);
		return new String(bytes, charset);
	}
}
