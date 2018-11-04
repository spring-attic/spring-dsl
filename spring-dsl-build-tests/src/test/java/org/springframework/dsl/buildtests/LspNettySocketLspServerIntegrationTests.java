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
package org.springframework.dsl.buildtests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dsl.autoconfigure.DslAutoConfiguration;
import org.springframework.dsl.autoconfigure.LanguageServerControllerAutoConfiguration;
import org.springframework.dsl.autoconfigure.LspClientAutoConfiguration;
import org.springframework.dsl.autoconfigure.LspServerAutoConfiguration;
import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.lsp.client.ClientReactorJsonRpcHandlerAdapter;
import org.springframework.dsl.lsp.client.NettyTcpClientLspClient;
import org.springframework.dsl.lsp.server.jsonrpc.NettyTcpServer;

/**
 * Command flow tests using Netty's socker server integration into generic lsp
 * controllers using language hooks defined in {@link AbstractLspIntegrationTests}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspNettySocketLspServerIntegrationTests extends AbstractLspIntegrationTests {

	private NettyTcpServer server;
	private NettyTcpClientLspClient client;

	@Test
	public void testLspInitialize() {
		assertThat(server).isNotNull();
		assertThat(client).isNotNull();

		InitializeParams initializeParams = InitializeParams.initializeParams()
			.capabilities()
				.textDocument()
					.synchronization()
					.and()
				.and()
			.and()
			.build();

		JsonRpcResponse response = client.request()
				.id("1")
				.method("initialize")
				.params(initializeParams)
				.exchange().block().response();
		assertThat(response).isNotNull();
		assertThat(response.getError()).isNull();
		assertThat(response.getResult()).contains("textDocumentSync");
	}

	@Override
	protected ConfigurableApplicationContext buildServerContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DslAutoConfiguration.class,
				LspServerAutoConfiguration.class, LanguageServerControllerAutoConfiguration.class);
		SpringApplication springApplication = builder.build();
		return springApplication.run("--spring.dsl.lsp.server.mode=SOCKET",
				"--logging.level.org.springframework.dsl=trace", "--logging.level.reactor.ipc.netty.tcp=debug");
	}

	@Override
	protected ConfigurableApplicationContext buildClientContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DslAutoConfiguration.class,
				LspClientAutoConfiguration.class);
		SpringApplication springApplication = builder.build();
		return springApplication.run("--spring.dsl.lsp.client.mode=SOCKET",
				"--logging.level.org.springframework.dsl=trace", "--logging.level.reactor.ipc.netty.tcp=debug", "--logging.level.root=debug");
	}

	@Override
	protected void onServerContext(ConfigurableApplicationContext context) {
		server = context.getBean(NettyTcpServer.class);
	}

	@Override
	protected void onClientContext(ConfigurableApplicationContext context) {
		ClientReactorJsonRpcHandlerAdapter adapter = context.getBean(ClientReactorJsonRpcHandlerAdapter.class);
		client = new NettyTcpClientLspClient("0.0.0.0", server.getPort(), adapter, adapter);
		client.start();
	}
}
