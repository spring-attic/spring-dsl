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
package org.springframework.dsl.lsp.docs;

import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.dsl.domain.LogMessageParams;
import org.springframework.dsl.domain.MessageType;
import org.springframework.dsl.domain.ShowMessageRequestParams;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.lsp.client.ClientReactorJsonRpcHandlerAdapter;
import org.springframework.dsl.lsp.client.LspClient;

import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
public class ClientDocs {

	private ApplicationContext clientContext;

	private void client1() {
// tag::snippet1[]
		ClientReactorJsonRpcHandlerAdapter handler =
				clientContext.getBean(ClientReactorJsonRpcHandlerAdapter.class);
		LspClient lspClient = LspClient.builder()
				.host("0.0.0.0")
				.port(8080)
				.function(handler)
				.processor(handler)
				.build();
		lspClient.start();
// end::snippet1[]
	}

// tag::snippet2[]
	@JsonRpcController
	@JsonRpcRequestMapping(method = "example/")
	public class Example1CommandsController {

		@JsonRpcRequestMapping(method = "log")
		@JsonRpcNotification
		public Mono<Void> sendLogNotification(LspClient lspClient) {
			return lspClient.notification()
				.method("window/logMessage")
				.params(LogMessageParams.from("hi"))
				.exchange()
				.then();
		}
	}
// end::snippet2[]

// tag::snippet3[]
	@JsonRpcController
	@JsonRpcRequestMapping(method = "example/")
	public class Example2CommandsController {

		@JsonRpcRequestMapping(method = "message")
		@JsonRpcNotification
		public Mono<Void> showMessage(LspClient lspClient) {
			return lspClient.request()
				.id(UUID.randomUUID().toString())
				.method("window/showMessageRequest")
				.params(ShowMessageRequestParams.showMessageRequestParams()
					.type(MessageType.Info)
					.message("message")
					.build())
				.exchange()
				.then();
		}
	}
// end::snippet3[]
}
