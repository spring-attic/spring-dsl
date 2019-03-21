/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.lsp.server.websocket.servlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dsl.lsp.server.config.DslProperties;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration for a servlet based websocket features.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@EnableWebSocket
@Import(LspServletWebSocketHandlerConfig.class)
public class LspServletWebSocketConfig implements WebSocketConfigurer {

	private final DslProperties properties;
	private final WebSocketHandler lspServletWebSocketHandler;

	public LspServletWebSocketConfig(DslProperties properties,
			WebSocketHandler lspServletWebSocketHandler) {
		this.properties = properties;
		this.lspServletWebSocketHandler = lspServletWebSocketHandler;
	}

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(lspServletWebSocketHandler, properties.getLsp().getServer().getWebsocket().getPath());
    }
}
