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
package org.springframework.dsl.autoconfigure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.jsonrpc.JsonRpcSystemConstants;
import org.springframework.dsl.jsonrpc.support.DispatcherJsonRpcHandler;
import org.springframework.dsl.lsp.client.ClientReactorJsonRpcHandlerAdapter;
import org.springframework.dsl.lsp.client.config.EnableLanguageClient;
import org.springframework.dsl.lsp.server.config.LspDomainJacksonConfiguration;
import org.springframework.dsl.lsp.server.jsonrpc.ReactorJsonRpcHandlerAdapter;
import org.springframework.dsl.lsp.server.jsonrpc.RpcHandler;
import org.springframework.dsl.lsp.server.jsonrpc.RpcJsonRpcHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link EnableAutoConfiguration Auto-configuration} integrating into {@code LSP Client} features.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.dsl.lsp.client", name = "mode")
@EnableLanguageClient
@Import({ LspDomainJacksonConfiguration.class })
public class LspClientAutoConfiguration {

	@Bean
	public ReactiveAdapterRegistry jsonRpcAdapterRegistry() {
		return new ReactiveAdapterRegistry();
	}

	@Bean
	public DispatcherJsonRpcHandler dispatcherJsonRpcHandler() {
		return new DispatcherJsonRpcHandler();
	}

	@Bean
	public ClientReactorJsonRpcHandlerAdapter clientReactorJsonRpcHandlerAdapter(RpcHandler rpcHandler,
			@Qualifier(JsonRpcSystemConstants.JSONRPC_OBJECT_MAPPER_BEAN_NAME) ObjectMapper objectMapper) {
		return new ClientReactorJsonRpcHandlerAdapter(rpcHandler, objectMapper);
	}

	@Bean
	public RpcJsonRpcHandlerAdapter rpcJsonRpcHandlerAdapter(DispatcherJsonRpcHandler dispatcherJsonRpcHandler) {
		return new RpcJsonRpcHandlerAdapter(dispatcherJsonRpcHandler);
	}

	@Bean
	public ReactorJsonRpcHandlerAdapter reactorJsonRpcHandlerAdapter(RpcJsonRpcHandlerAdapter rpcJsonRpcHandlerAdapter,
			@Qualifier(JsonRpcSystemConstants.JSONRPC_OBJECT_MAPPER_BEAN_NAME) ObjectMapper objectMapper) {
		return new ReactorJsonRpcHandlerAdapter(rpcJsonRpcHandlerAdapter, objectMapper);
	}
}
