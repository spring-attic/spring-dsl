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

import org.springframework.core.MethodParameter;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;

import reactor.core.publisher.Mono;

/**
 * Strategy to resolve the argument value for a method parameter in the context
 * of the current {@code JSONRPC} request.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcHandlerMethodArgumentResolver {

	/**
	 * Whether this resolver supports the given method parameter.
	 *
	 * @param parameter the method parameter
	 * @return true if supports the given method parameter
	 */
	boolean supportsParameter(MethodParameter parameter);

	/**
	 * Resolve the value for the method parameter.
	 *
	 * @param parameter the method parameter
	 * @param exchange the current json rpc exchange
	 * @return {@code Mono} for the argument value, possibly empty
	 */
	Mono<Object> resolveArgument(MethodParameter parameter, ServerJsonRpcExchange exchange);
}
