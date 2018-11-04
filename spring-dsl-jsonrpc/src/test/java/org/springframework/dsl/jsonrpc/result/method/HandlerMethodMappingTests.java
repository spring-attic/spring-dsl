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

import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;

/**
 * Tests for {@link AbstractHandlerMethodMapping}.
 *
 * @author Janne Valkealahti
 *
 */
public class HandlerMethodMappingTests {

	private AbstractHandlerMethodMapping<String> mapping;
	private MyHandler handler;
    private Method method1;
    private Method method2;

	@Before
    public void setup() throws Exception {
		mapping = new MyHandlerMethodMapping();
		handler = new MyHandler();
		this.method1 = handler.getClass().getMethod("handlerMethod1");
        this.method2 = handler.getClass().getMethod("handlerMethod2");
	}

    @Test(expected = IllegalStateException.class)
    public void registerDuplicates() {
            this.mapping.registerMapping("foo", this.handler, this.method1);
            this.mapping.registerMapping("foo", this.handler, this.method2);
    }

	@Test
	public void registerMapping() throws Exception {
		String key1 = "/foo";
		String key2 = "/foo*";
		this.mapping.registerMapping(key1, this.handler, this.method1);
		this.mapping.registerMapping(key2, this.handler, this.method2);
		assertThat(mapping.getMappingRegistry().getMappings().keySet()).containsExactlyInAnyOrder(key1, key2);
	}

	@Test
	public void registerMappingWithSameMethodAndTwoHandlerInstances() throws Exception {
		String key1 = "foo";
		String key2 = "bar";
		MyHandler handler1 = new MyHandler();
		MyHandler handler2 = new MyHandler();
		this.mapping.registerMapping(key1, handler1, this.method1);
		this.mapping.registerMapping(key2, handler2, this.method1);
		assertThat(mapping.getMappingRegistry().getMappings().keySet()).containsExactlyInAnyOrder(key1, key2);
	}

	private static class MyHandlerMethodMapping extends AbstractHandlerMethodMapping<String> {

		@Override
		protected boolean isHandler(Class<?> beanType) {
			return true;
		}

		@Override
		protected String getMappingForMethod(Method method, Class<?> handlerType) {
			String methodName = method.getName();
			return methodName.startsWith("handler") ? methodName : null;
		}

		@Override
		protected String getMatchingMapping(String pattern, ServerJsonRpcExchange exchange) {
			return pattern;
		}

		@Override
		protected Comparator<String> getMappingComparator(ServerJsonRpcExchange exchange) {
			return (o1, o2) -> o1.compareTo(o2);
		}

	}

	@JsonRpcController
	private static class MyHandler {

		@JsonRpcRequestMapping
		public void handlerMethod1() {
		}

		@JsonRpcRequestMapping
		public void handlerMethod2() {
		}
	}
}
