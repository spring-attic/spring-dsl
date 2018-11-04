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

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.lsp.server.config.DslConfigurationProperties;
import org.springframework.dsl.lsp.server.config.EnableLanguageServer;
import org.springframework.dsl.lsp.server.config.LspServerSocketConfiguration;
import org.springframework.dsl.lsp.server.config.LspServerStdioConfiguration;
import org.springframework.dsl.lsp.server.support.JvmLspExiter;
import org.springframework.dsl.lsp.server.support.LspExiter;
import org.springframework.dsl.lsp.server.websocket.LspWebSocketConfig;
import org.springframework.web.reactive.socket.WebSocketHandler;

/**
 * {@link EnableAutoConfiguration Auto-configuration} integrating into {@code LSP} features.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.dsl.lsp.server", name = "mode")
@EnableConfigurationProperties(DslConfigurationProperties.class)
@EnableLanguageServer
public class LspServerAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "spring.dsl.lsp.server", name = "force-jvm-exit-on-shutdown", havingValue = "true")
	public LspExiter lspExiter() {
		return new JvmLspExiter();
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveAdapterRegistry.class)
	public ReactiveAdapterRegistry jsonRpcAdapterRegistry() {
		return new ReactiveAdapterRegistry();
	}

	@ConditionalOnProperty(prefix = "spring.dsl.lsp.server", name = "mode", havingValue = "WEBSOCKET")
	@ConditionalOnClass(value = WebSocketHandler.class)
	@Configuration
	@Import({ LspWebSocketConfig.class })
	public static class LspServerWebsocketConfig {
	}

	@ConditionalOnProperty(prefix = "spring.dsl.lsp.server", name = "mode", havingValue = "PROCESS")
	@Configuration
	@Import({ LspServerStdioConfiguration.class })
	public static class LspServerProcessConfig {
	}

	@ConditionalOnProperty(prefix = "spring.dsl.lsp.server", name = "mode", havingValue = "SOCKET")
	@Configuration
	@Import({ LspServerSocketConfiguration.class })
	public static class LspServerSocketConfig {
	}
}
