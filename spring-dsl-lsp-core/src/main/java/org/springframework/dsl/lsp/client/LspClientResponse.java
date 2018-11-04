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

import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractor;
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractorStrategies;

import reactor.core.publisher.Mono;

/**
 * Represents a {@code JSONRPC} response, as returned by {@link LspClient}.
 *
 * @author Janne Valkealahti
 *
 */
public interface LspClientResponse {

	/**
	 * Return the strategies used to convert the result of this response.
	 *
	 * @return the json rpc extractor strategies
	 */
	JsonRpcExtractorStrategies strategies();

	/**
	 * Extract the result to a {@link Mono}.
	 *
	 * @param <T> the element type
	 * @param elementClass the class of element in the {@link Mono}
	 * @return a mono containing the result of the given type {@code T}
	 */
	<T> Mono<T> resultToMono(Class<? extends T> elementClass);

	/**
	 * Extract the result with the given {@link JsonRpcExtractor}.
	 *
	 * @param <T> the type f the result returned
	 * @param extractor the {@link JsonRpcExtractor} that reads from the response
	 * @return the extracted result
	 */
	<T> T result(JsonRpcExtractor<T, JsonRpcResponse> extractor);

	/**
	 * Gets the wrapped response.
	 *
	 * @return the json rpc response
	 */
	JsonRpcResponse response();

	/**
	 * Creates the builder using default {@link JsonRpcExtractorStrategies}.
	 *
	 * @return the builder
	 */
	static Builder create() {
		return new DefaultLspClientResponseBuilder(JsonRpcExtractorStrategies.withDefaults());
	}

	/**
	 * Creates the builder using given {@link JsonRpcExtractorStrategies}.
	 *
	 * @param strategies the strategies
	 * @return the builder
	 */
	static Builder create(JsonRpcExtractorStrategies strategies) {
		return new DefaultLspClientResponseBuilder(strategies);
	}

	/**
	 * Defines a builder for a response.
	 */
	interface Builder {

		/**
		 * Set the used response.
		 *
		 * @param response the response
		 * @return the builder for chaining
		 */
		Builder response(JsonRpcResponse response);

		/**
		 * Builds the {@link LspClientResponse}.
		 *
		 * @return the lsp client response
		 */
		LspClientResponse build();
	}
}
