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

import org.springframework.dsl.jsonrpc.session.JsonRpcSession;

import reactor.core.publisher.Mono;

/**
 * Contract for an JSONRPC request-response interaction. Provides access to the
 * JSONRPC request and response.
 *
 * @author Janne Valkealahti
 *
 */
public interface ServerJsonRpcExchange {

	/**
	 * Gets the current JSONRPC request.
	 *
	 * @return the JSONRPC request
	 */
	JsonRpcInputMessage getRequest();

	/**
	 * Gets the current JSONRPC response.
	 *
	 * @return the JSONRPC response
	 */
	JsonRpcOutputMessage getResponse();

	/**
	 * Return the {@link JsonRpcSession} for the current request. Always guaranteed
	 * to return an instance either matching to the session id requested by the
	 * client, or with a new session id either because the client did not specify
	 * one or because the underlying session had expired. Use of this method does
	 * not automatically create a session.
	 *
	 * @return the {@link Mono} for a {@link JsonRpcSession}}
	 */
	Mono<JsonRpcSession> getSession();
}
