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
package org.springframework.dsl.lsp.server.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dsl.jsonrpc.JsonRpcSystemConstants;
import org.springframework.dsl.jsonrpc.config.JsonRpcJacksonConfiguration;
import org.springframework.dsl.jsonrpc.support.DispatcherJsonRpcHandler;
import org.springframework.dsl.lsp.server.jsonrpc.LspClientArgumentResolver;
import org.springframework.dsl.lsp.server.jsonrpc.LspDomainArgumentResolver;
import org.springframework.dsl.lsp.server.jsonrpc.ReactorJsonRpcHandlerAdapter;
import org.springframework.dsl.lsp.server.jsonrpc.RpcJsonRpcHandlerAdapter;
import org.springframework.dsl.service.reconcile.DefaultReconciler;
import org.springframework.dsl.service.reconcile.Linter;
import org.springframework.dsl.service.reconcile.Reconciler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic configurations among {@code LSP} features.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@Import({ JsonRpcJacksonConfiguration.class, LspDomainJacksonConfiguration.class })
public class GenericLspConfiguration {

	@Bean
	public Reconciler reconciler(Optional<List<Linter>> linters) {
		return new DefaultReconciler(linters.orElseGet(ArrayList::new));
	}

	@Bean
	public LspDomainArgumentResolver lspDomainArgumentResolver() {
		return new LspDomainArgumentResolver();
	}

	@Bean
	public LspClientArgumentResolver lspClientArgumentResolver() {
		return new LspClientArgumentResolver();
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
