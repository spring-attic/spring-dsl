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

import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession.JsonRpcSessionCustomizer;
import org.springframework.dsl.jsonrpc.session.JsonRpcSessionManager;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * Default implementation of a {@link ServerJsonRpcExchange}. Simply returning
 * request and response as is. Session is requested from a manager and cached in
 * this exchange.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultServerJsonRpcExchange implements ServerJsonRpcExchange {

	private final JsonRpcInputMessage request;
	private final JsonRpcOutputMessage response;
	private final Mono<JsonRpcSession> sessionMono;

	/**
	 * Instantiates a new default server json rpc exchange.
	 *
	 * @param request the request
	 * @param response the response
	 * @param sessionManager the session manager
	 */
	public DefaultServerJsonRpcExchange(JsonRpcInputMessage request, JsonRpcOutputMessage response,
			JsonRpcSessionManager sessionManager) {
		this(request, response, sessionManager, null);
	}

	/**
	 * Instantiates a new default server json rpc exchange.
	 *
	 * @param request the request
	 * @param response the response
	 * @param sessionManager the session manager
	 * @param sessionCustomizer the session customizer
	 */
	public DefaultServerJsonRpcExchange(JsonRpcInputMessage request, JsonRpcOutputMessage response,
			JsonRpcSessionManager sessionManager, JsonRpcSessionCustomizer sessionCustomizer) {
		Assert.notNull(request, "'request' is required");
		Assert.notNull(response, "'response' is required");
		Assert.notNull(sessionManager, "'sessionManager' is required");
		this.request = request;
		this.response = response;
		this.sessionMono = sessionManager.getSession(this).doOnNext(session -> {
			if (sessionCustomizer != null) {
				sessionCustomizer.customize(session);
			}
		}).cache();
	}

	@Override
	public JsonRpcInputMessage getRequest() {
		return request;
	}

	@Override
	public JsonRpcOutputMessage getResponse() {
		return response;
	}

	@Override
	public Mono<JsonRpcSession> getSession() {
		// sessionMono is caching last emitted session as session manager
		// would anyway need to return same session associated with a connection.
		return sessionMono;
	}
}
