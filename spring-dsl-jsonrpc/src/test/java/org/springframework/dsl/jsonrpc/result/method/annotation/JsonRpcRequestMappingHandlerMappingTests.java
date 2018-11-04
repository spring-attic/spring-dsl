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
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcRequestMappingInfo;

/**
 * Tests for {@link JsonRpcRequestMappingHandlerMapping}.
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcRequestMappingHandlerMappingTests {

	private final StaticApplicationContext context = new StaticApplicationContext();
	private final JsonRpcRequestMappingHandlerMapping handlerMapping = new JsonRpcRequestMappingHandlerMapping();

	@Before
	public void setup() {
		handlerMapping.setApplicationContext(context);
	}

	@Test
	public void testMapping() throws Exception {
		assertAnnotationMapping("handlerMethod1", TestController1.class);
		assertAnnotationMapping("handlerMethod2", TestController1.class);
		assertAnnotationMapping("handlerMethod1", TestController2.class);
		assertAnnotationMapping("handlerMethod2", TestController2.class);
	}

	private JsonRpcRequestMappingInfo assertAnnotationMapping(String methodName, Class<?> clazz) throws Exception {
		Method method = clazz.getMethod(methodName);
		JsonRpcRequestMappingInfo info = this.handlerMapping.getMappingForMethod(method, clazz);

		assertThat(info).isNotNull();

		Set<String> methods = info.getMethodsCondition().getMethods();
		assertThat(methods).containsExactlyInAnyOrder(methodName);
		return info;
	}

	@JsonRpcController
	private static class TestController1 {

		@JsonRpcRequestMapping(method = "handlerMethod1")
		public void handlerMethod1() {
		}

		@JsonRpcRequestMapping(method = "handlerMethod2")
		public void handlerMethod2() {
		}
	}

	@JsonRpcController
	@JsonRpcRequestMapping(method = "handler")
	private static class TestController2 {

		@JsonRpcRequestMapping(method = "Method1")
		public void handlerMethod1() {
		}

		@JsonRpcRequestMapping(method = "Method2")
		public void handlerMethod2() {
		}
	}
}
