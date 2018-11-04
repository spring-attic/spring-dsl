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
package org.springframework.dsl.jsonrpc.config;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.jsonrpc.JsonRpcSystemConstants;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilder;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilderCustomizer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration in a {@code JSONRPC} for jackson {@link ObjectMapper} via
 * {@link JsonRpcJackson2ObjectMapperBuilder} allowing others to configure it through
 * {@link JsonRpcJackson2ObjectMapperBuilderCustomizer} beans.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
public class JsonRpcJacksonConfiguration {

	private final ApplicationContext applicationContext;

	public JsonRpcJacksonConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean(JsonRpcSystemConstants.JSONRPC_OBJECT_MAPPER_BEAN_NAME)
	public ObjectMapper jsonRpcObjectMapper(JsonRpcJackson2ObjectMapperBuilder builder) {
		return builder.build();
	}

	@Bean
	public JsonRpcJackson2ObjectMapperBuilder jsonRpcJacksonObjectMapperBuilder(
			List<JsonRpcJackson2ObjectMapperBuilderCustomizer> customizers) {
		JsonRpcJackson2ObjectMapperBuilder builder = new JsonRpcJackson2ObjectMapperBuilder();
		builder.applicationContext(this.applicationContext);
		customize(builder, customizers);
		return builder;
	}

	private void customize(JsonRpcJackson2ObjectMapperBuilder builder,
			List<JsonRpcJackson2ObjectMapperBuilderCustomizer> customizers) {
		for (JsonRpcJackson2ObjectMapperBuilderCustomizer customizer : customizers) {
			customizer.customize(builder);
		}
	}
}
