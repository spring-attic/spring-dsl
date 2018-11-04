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
package org.springframework.dsl.jsonrpc.result.method.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.ResolvableMethod;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;
import org.springframework.dsl.jsonrpc.codec.JsonRpcMessageWriter;
import org.springframework.dsl.jsonrpc.result.method.HandlerMethod;

/**
 * Tests for {@link JsonRpcResponseResultResultHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcResponseResultResultHandlerTests {

	private JsonRpcResponseResultResultHandler resultHandler;

	@Before
	public void setup() {
		List<JsonRpcMessageWriter<?>> messageWriters = new ArrayList<>(1);
		this.resultHandler = new JsonRpcResponseResultResultHandler(messageWriters,
				ReactiveAdapterRegistry.getSharedInstance());
	}

	@Test
	public void supports() throws NoSuchMethodException {
		Object controller = new TestController();
		Method method;

		method = ResolvableMethod.on(TestController.class).annotPresent(JsonRpcResponseResult.class).resolveMethod("hi");
		testSupports(controller, method);
	}

	private void testSupports(Object controller, Method method) {
		JsonRpcHandlerResult handlerResult = getHandlerResult(controller, method);
		assertThat(this.resultHandler.supports(handlerResult)).isTrue();
	}

	private JsonRpcHandlerResult getHandlerResult(Object controller, Method method) {
		HandlerMethod handlerMethod = new HandlerMethod(controller, method);
		return new JsonRpcHandlerResult(handlerMethod, null, handlerMethod.getReturnType());
	}

	@JsonRpcController
	private static class TestController {

		@JsonRpcResponseResult
		public String hi() {
			return null;
		}
	}
}
