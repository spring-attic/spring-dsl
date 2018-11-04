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
package org.springframework.dsl.jsonrpc.codec;

import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilderCustomizer;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonRpcExtractorStrategies {

	ObjectMapper objectMapper();

	/**
	 * Return a new {@code JsonRpcExtractorStrategies} with default initialization.
	 *
	 * @return the new {@code JsonRpcExtractorStrategies}
	 */
	static JsonRpcExtractorStrategies withDefaults() {
		return builder().build();
	}

	/**
	 * Return a mutable builder for a {@code JsonRpcExtractorStrategies} with default initialization.
	 *
	 * @return the builder
	 */
	static Builder builder() {
		DefaultJsonRpcExtractorStrategiesBuilder builder = new DefaultJsonRpcExtractorStrategiesBuilder();
		builder.defaultConfiguration();
		return builder;
	}

	/**
	 * Return a mutable, empty builder for a {@code JsonRpcExtractorStrategies}.
	 *
	 * @return the builder
	 */
	static Builder empty() {
		return new DefaultJsonRpcExtractorStrategiesBuilder();
	}

	interface Builder {

		Builder jackson(JsonRpcJackson2ObjectMapperBuilderCustomizer customizer);
		JsonRpcExtractorStrategies build();
	}
}
