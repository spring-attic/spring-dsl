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
package org.springframework.dsl.jsonrpc.session;

import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;

import reactor.core.publisher.Flux;

/**
 * Contract for session id resolution strategies. Allows for session id
 * resolution through the request and for sending the session id or expiring the
 * session through the response.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcSessionIdResolver {

	/**
	 * Resolve the session id's associated with the request.
	 *
	 * @param exchange the current exchange
	 * @return the session id's
	 */
	Flux<String> resolveSessionIds(ServerJsonRpcExchange exchange);
}
