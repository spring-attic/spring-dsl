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

import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractorStrategies.Builder;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilder;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilderCustomizer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultJsonRpcExtractorStrategiesBuilder implements JsonRpcExtractorStrategies.Builder {

	private JsonRpcJackson2ObjectMapperBuilder builder = new JsonRpcJackson2ObjectMapperBuilder();

	public DefaultJsonRpcExtractorStrategiesBuilder() {
	}

	public void defaultConfiguration() {
	}

	@Override
	public Builder jackson(JsonRpcJackson2ObjectMapperBuilderCustomizer customizer) {
		customizer.customize(builder);
		return this;
	}

	@Override
	public JsonRpcExtractorStrategies build() {
		return new DefaultJsonRpcExtractorStrategies(builder.build());
	}

	private static class DefaultJsonRpcExtractorStrategies implements JsonRpcExtractorStrategies {

		private final ObjectMapper objectMapper;

		public DefaultJsonRpcExtractorStrategies(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Override
		public ObjectMapper objectMapper() {
			return objectMapper;
		}
	}
}
