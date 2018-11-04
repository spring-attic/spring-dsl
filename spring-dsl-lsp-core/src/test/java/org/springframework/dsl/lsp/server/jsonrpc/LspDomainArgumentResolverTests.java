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
package org.springframework.dsl.lsp.server.jsonrpc;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dsl.domain.CompletionParams;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.DidCloseTextDocumentParams;
import org.springframework.dsl.domain.DidOpenTextDocumentParams;
import org.springframework.dsl.domain.DidSaveTextDocumentParams;
import org.springframework.dsl.domain.DocumentSymbolParams;
import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.domain.InitializedParams;
import org.springframework.dsl.domain.RenameParams;
import org.springframework.dsl.domain.TextDocumentPositionParams;
import org.springframework.dsl.jsonrpc.ResolvableMethod;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.support.MockJsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.support.MockServerJsonRpcExchange;

/**
 * Tests for {@link LspDomainArgumentResolver}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspDomainArgumentResolverTests {

	private LspDomainArgumentResolver resolver;
	private ResolvableMethod testMethod = ResolvableMethod.on(getClass()).named("handle").build();

	@Before
	public void setup() {
		this.resolver = new LspDomainArgumentResolver();
	}

	@Test
    public void testSupports() throws Exception {
		MethodParameter param;

		param = this.testMethod.arg(InitializeParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(InitializedParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(DidChangeTextDocumentParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(DidCloseTextDocumentParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(DidOpenTextDocumentParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(DidSaveTextDocumentParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(CompletionParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(TextDocumentPositionParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(DocumentSymbolParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(RenameParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
	}

	@Test
	public void testInitializeParams() {
		String params = "{" +
				"\"processId\":1," +
				"\"rootUri\":\"rootUri\"," +
				"\"initializationOptions\":\"initializationOptions\"," +
				"\"trace\":\"trace\"," +
				"\"capabilities\":{\"experimental\":\"experimental\"}" +
				"}";
		MockServerJsonRpcExchange exchange = MockServerJsonRpcExchange.from(MockJsonRpcInputMessage.get("")
				.body(params));
		MethodParameter param = this.testMethod.arg(InitializeParams.class);
		Object result = resolve(param, exchange);
		InitializeParams expected = InitializeParams.initializeParams()
				.processId(1)
				.rootUri("rootUri")
				.initializationOptions("initializationOptions")
				.trace("trace")
				.capabilities()
					.experimental("experimental")
					.and()
				.build();
		assertThat(result).isEqualTo(expected);
	}

	public void testDidOpenTextDocumentParams() {

	}

	private Object resolve(MethodParameter parameter, ServerJsonRpcExchange exchange) {
		return this.resolver.resolveArgument(parameter, exchange).block(Duration.ZERO);
	}

	void handle(
			InitializeParams initializeParams,
			InitializedParams initializedParams,
			DidChangeTextDocumentParams didChangeTextDocumentParams,
			DidCloseTextDocumentParams didCloseTextDocumentParams,
			DidOpenTextDocumentParams didOpenTextDocumentParams,
			DidSaveTextDocumentParams didSaveTextDocumentParams,
			CompletionParams completionParams,
			TextDocumentPositionParams textDocumentPositionParams,
			DocumentSymbolParams documentSymbolParams,
			RenameParams renameParams
			) {}
}
