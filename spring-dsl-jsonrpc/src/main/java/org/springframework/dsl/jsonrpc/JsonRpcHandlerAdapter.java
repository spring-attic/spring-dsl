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
package org.springframework.dsl.jsonrpc;

import org.springframework.dsl.jsonrpc.support.DispatcherJsonRpcHandler;

import reactor.core.publisher.Mono;

/**
 * Contract that decouples the {@link DispatcherJsonRpcHandler} from the details
 * of invoking a handler and makes it possible to support any handler type.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcHandlerAdapter {

	/**
	 * Whether this {@code JsonRpcHandlerAdapter} supports the given {@code handler}.
	 *
	 * @param handler handler object to check
	 * @return whether or not the handler is supported
	 */
	boolean supports(Object handler);

	/**
	 * Handle the request with the given handler.
	 *
	 * @param exchange current json rpc server exchange
	 * @param handler the selected handler which must have been previously
	 *                checked via {@link #supports(Object)}
	 * @return {@link Mono} that emits a single {@code JsonRpcHandlerResult} or none if
	 *         the request has been fully handled and doesn't require further handling.
	 */
	Mono<JsonRpcHandlerResult> handle(ServerJsonRpcExchange exchange, Object handler);
}
