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
package org.springframework.dsl.lsp.client;

import org.springframework.dsl.jsonrpc.JsonRpcRequest;

import reactor.core.publisher.Mono;

/**
 * Contract to handle exchange for a {@code JSONRPC request}.
 *
 * @author Janne Valkealahti
 *
 */
@FunctionalInterface
public interface ExchangeRequestFunction {

	/**
	 * Handle exchange and return a {@link Mono} for completion.
	 *
	 * @param request the json rpc request
	 * @return the mono having a response
	 */
	Mono<LspClientResponse> exchange(JsonRpcRequest request);
}
