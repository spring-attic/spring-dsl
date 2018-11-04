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

import java.util.List;

import org.springframework.dsl.jsonrpc.result.method.HandlerMethod;
import org.springframework.dsl.jsonrpc.result.method.InvocableHandlerMethod;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcHandlerMethodArgumentResolver;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class ControllerMethodResolver {

	/** The request mapping resolvers. */
	private final List<JsonRpcHandlerMethodArgumentResolver> requestMappingResolvers;

	/**
	 * Instantiates a new controller method resolver.
	 *
	 * @param requestMappingResolvers the request mapping resolvers
	 */
	public ControllerMethodResolver(List<JsonRpcHandlerMethodArgumentResolver> requestMappingResolvers) {
		this.requestMappingResolvers = requestMappingResolvers;
	}

	/**
	 * Gets the request mapping method.
	 *
	 * @param handlerMethod the handler method
	 * @return the request mapping method
	 */
	public InvocableHandlerMethod getRequestMappingMethod(HandlerMethod handlerMethod) {
		InvocableHandlerMethod invocable = new InvocableHandlerMethod(handlerMethod);
		invocable.setArgumentResolvers(this.requestMappingResolvers);
		return invocable;
	}

//	@Nullable
//	public InvocableHandlerMethod getExceptionHandlerMethod(Throwable ex, HandlerMethod handlerMethod) {
//		Class<?> handlerType = handlerMethod.getBeanType();
//
//		InvocableHandlerMethod invocable = new InvocableHandlerMethod(targetBean, targetMethod);
//		invocable.setArgumentResolvers(this.exceptionHandlerResolvers);
//		return invocable;
//	}
}
