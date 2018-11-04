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
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Default implementation of a {@link JsonRpcSessionManager} delegating to a
 * {@link JsonRpcSessionIdResolver} for session id and to a
 * {@link JsonRpcSessionStore}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultJsonRpcSessionManager implements JsonRpcSessionManager {

	private JsonRpcSessionIdResolver sessionIdResolver = new RequestSessionIdJsonRpcSessionIdResolver();
	private JsonRpcSessionStore sessionStore = new InMemoryJsonRpcSessionStore();

	@Override
	public Mono<JsonRpcSession> getSession(ServerJsonRpcExchange exchange) {
		return Mono.defer(() -> retrieveSession(exchange)
				.switchIfEmpty(this.sessionStore.createSession(exchange.getRequest().getSessionId().block()))
				.doOnNext(session -> exchange.getResponse().beforeCommit(() -> save(exchange, session))));
	}

	/**
	 * Sets the session id resolver.
	 * <p>
	 * By default an instance of {@link RequestSessionIdJsonRpcSessionIdResolver}.
	 *
	 * @param sessionIdResolver the new session id resolver
	 */
	public void setSessionIdResolver(JsonRpcSessionIdResolver sessionIdResolver) {
		Assert.notNull(sessionIdResolver, "JsonRpcSessionIdResolver is required");
		this.sessionIdResolver = sessionIdResolver;
	}

	/**
	 * Gets the session id resolver.
	 *
	 * @return the session id resolver
	 */
	public JsonRpcSessionIdResolver getSessionIdResolver() {
		return this.sessionIdResolver;
	}

	/**
	 * Sets the session store.
	 * <p>
	 * By default an instance of {@link InMemoryJsonRpcSessionStore}.
	 *
	 * @param sessionStore the new session store
	 */
	public void setSessionStore(JsonRpcSessionStore sessionStore) {
		Assert.notNull(sessionStore, "JsonRpcSessionStore is required");
		this.sessionStore = sessionStore;
	}

	/**
	 * Gets the session store.
	 *
	 * @return the session store
	 */
	public JsonRpcSessionStore getSessionStore() {
		return sessionStore;
	}

	private Mono<JsonRpcSession> retrieveSession(ServerJsonRpcExchange exchange) {
		return Flux.fromIterable(getSessionIdResolver().resolveSessionIds(exchange))
				.concatMap(this.sessionStore::retrieveSession)
				.next();
	}

	private Mono<Void> save(ServerJsonRpcExchange exchange, JsonRpcSession session) {
		if (!session.isStarted() || session.isExpired()) {
			return Mono.empty();
		}
		return session.save();
	}
}
