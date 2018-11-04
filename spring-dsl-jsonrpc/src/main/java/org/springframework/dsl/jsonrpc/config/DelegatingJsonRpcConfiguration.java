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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.jsonrpc.JsonRpcSystemConstants;
import org.springframework.dsl.jsonrpc.codec.Jackson2JsonRpcMessageWriter;
import org.springframework.dsl.jsonrpc.codec.JsonRpcMessageWriter;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcHandlerMethodArgumentResolver;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcMethodParamsArgumentResolver;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcRequestParamsArgumentResolver;
import org.springframework.dsl.jsonrpc.result.method.annotation.JsonRpcNotificationResultHandler;
import org.springframework.dsl.jsonrpc.result.method.annotation.JsonRpcRequestMappingHandlerAdapter;
import org.springframework.dsl.jsonrpc.result.method.annotation.JsonRpcRequestMappingHandlerMapping;
import org.springframework.dsl.jsonrpc.result.method.annotation.JsonRpcResponseResultResultHandler;
import org.springframework.dsl.jsonrpc.result.method.annotation.ServerJsonRpcExchangeArgumentResolver;
import org.springframework.dsl.jsonrpc.support.DispatcherJsonRpcHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class DelegatingJsonRpcConfiguration {

	@Bean
	public DispatcherJsonRpcHandler dispatcherJsonRpcHandler() {
		return new DispatcherJsonRpcHandler();
	}

	@Bean
	public Jackson2JsonRpcMessageWriter jackson2JsonRpcMessageWriter(
			@Qualifier(JsonRpcSystemConstants.JSONRPC_OBJECT_MAPPER_BEAN_NAME) ObjectMapper objectMapper) {
		return new Jackson2JsonRpcMessageWriter(objectMapper);
	}

	@Bean
	public JsonRpcResponseResultResultHandler jsonRpcResponseBodyResultHandler(
			List<JsonRpcMessageWriter<?>> messageWriters, ReactiveAdapterRegistry adapterRegistry) {
		return new JsonRpcResponseResultResultHandler(messageWriters, adapterRegistry);
	}

	@Bean
	public JsonRpcNotificationResultHandler jsonRpcNotificationResultHandler(
			List<JsonRpcMessageWriter<?>> messageWriters, ReactiveAdapterRegistry adapterRegistry) {
		return new JsonRpcNotificationResultHandler(messageWriters, adapterRegistry);
	}

	@Bean
	public JsonRpcRequestMappingHandlerMapping jsonRpcRequestMappingHandlerMapping() {
		return new JsonRpcRequestMappingHandlerMapping();
	}

	@Bean
	public ServerJsonRpcExchangeArgumentResolver serverJsonRpcExchangeArgumentResolver() {
		return new ServerJsonRpcExchangeArgumentResolver();
	}

	@Bean
	public JsonRpcRequestMappingHandlerAdapter jsonRpcRequestMappingHandlerAdapter(
			List<JsonRpcHandlerMethodArgumentResolver> resolvers) {
		return new JsonRpcRequestMappingHandlerAdapter(resolvers);
	}

	@Bean
	public JsonRpcMethodParamsArgumentResolver jsonRpcMethodParamsArgumentResolver() {
		return new JsonRpcMethodParamsArgumentResolver();
	}
	
	@Bean
	public JsonRpcRequestParamsArgumentResolver jsonRpcRequestParamsArgumentResolver() {
		return new JsonRpcRequestParamsArgumentResolver();
	}
}
