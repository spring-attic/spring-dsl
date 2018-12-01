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
package org.springframework.dsl.lsp.server.websocket.servlet;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.jsonrpc.JsonRpcSystemConstants;
import org.springframework.dsl.lsp.server.jsonrpc.RpcHandler;
import org.springframework.web.socket.WebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration for a servlet based {@link WebSocketHandler}.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
public class LspServletWebSocketHandlerConfig {

	@Bean
	public WebSocketHandler lspServletWebSocketHandler(RpcHandler rpcHandler,
			@Qualifier(JsonRpcSystemConstants.JSONRPC_OBJECT_MAPPER_BEAN_NAME) ObjectMapper objectMapper) {
		return new LspServletWebSocketHandler(rpcHandler, objectMapper);
	}
}
