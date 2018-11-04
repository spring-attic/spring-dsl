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

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MockJsonRpcInputMessage implements JsonRpcInputMessage {

	private final Flux<DataBuffer> body;
	private final String params;

	public MockJsonRpcInputMessage(Publisher<? extends DataBuffer> body, String params) {
		this.body = Flux.from(body);
		this.params = params;
	}

//	@Override
//	public Flux<DataBuffer> getBody() {
//		return body;
//	}

	@Override
	public Mono<String> getMethod() {
		return null;
	}

	@Override
	public Mono<String> getParams() {
		return Mono.just(params);
	}

	public static Builder get(String method) {
		return new DefaultBuilder(method);
	}

	public interface Builder {
		Builder body(String body);
		MockJsonRpcInputMessage build();
	}

	private static class DefaultBuilder implements Builder {

		private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();
		private String body;

		public DefaultBuilder(String body) {
			this.body = body;
		}

		@Override
		public Builder body(String body) {
			this.body = body;
			return this;
		}

		@Override
		public MockJsonRpcInputMessage build() {
			return new MockJsonRpcInputMessage(Flux.just(BUFFER_FACTORY.wrap(body.getBytes(Charset.defaultCharset()))), body);
		}

	}

	@Override
	public Mono<String> getJsonrpc() {
		return null;
	}

	@Override
	public Mono<String> getId() {
		return null;
	}

	@Override
	public Mono<String> getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
