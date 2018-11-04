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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerAdapter;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.result.method.HandlerMethod;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcHandlerMethodArgumentResolver;
import org.springframework.dsl.jsonrpc.support.ControllerMethodResolver;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

public class JsonRpcRequestMappingHandlerAdapter implements JsonRpcHandlerAdapter, InitializingBean {

	private final List<JsonRpcHandlerMethodArgumentResolver> resolvers;
	private ControllerMethodResolver methodResolver;

	public JsonRpcRequestMappingHandlerAdapter(List<JsonRpcHandlerMethodArgumentResolver> resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<JsonRpcHandlerMethodArgumentResolver> requestMappingResolvers = new ArrayList<>();
		requestMappingResolvers.addAll(this.resolvers);
		this.methodResolver = new ControllerMethodResolver(requestMappingResolvers);
	}

	@Override
	public boolean supports(Object handler) {
		return HandlerMethod.class.equals(handler.getClass());
	}

	@Override
	public Mono<JsonRpcHandlerResult> handle(ServerJsonRpcExchange exchange, Object handler) {
		Assert.notNull(handler, "Expected handler");
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		Function<Throwable, Mono<JsonRpcHandlerResult>> exceptionHandler =
				ex -> handleException(ex, handlerMethod, exchange);

		return methodResolver.getRequestMappingMethod(handlerMethod).invoke(exchange)
			.doOnNext(result -> result.setExceptionHandler(exceptionHandler))
			.onErrorResume(exceptionHandler);

//
//		Mono<JsonRpcHandlerResult> invoke = this.methodResolver.getRequestMappingMethod(handlerMethod).invoke(exchange);
//		return invoke;
	}

	private Mono<JsonRpcHandlerResult> handleException(Throwable exception, HandlerMethod handlerMethod,
			ServerJsonRpcExchange exchange) {

//		InvocableHandlerMethod invocable = this.methodResolver.getExceptionHandlerMethod(exception, handlerMethod);


		return Mono.error(exception);
	}
}
