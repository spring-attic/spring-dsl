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
package org.springframework.dsl.jsonrpc.result.method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.dsl.jsonrpc.ResolvableMethod.on;
import static org.springframework.dsl.jsonrpc.support.MockJsonRpcInputMessage.get;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.support.MockServerJsonRpcExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InvocableHandlerMethodTests {

	private final MockServerJsonRpcExchange exchange = MockServerJsonRpcExchange.from(get("body"));

	@Test
	public void invokeAndHandleWithExchangeAndMonoVoid() throws Exception {
		Method method = on(VoidController.class).mockCall(c -> c.exchangeMonoVoid(exchange)).method();
		JsonRpcHandlerResult result = invoke(new VoidController(), method, resolverFor(Mono.just(this.exchange)))
				.block(Duration.ZERO);

		assertThat(result).isNull();
		assertThat(this.exchange.getResponse().getBodyAsString().block(Duration.ZERO)).isEqualTo("body");
	}

	private Mono<JsonRpcHandlerResult> invoke(Object handler, Method method, JsonRpcHandlerMethodArgumentResolver... resolver) {

		InvocableHandlerMethod hm = new InvocableHandlerMethod(handler, method);
		hm.setArgumentResolvers(Arrays.asList(resolver));
		return hm.invoke(this.exchange);
	}

	private <T> JsonRpcHandlerMethodArgumentResolver resolverFor(Mono<Object> resolvedValue) {
		JsonRpcHandlerMethodArgumentResolver resolver = mock(JsonRpcHandlerMethodArgumentResolver.class);
		when(resolver.supportsParameter(any())).thenReturn(true);
		when(resolver.resolveArgument(any(), any())).thenReturn(resolvedValue);
		return resolver;
	}

	@SuppressWarnings("unused")
	private static class TestController {

		public String noArgs() {
			return "success";
		}

		public String singleArg(String q) {
			return "success:" + q;
		}

		public void exceptionMethod() {
			throw new IllegalStateException("boo");
		}
	}

	private static class VoidController {

		public Mono<Void> exchangeMonoVoid(ServerJsonRpcExchange exchange) {
			return exchange.getResponse().writeWith(getBody("body"));
		}

		private Flux<DataBuffer> getBody(String body) {
			try {
				return Flux.just(new DefaultDataBufferFactory().wrap(body.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException ex) {
				throw new IllegalStateException(ex);
			}
		}
	}
}
