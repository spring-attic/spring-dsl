/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.lsp.server.jsonrpc;

import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession.JsonRpcSessionCustomizer;

import reactor.core.publisher.Mono;

/**
 * Lowest level contract for reactive JSONRPC request handling that serves as a
 * common denominator across different runtimes.
 *
 * @author Janne Valkealahti
 *
 */
public interface RpcHandler {

	/**
	 * Handle the given request and write to the response.
	 *
	 * @param request the current request
	 * @param response the current response
	 * @return indicates completion of request handling
	 */
	Mono<Void> handle(JsonRpcInputMessage request, JsonRpcOutputMessage response, JsonRpcSessionCustomizer sessionCustomizer);

	default Mono<Void> handle(JsonRpcInputMessage request, JsonRpcOutputMessage response) {
		return handle(request, response, null);
	}
}
