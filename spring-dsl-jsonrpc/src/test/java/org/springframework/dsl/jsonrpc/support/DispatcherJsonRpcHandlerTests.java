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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Supplier;

import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerAdapter;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerMapping;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResultHandler;
import org.springframework.dsl.jsonrpc.ResolvableMethod;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * Tests for {@link DispatcherJsonRpcHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class DispatcherJsonRpcHandlerTests {

	private static final MethodParameter RETURN_TYPE = ResolvableMethod.on(DispatcherJsonRpcHandler.class).named("handle")
			.resolveReturnType();

	@Test
	public void handlerMappingOrder() throws Exception {
		JsonRpcHandlerMapping hm1 = mock(JsonRpcHandlerMapping.class, withSettings().extraInterfaces(Ordered.class));
		JsonRpcHandlerMapping hm2 = mock(JsonRpcHandlerMapping.class, withSettings().extraInterfaces(Ordered.class));
		when(((Ordered) hm1).getOrder()).thenReturn(1);
		when(((Ordered) hm2).getOrder()).thenReturn(2);
		when((hm1).getHandler(any())).thenReturn(Mono.just((Supplier<String>) () -> "1"));
		when((hm2).getHandler(any())).thenReturn(Mono.just((Supplier<String>) () -> "2"));

		StaticApplicationContext context = new StaticApplicationContext();
		context.registerBean("b2", JsonRpcHandlerMapping.class, () -> hm2);
		context.registerBean("b1", JsonRpcHandlerMapping.class, () -> hm1);
		context.registerBean(JsonRpcHandlerAdapter.class, SupplierHandlerAdapter::new);
		context.registerBean(JsonRpcHandlerResultHandler.class, StringHandlerResultHandler::new);
		context.refresh();

		DispatcherJsonRpcHandler dispatcherHandler = new DispatcherJsonRpcHandler(context);

		MockServerJsonRpcExchange exchange = MockServerJsonRpcExchange.from(MockJsonRpcInputMessage.get("/"));
		dispatcherHandler.handle(exchange).block(Duration.ofSeconds(0));
		assertThat(exchange.getResponse().getBodyAsString().block(Duration.ofSeconds(5))).isEqualTo("1");
	}

	@SuppressWarnings("unused")
	private void handle() {
	}

	private static class SupplierHandlerAdapter implements JsonRpcHandlerAdapter {

		@Override
		public boolean supports(Object handler) {
			return handler instanceof Supplier;
		}

		@Override
		public Mono<JsonRpcHandlerResult> handle(ServerJsonRpcExchange exchange, Object handler) {
			return Mono.just(new JsonRpcHandlerResult(handler, ((Supplier<?>) handler).get(), RETURN_TYPE));
		}
	}

	private static class StringHandlerResultHandler implements JsonRpcHandlerResultHandler {

		@Override
		public boolean supports(JsonRpcHandlerResult result) {
			Object value = result.getReturnValue();
			return value != null && String.class.equals(value.getClass());
		}

		@Override
		public Mono<Void> handleResult(ServerJsonRpcExchange exchange, JsonRpcHandlerResult result) {
			byte[] bytes = ((String) result.getReturnValue()).getBytes(StandardCharsets.UTF_8);
			DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(bytes);
			return exchange.getResponse().writeWith(Mono.just(dataBuffer));
		}
	}
}
