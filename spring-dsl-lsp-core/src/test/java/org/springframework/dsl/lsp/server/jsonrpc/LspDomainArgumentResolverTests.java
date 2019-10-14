/*
 * Copyright 2018-2019 the original author or authors.
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
import org.springframework.dsl.domain.CodeLensParams;
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
import org.springframework.dsl.domain.WorkspaceSymbolParams;
import org.springframework.dsl.jsonrpc.ResolvableMethod;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilder;
import org.springframework.dsl.jsonrpc.support.MockJsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.support.MockServerJsonRpcExchange;
import org.springframework.dsl.lsp.server.config.LspDomainJacksonConfiguration;

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
		LspDomainJacksonConfiguration configuration = new LspDomainJacksonConfiguration();
		JsonRpcJackson2ObjectMapperBuilder builder = new JsonRpcJackson2ObjectMapperBuilder();
		configuration.lspJackson2ObjectMapperBuilderCustomizer().customize(builder);
		this.resolver = new LspDomainArgumentResolver(builder.build());
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
		param = this.testMethod.arg(CodeLensParams.class);
		assertThat(this.resolver.supportsParameter(param)).isTrue();
		param = this.testMethod.arg(WorkspaceSymbolParams.class);
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

	@Test
	public void testJacksonDeserialization() {
		String params = "{  \"processId\": 24297,  \"rootPath\": \"/home/jvalkealahti/Documents/vscode/test/log-sink-rabbit\",  \"rootUri\": \"file:///home/jvalkealahti/Documents/vscode/test/log-sink-rabbit\",  \"capabilities\": {    \"workspace\": {      \"applyEdit\": true,      \"workspaceEdit\": {        \"documentChanges\": true,        \"resourceOperations\": [          \"create\",          \"rename\",          \"delete\"        ],        \"failureHandling\": \"textOnlyTransactional\"      },      \"didChangeConfiguration\": {        \"dynamicRegistration\": true      },      \"didChangeWatchedFiles\": {        \"dynamicRegistration\": true      },      \"symbol\": {        \"dynamicRegistration\": true,        \"symbolKind\": {          \"valueSet\": [            1,            2,            3,            4,            5,            6,            7,            8,            9,            10,            11,            12,            13,            14,            15,            16,            17,            18,            19,            20,            21,            22,            23,            24,            25,            26          ]        }      },      \"executeCommand\": {        \"dynamicRegistration\": true      },      \"configuration\": true,      \"workspaceFolders\": true    },    \"textDocument\": {      \"publishDiagnostics\": {        \"relatedInformation\": true      },      \"synchronization\": {        \"dynamicRegistration\": true,        \"willSave\": true,        \"willSaveWaitUntil\": true,        \"didSave\": true      },      \"completion\": {        \"dynamicRegistration\": true,        \"contextSupport\": true,        \"completionItem\": {          \"snippetSupport\": true,          \"commitCharactersSupport\": true,          \"documentationFormat\": [            \"markdown\",            \"plaintext\"          ],          \"deprecatedSupport\": true,          \"preselectSupport\": true        },        \"completionItemKind\": {          \"valueSet\": [            1,            2,            3,            4,            5,            6,            7,            8,            9,            10,            11,            12,            13,            14,            15,            16,            17,            18,            19,            20,            21,            22,            23,            24,            25          ]        }      },      \"hover\": {        \"dynamicRegistration\": true,        \"contentFormat\": [          \"markdown\",          \"plaintext\"        ]      },      \"signatureHelp\": {        \"dynamicRegistration\": true,        \"signatureInformation\": {          \"documentationFormat\": [            \"markdown\",            \"plaintext\"          ],          \"parameterInformation\": {            \"labelOffsetSupport\": true          }        }      },      \"definition\": {        \"dynamicRegistration\": true,        \"linkSupport\": true      },      \"references\": {        \"dynamicRegistration\": true      },      \"documentHighlight\": {        \"dynamicRegistration\": true      },      \"documentSymbol\": {        \"dynamicRegistration\": true,        \"symbolKind\": {          \"valueSet\": [            1,            2,            3,            4,            5,            6,            7,            8,            9,            10,            11,            12,            13,            14,            15,            16,            17,            18,            19,            20,            21,            22,            23,            24,            25,            26          ]        },        \"hierarchicalDocumentSymbolSupport\": true      },      \"codeAction\": {        \"dynamicRegistration\": true,        \"codeActionLiteralSupport\": {          \"codeActionKind\": {            \"valueSet\": [              \"\",              \"quickfix\",              \"refactor\",              \"refactor.extract\",              \"refactor.inline\",              \"refactor.rewrite\",              \"source\",              \"source.organizeImports\"            ]          }        }      },      \"codeLens\": {        \"dynamicRegistration\": true      },      \"formatting\": {        \"dynamicRegistration\": true      },      \"rangeFormatting\": {        \"dynamicRegistration\": true      },      \"onTypeFormatting\": {        \"dynamicRegistration\": true      },      \"rename\": {        \"dynamicRegistration\": true,        \"prepareSupport\": true      },      \"documentLink\": {        \"dynamicRegistration\": true      },      \"typeDefinition\": {        \"dynamicRegistration\": true,        \"linkSupport\": true      },      \"implementation\": {        \"dynamicRegistration\": true,        \"linkSupport\": true      },      \"colorProvider\": {        \"dynamicRegistration\": true      },      \"foldingRange\": {        \"dynamicRegistration\": true,        \"rangeLimit\": 5000,        \"lineFoldingOnly\": true      },      \"declaration\": {        \"dynamicRegistration\": true,        \"linkSupport\": true      }    }  },  \"trace\": \"off\",  \"workspaceFolders\": [    {      \"uri\": \"file:///home/jvalkealahti/Documents/vscode/test/log-sink-rabbit\",      \"name\": \"log-sink-rabbit\"    },    {      \"uri\": \"file:///home/jvalkealahti/Documents/vscode/test/time-source-rabbit\",      \"name\": \"time-source-rabbit\"    },    {      \"uri\": \"file:///home/jvalkealahti/Documents/vscode/test/dataflow\",      \"name\": \"dataflow\"    }  ]}";
		MockServerJsonRpcExchange exchange = MockServerJsonRpcExchange.from(MockJsonRpcInputMessage.get("")
				.body(params));
		MethodParameter param = this.testMethod.arg(InitializeParams.class);
		resolve(param, exchange);
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
			RenameParams renameParams,
			CodeLensParams codeLensParams,
			WorkspaceSymbolParams workspaceSymbolParams
			) {}
}
