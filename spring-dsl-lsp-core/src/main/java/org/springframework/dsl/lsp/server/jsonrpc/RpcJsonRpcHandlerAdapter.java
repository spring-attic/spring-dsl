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
package org.springframework.dsl.lsp.server.jsonrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.jsonrpc.JsonRpcHandler;
import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.session.DefaultJsonRpcSessionManager;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession.JsonRpcSessionCustomizer;
import org.springframework.dsl.jsonrpc.session.JsonRpcSessionManager;
import org.springframework.dsl.jsonrpc.support.DefaultServerJsonRpcExchange;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * {@link RpcHandler} delegating to {@link JsonRpcHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class RpcJsonRpcHandlerAdapter implements RpcHandler {

	private static final Logger log = LoggerFactory.getLogger(RpcJsonRpcHandlerAdapter.class);
	private final JsonRpcHandler delegate;
	private JsonRpcSessionManager sessionManager = new DefaultJsonRpcSessionManager();

	/**
	 * Instantiates a new rpc json rpc handler adapter.
	 *
	 * @param delegate the delegate
	 */
	public RpcJsonRpcHandlerAdapter(JsonRpcHandler delegate) {
		Assert.notNull(delegate, "JsonRpcHandler must be set");
		this.delegate = delegate;
	}

	@Override
	public Mono<Void> handle(JsonRpcInputMessage request, JsonRpcOutputMessage response, JsonRpcSessionCustomizer sessionCustomizer) {
		ServerJsonRpcExchange exchange = createExchange(request, response, sessionCustomizer);
		return delegate.handle(exchange)
				.onErrorResume(exception -> handleFailure(request, response, exception))
				.then(Mono.defer(response::setComplete));
	}

	/**
	 * Gets the session manager.
	 *
	 * @return the session manager
	 */
	public JsonRpcSessionManager getSessionManager() {
		return sessionManager;
	}

	/**
	 * Sets the session manager.
	 *
	 * @param sessionManager the new session manager
	 */
	public void setSessionManager(JsonRpcSessionManager sessionManager) {
		Assert.notNull(sessionManager, "JsonRpcSessionManager must be set");
		this.sessionManager = sessionManager;
	}

	/**
	 * Creates the {@link ServerJsonRpcExchange}.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the server json rpc exchange
	 */
	protected ServerJsonRpcExchange createExchange(JsonRpcInputMessage request, JsonRpcOutputMessage response, JsonRpcSessionCustomizer sessionCustomizer) {
		return new DefaultServerJsonRpcExchange(request, response, sessionManager, sessionCustomizer);
	}

	private Mono<Void> handleFailure(JsonRpcInputMessage request, JsonRpcOutputMessage response, Throwable exception) {
//		EncoderJsonRpcMessageWriter<CharSequence> writer = new EncoderJsonRpcMessageWriter<>(CharSequenceEncoder.allMimeTypes());
//		ResolvableType elementType = ResolvableType.forClass(String.class);
//		Mono<String> publisher = Mono.just("error");
//		return writer.write(publisher, elementType, response, Collections.emptyMap());
//		return Mono.empty();

		log.error("Unhandled failure: " + exception.getMessage() + ", response already set");
		return Mono.error(exception);
	}
}
