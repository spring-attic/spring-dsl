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
package org.springframework.dsl.jsonrpc.session;

import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;

import reactor.core.publisher.Mono;

/**
 * Main class for access to the {@link JsonRpcSession} for a {@code JSONRPC} request.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcSessionManager {

	/**
	 * Return the {@link JsonRpcSession} for the given exchange. Always guaranteed
	 * to return an instance either matching to the session id requested by the
	 * client, or a new session either because the client did not specify one or
	 * because the underlying session expired.
	 *
	 * @param exchange the current exchange
	 * @return {@link Mono} for the JsonRpcSession
	 */
	Mono<JsonRpcSession> getSession(ServerJsonRpcExchange exchange);
}
