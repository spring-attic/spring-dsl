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
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractorStrategies;
import org.springframework.dsl.jsonrpc.support.DefaultJsonRpcRequest;
import org.springframework.dsl.lsp.server.config.LspDomainJacksonConfiguration;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * Base implementation of a {@link LspClient} having some shared functionality.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractLspClient implements LspClient {

	private JsonRpcExtractorStrategies jsonRpcExtractorStrategies;

	public AbstractLspClient() {
		this.jsonRpcExtractorStrategies = JsonRpcExtractorStrategies.builder()
				.jackson(LspDomainJacksonConfiguration.DEFAULT_CUSTOMIZERS)
				.build();
	}

	@Override
	public final void start() {
		doStart();
	}

	@Override
	public final void stop() {
		doStop();
	}

	@Override
	public abstract RequestSpec request();

	@Override
	public abstract NotificationSpec notification();

	public JsonRpcExtractorStrategies getJsonRpcExtractorStrategies() {
		return jsonRpcExtractorStrategies;
	}

	public void setJsonRpcExtractorStrategies(JsonRpcExtractorStrategies jsonRpcExtractorStrategies) {
		this.jsonRpcExtractorStrategies = jsonRpcExtractorStrategies;
	}

	/**
	 * Actual {@link #start()} which a sub-class can override.
	 */
	protected void doStart() {
	}

	/**
	 * Actual {@link #stop()} which a sub-class can override.
	 */
	protected void doStop() {
	}

	/**
	 * Default request spec delegating to {@link ExchangeRequestFunction}.
	 */
	protected static class DefaultRequestSpec implements RequestSpec {

		private String id;
		private String method;
		private Object params;
		private ExchangeRequestFunction exchangeFunction;

		/**
		 * Instantiates a new default request spec.
		 *
		 * @param exchangeFunction the exchange function
		 */
		public DefaultRequestSpec(ExchangeRequestFunction exchangeFunction) {
			Assert.notNull(exchangeFunction, "ExchangeRequestFunction must be set");
			this.exchangeFunction = exchangeFunction;
		}

		@Override
		public RequestSpec id(String id) {
			this.id = id;
			return this;
		}

		@Override
		public RequestSpec method(String method) {
			this.method = method;
			return this;
		}

		@Override
		public RequestSpec params(Object params) {
			this.params = params;
			return this;
		}

		@Override
		public Mono<LspClientResponse> exchange() {
			JsonRpcRequest request = new DefaultJsonRpcRequest(id, method, params);
			return exchangeFunction.exchange(request);
		}
	}

	/**
	 * Default notification spec delegating to {@link ExchangeNotificationFunction}.
	 */
	protected static class DefaultNotificationSpec implements NotificationSpec {

		private String method;
		private Object params;
		private ExchangeNotificationFunction exchangeFunction;

		/**
		 * Instantiates a new default notification spec.
		 *
		 * @param exchangeFunction the exchange function
		 */
		public DefaultNotificationSpec(ExchangeNotificationFunction exchangeFunction) {
			Assert.notNull(exchangeFunction, "ExchangeNotificationFunction must be set");
			this.exchangeFunction = exchangeFunction;
		}

		@Override
		public NotificationSpec method(String method) {
			this.method = method;
			return this;
		}

		@Override
		public NotificationSpec params(Object params) {
			this.params = params;
			return this;
		}

		@Override
		public Mono<Void> exchange() {
			DefaultJsonRpcRequest request = new DefaultJsonRpcRequest(null, method, params);
			return exchangeFunction.exchange(request);
		}
	}

}
