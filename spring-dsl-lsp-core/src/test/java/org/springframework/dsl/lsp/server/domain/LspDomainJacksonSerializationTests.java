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
package org.springframework.dsl.lsp.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dsl.domain.ClientCapabilities;
import org.springframework.dsl.domain.Command;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.CompletionItemKind;
import org.springframework.dsl.domain.CompletionList;
import org.springframework.dsl.domain.CompletionOptions;
import org.springframework.dsl.domain.Diagnostic;
import org.springframework.dsl.domain.DiagnosticSeverity;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.DocumentSymbolParams;
import org.springframework.dsl.domain.DynamicRegistration;
import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.InitializeResult;
import org.springframework.dsl.domain.InsertTextFormat;
import org.springframework.dsl.domain.Location;
import org.springframework.dsl.domain.LogMessageParams;
import org.springframework.dsl.domain.MarkupContent;
import org.springframework.dsl.domain.MarkupKind;
import org.springframework.dsl.domain.MessageActionItem;
import org.springframework.dsl.domain.MessageParams;
import org.springframework.dsl.domain.MessageType;
import org.springframework.dsl.domain.PublishDiagnosticsParams;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.domain.Registration;
import org.springframework.dsl.domain.RegistrationParams;
import org.springframework.dsl.domain.RenameParams;
import org.springframework.dsl.domain.ServerCapabilities;
import org.springframework.dsl.domain.ShowMessageRequestParams;
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.domain.Synchronization;
import org.springframework.dsl.domain.TextDocumentClientCapabilities;
import org.springframework.dsl.domain.TextDocumentEdit;
import org.springframework.dsl.domain.TextDocumentPositionParams;
import org.springframework.dsl.domain.TextDocumentSyncKind;
import org.springframework.dsl.domain.TextDocumentSyncOptions;
import org.springframework.dsl.domain.TextEdit;
import org.springframework.dsl.domain.Unregistration;
import org.springframework.dsl.domain.WorkspaceEdit;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilder;
import org.springframework.dsl.lsp.server.config.LspDomainJacksonConfiguration;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class LspDomainJacksonSerializationTests {

	private ObjectMapper mapper;

	@Before
	public void setup() {
		LspDomainJacksonConfiguration configuration = new LspDomainJacksonConfiguration();
		JsonRpcJackson2ObjectMapperBuilder builder = new JsonRpcJackson2ObjectMapperBuilder();
		configuration.lspJackson2ObjectMapperBuilderCustomizer().customize(builder);
		this.mapper = builder.build();
	}

	@After
	public void clean() {
		mapper = null;
	}

	@Test
	public void testCompletionOptions() throws Exception {
		CompletionOptions from = new CompletionOptions();
		String json = mapper.writeValueAsString(from);
		CompletionOptions to = mapper.readValue(json, CompletionOptions.class);
		assertObjects(from, to);

		from = CompletionOptions.completionOptions()
			.resolveProvider(true)
			.triggerCharacters(Arrays.asList("a", "b"))
			.build();
		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, CompletionOptions.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("CompletionOptions1.json");
		to = mapper.readValue(expect, CompletionOptions.class);
		assertObjects(from, to);
	}

	@Test
	public void testTextDocumentSyncOptions() throws Exception {
		TextDocumentSyncOptions from = new TextDocumentSyncOptions();
		String json = mapper.writeValueAsString(from);
		TextDocumentSyncOptions to = mapper.readValue(json, TextDocumentSyncOptions.class);
		assertObjects(from, to);

		from = TextDocumentSyncOptions.textDocumentSyncOptions()
				.openClose(true)
				.change(TextDocumentSyncKind.Incremental)
				.willSave(true)
				.willSaveWaitUntil(true)
				.build();

		json = mapper.writeValueAsString(from);
		Integer change = JsonPath.read(json, "change");
		assertThat(change).isEqualTo(2);

		to = mapper.readValue(json, TextDocumentSyncOptions.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("TextDocumentSyncOptions1.json");
		to = mapper.readValue(expect, TextDocumentSyncOptions.class);
		assertObjects(from, to);
	}

	@Test
	public void testServerCapabilities() throws Exception {
		ServerCapabilities from = new ServerCapabilities();
		String json = mapper.writeValueAsString(from);
		ServerCapabilities to = mapper.readValue(json, ServerCapabilities.class);
		assertObjects(from, to);

		from = ServerCapabilities.serverCapabilities()
				.textDocumentSyncOptions()
					.openClose(true)
					.willSave(true)
					.willSaveWaitUntil(true)
					.and()
				.hoverProvider(true)
				.documentSymbolProvider(true)
				.completionProvider()
					.resolveProvider(true)
					.triggerCharacters(Arrays.asList("a", "b"))
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, ServerCapabilities.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("ServerCapabilities1.json");
		to = mapper.readValue(expect, ServerCapabilities.class);
		assertObjects(from, to);

		from = ServerCapabilities.serverCapabilities()
				.textDocumentSyncKind(TextDocumentSyncKind.Incremental)
				.hoverProvider(true)
				.completionProvider()
					.resolveProvider(true)
					.triggerCharacters(Arrays.asList("a", "b"))
					.and()
				.renameProvider(true)
				.build();
		expect = loadResourceAsString("ServerCapabilities2.json");
		to = mapper.readValue(expect, ServerCapabilities.class);
		assertObjects(from, to);
	}

	@Test
	public void testInitializeResult() throws Exception {
		InitializeResult from = new InitializeResult();
		String json = mapper.writeValueAsString(from);
		InitializeResult to = mapper.readValue(json, InitializeResult.class);
		assertObjects(from, to);

		from = InitializeResult.initializeResult()
				.capabilities()
					.textDocumentSyncOptions()
						.openClose(true)
						.willSave(true)
						.willSaveWaitUntil(true)
						.and()
					.hoverProvider(true)
					.renameProvider(true)
					.completionProvider()
						.resolveProvider(true)
						.triggerCharacters(Arrays.asList("a", "b"))
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, InitializeResult.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("InitializeResult1.json");
		to = mapper.readValue(expect, InitializeResult.class);
		assertObjects(from, to);
	}

	@Test
	public void testDiagnostic() throws Exception {
		Diagnostic from = new Diagnostic();
		String json = mapper.writeValueAsString(from);
		Diagnostic to = mapper.readValue(json, Diagnostic.class);
		assertObjects(from, to);

		from = Diagnostic.diagnostic()
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.severity(DiagnosticSeverity.Error)
				.code("code")
				.source("source")
				.message("message")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Diagnostic.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Diagnostic1.json");
		to = mapper.readValue(expect, Diagnostic.class);
		assertObjects(from, to);
	}

	@Test
	public void testPublishDiagnosticsParams() throws Exception {
		PublishDiagnosticsParams from = new PublishDiagnosticsParams();
		String json = mapper.writeValueAsString(from);
		PublishDiagnosticsParams to = mapper.readValue(json, PublishDiagnosticsParams.class);
		assertObjects(from, to);

		from = PublishDiagnosticsParams.publishDiagnosticsParams()
				.uri("uri")
				.diagnostic()
					.range()
						.start()
							.line(1)
							.character(1)
							.and()
						.end()
							.line(2)
							.character(2)
							.and()
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, PublishDiagnosticsParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("PublishDiagnosticsParams1.json");
		to = mapper.readValue(expect, PublishDiagnosticsParams.class);
		assertObjects(from, to);

		from = PublishDiagnosticsParams.publishDiagnosticsParams()
				.uri("uri")
				.diagnostic()
					.range()
						.start()
							.line(1)
							.character(1)
							.and()
						.end()
							.line(2)
							.character(2)
							.and()
						.and()
					.and()
				.diagnostic()
					.range()
						.start()
							.line(3)
							.character(3)
							.and()
						.end()
							.line(4)
							.character(4)
							.and()
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, PublishDiagnosticsParams.class);
		assertObjects(from, to);

		expect = loadResourceAsString("PublishDiagnosticsParams2.json");
		to = mapper.readValue(expect, PublishDiagnosticsParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testDynamicRegistration() throws Exception {
		DynamicRegistration from = new DynamicRegistration();
		String json = mapper.writeValueAsString(from);
		DynamicRegistration to = mapper.readValue(json, DynamicRegistration.class);
		assertObjects(from, to);

		from = DynamicRegistration.dynamicRegistration()
				.dynamicRegistration(true)
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, DynamicRegistration.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("DynamicRegistration1.json");
		to = mapper.readValue(expect, DynamicRegistration.class);
		assertObjects(from, to);
	}

	@Test
	public void testSynchronization() throws Exception {
		Synchronization from = new Synchronization();
		String json = mapper.writeValueAsString(from);
		Synchronization to = mapper.readValue(json, Synchronization.class);
		assertObjects(from, to);

		from = Synchronization.synchronization()
				.dynamicRegistration(true)
				.willSave(true)
				.willSaveWaitUntil(true)
				.didSave(true)
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Synchronization.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Synchronization1.json");
		to = mapper.readValue(expect, Synchronization.class);
		assertObjects(from, to);

		from = new Synchronization(true, true, true, false);
		to = new Synchronization(true, true, true, true);
		assertThat(from).isNotEqualTo(to);
		from = new Synchronization(true, true, true, false);
		to = new Synchronization(false, true, true, false);
		assertThat(from).isNotEqualTo(to);
	}

	@Test
	public void testTextDocumentClientCapabilities() throws Exception {
		TextDocumentClientCapabilities from = new TextDocumentClientCapabilities();
		String json = mapper.writeValueAsString(from);
		TextDocumentClientCapabilities to = mapper.readValue(json, TextDocumentClientCapabilities.class);
		assertObjects(from, to);

		from = TextDocumentClientCapabilities.textDocumentClientCapabilities()
				.synchronization()
					.dynamicRegistration(true)
					.willSave(true)
					.willSaveWaitUntil(true)
					.didSave(true)
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, TextDocumentClientCapabilities.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("TextDocumentClientCapabilities1.json");
		to = mapper.readValue(expect, TextDocumentClientCapabilities.class);
		assertObjects(from, to);
	}

	@Test
	public void testClientCapabilities() throws Exception {
		ClientCapabilities from = new ClientCapabilities();
		String json = mapper.writeValueAsString(from);
		ClientCapabilities to = mapper.readValue(json, ClientCapabilities.class);
		assertObjects(from, to);

		from = ClientCapabilities.clientCapabilities()
				.experimental("experimental")
				.textDocument()
					.synchronization()
						.dynamicRegistration(true)
						.willSave(true)
						.willSaveWaitUntil(true)
						.didSave(true)
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, ClientCapabilities.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("ClientCapabilities1.json");
		to = mapper.readValue(expect, ClientCapabilities.class);
		assertObjects(from, to);
	}

	@Test
	public void testMarkupContent() throws Exception {
		MarkupContent from = new MarkupContent();
		String json = mapper.writeValueAsString(from);
		MarkupContent to = mapper.readValue(json, MarkupContent.class);
		assertObjects(from, to);

		from = MarkupContent.markupContent()
				.kind(MarkupKind.markdown)
				.value("value")
				.build();

		json = mapper.writeValueAsString(from);
		String kind = JsonPath.read(json, "kind");
		assertThat(kind).isEqualTo("markdown");

		to = mapper.readValue(json, MarkupContent.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("MarkupContent1.json");
		to = mapper.readValue(expect, MarkupContent.class);
		assertObjects(from, to);
	}

	@Test
	public void testTextEdit() throws Exception {
		TextEdit from = new TextEdit();
		String json = mapper.writeValueAsString(from);
		TextEdit to = mapper.readValue(json, TextEdit.class);
		assertObjects(from, to);

		from = TextEdit.textEdit()
				.range()
					.start()
						.line(0)
						.character(0)
						.and()
					.end()
						.line(1)
						.character(1)
						.and()
					.and()
				.newText("newText")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, TextEdit.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("TextEdit1.json");
		to = mapper.readValue(expect, TextEdit.class);
		assertObjects(from, to);
	}

	@Test
	public void testCompletionItem() throws Exception {
		CompletionItem from = new CompletionItem();
		String json = mapper.writeValueAsString(from);
		CompletionItem to = mapper.readValue(json, CompletionItem.class);
		assertObjects(from, to);

		from = CompletionItem.completionItem()
				.label("label")
				.kind(CompletionItemKind.Class)
				.detail("detail")
				.documentation()
					.kind(MarkupKind.markdown)
					.value("value")
					.and()
				.sortText("sortText")
				.filterText("filterText")
				.insertText("insertText")
				.insertTextFormat(InsertTextFormat.PlainText)
				.textEdit()
					.range()
						.start()
							.line(0)
							.character(0)
							.and()
						.end()
							.line(1)
							.character(1)
							.and()
						.and()
					.newText("newText")
					.and()
				.command()
					.title("title")
					.command("command")
					.arguments(Arrays.asList("arg1", "arg2"))
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, CompletionItem.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("CompletionItem1.json");
		to = mapper.readValue(expect, CompletionItem.class);
		assertObjects(from, to);
	}

	@Test
	public void testCompletionList() throws Exception {
		CompletionList from = new CompletionList();
		String json = mapper.writeValueAsString(from);
		CompletionList to = mapper.readValue(json, CompletionList.class);
		assertObjects(from, to);

		from = CompletionList.completionList()
				.isIncomplete(true)
				.item()
					.label("label1")
					.and()
				.item()
					.label("label2")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, CompletionList.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("CompletionList1.json");
		to = mapper.readValue(expect, CompletionList.class);
		assertObjects(from, to);
	}

	@Test
	public void testMessageActionItem() throws Exception {
		MessageActionItem from = new MessageActionItem();
		String json = mapper.writeValueAsString(from);
		MessageActionItem to = mapper.readValue(json, MessageActionItem.class);
		assertObjects(from, to);

		from = MessageActionItem.messageActionItem()
				.title("title")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, MessageActionItem.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("MessageActionItem1.json");
		to = mapper.readValue(expect, MessageActionItem.class);
		assertObjects(from, to);
	}

	@Test
	public void testMessageParams() throws Exception {
		MessageParams from = new MessageParams();
		String json = mapper.writeValueAsString(from);
		MessageParams to = mapper.readValue(json, MessageParams.class);
		assertObjects(from, to);

		from = MessageParams.messageParams()
				.type(MessageType.Info)
				.message("message")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, MessageParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("MessageParams1.json");
		to = mapper.readValue(expect, MessageParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testShowMessageRequestParams() throws Exception {
		ShowMessageRequestParams from = new ShowMessageRequestParams();
		String json = mapper.writeValueAsString(from);
		ShowMessageRequestParams to = mapper.readValue(json, ShowMessageRequestParams.class);
		assertObjects(from, to);

		from = ShowMessageRequestParams.showMessageRequestParams()
				.action()
					.title("title")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, ShowMessageRequestParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("ShowMessageRequestParams1.json");
		to = mapper.readValue(expect, ShowMessageRequestParams.class);
		assertObjects(from, to);

		from = ShowMessageRequestParams.showMessageRequestParams()
				.action()
					.title("title1")
					.and()
				.action()
					.title("title2")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, ShowMessageRequestParams.class);
		assertObjects(from, to);

		expect = loadResourceAsString("ShowMessageRequestParams2.json");
		to = mapper.readValue(expect, ShowMessageRequestParams.class);
		assertObjects(from, to);

		from = ShowMessageRequestParams.showMessageRequestParams()
				.type(MessageType.Info)
				.message("message")
				.action()
					.title("title")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, ShowMessageRequestParams.class);
		assertObjects(from, to);

		expect = loadResourceAsString("ShowMessageRequestParams3.json");
		to = mapper.readValue(expect, ShowMessageRequestParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testRegistration() throws Exception {
		Registration from = new Registration();
		String json = mapper.writeValueAsString(from);
		Registration to = mapper.readValue(json, Registration.class);
		assertObjects(from, to);

		from = Registration.registration()
				.id("id")
				.method("method")
				.registerOptions("registerOptions")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Registration.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Registration1.json");
		to = mapper.readValue(expect, Registration.class);
		assertObjects(from, to);

		from = Registration.registration()
				.id("id")
				.method("method")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Registration.class);
		assertObjects(from, to);

		expect = loadResourceAsString("Registration2.json");
		to = mapper.readValue(expect, Registration.class);
		assertObjects(from, to);
	}

	@Test
	public void testRegistrationParams() throws Exception {
		RegistrationParams from = new RegistrationParams();
		String json = mapper.writeValueAsString(from);
		RegistrationParams to = mapper.readValue(json, RegistrationParams.class);
		assertObjects(from, to);

		from = RegistrationParams.registrationParams()
				.registration()
					.id("id")
					.method("method")
					.registerOptions("registerOptions")
					.and()
				.registration()
					.id("id")
					.method("method")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, RegistrationParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("RegistrationParams1.json");
		to = mapper.readValue(expect, RegistrationParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testUnregistration() throws Exception {
		Unregistration from = new Unregistration();
		String json = mapper.writeValueAsString(from);
		Unregistration to = mapper.readValue(json, Unregistration.class);
		assertObjects(from, to);

		from = Unregistration.unregistration()
				.id("id")
				.method("method")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Unregistration.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Unregistration1.json");
		to = mapper.readValue(expect, Unregistration.class);
		assertObjects(from, to);

		from = Unregistration.unregistration()
				.id("id")
				.method("method")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Unregistration.class);
		assertObjects(from, to);
	}

	@Test
	public void testHover() throws Exception {
		Hover from = new Hover();
		String json = mapper.writeValueAsString(from);
		Hover to = mapper.readValue(json, Hover.class);
		assertObjects(from, to);

		from = Hover.hover()
				.contents()
					.kind(MarkupKind.plaintext)
					.value("value")
					.and()
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Hover.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Hover1.json");
		to = mapper.readValue(expect, Hover.class);
		assertObjects(from, to);

		from = Hover.hover()
				.contents()
					.kind(MarkupKind.markdown)
					.value("value")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Hover.class);
		assertObjects(from, to);

		expect = loadResourceAsString("Hover2.json");
		to = mapper.readValue(expect, Hover.class);
		assertObjects(from, to);

		from = Hover.hover()
				.contents()
					.kind(MarkupKind.plaintext)
					.value("value")
					.and()
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.build();
		to = Hover.hover()
				.contents()
					.kind(MarkupKind.plaintext)
					.value("value")
					.and()
				.range(Range.from(1, 1, 2, 2))
				.build();
		assertObjects(from, to);
	}

	@Test
	public void testCommand() throws Exception {
		Command from = new Command();
		String json = mapper.writeValueAsString(from);
		Command to = mapper.readValue(json, Command.class);
		assertObjects(from, to);

		from = Command.command()
				.title("title")
				.command("command")
				.arguments(Arrays.asList("arg1", "arg2"))
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Command.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Command1.json");
		to = mapper.readValue(expect, Command.class);
		assertObjects(from, to);

		from = Command.command()
				.title("title")
				.command("command")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Command.class);
		assertObjects(from, to);

		expect = loadResourceAsString("Command2.json");
		to = mapper.readValue(expect, Command.class);
		assertObjects(from, to);

		from = Command.command()
				.title("title")
				.command("command")
				.argument("arg1")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Command.class);
		assertObjects(from, to);

		expect = loadResourceAsString("Command3.json");
		to = mapper.readValue(expect, Command.class);
		assertObjects(from, to);
	}

	@Test
	public void testLogMessageParams() throws Exception {
		LogMessageParams from = new LogMessageParams();
		String json = mapper.writeValueAsString(from);
		LogMessageParams to = mapper.readValue(json, LogMessageParams.class);
		assertObjects(from, to);

		from = LogMessageParams.logMessageParams()
				.type(MessageType.Error)
				.message("message")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, LogMessageParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("LogMessageParams1.json");
		to = mapper.readValue(expect, LogMessageParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testLocation() throws Exception {
		Location from = new Location();
		String json = mapper.writeValueAsString(from);
		Location to = mapper.readValue(json, Location.class);
		assertObjects(from, to);

		from = Location.location()
				.uri("uri")
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Location.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Location1.json");
		to = mapper.readValue(expect, Location.class);
		assertObjects(from, to);
	}

	@Test
	public void testDocumentSymbolParams() throws Exception {
		DocumentSymbolParams from = new DocumentSymbolParams();
		String json = mapper.writeValueAsString(from);
		DocumentSymbolParams to = mapper.readValue(json, DocumentSymbolParams.class);
		assertObjects(from, to);

		from = DocumentSymbolParams.documentSymbolParams()
				.textDocument()
					.uri("uri")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, DocumentSymbolParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("DocumentSymbolParams1.json");
		to = mapper.readValue(expect, DocumentSymbolParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testDocumentSymbol() throws Exception {
		DocumentSymbol from = new DocumentSymbol();
		String json = mapper.writeValueAsString(from);
		DocumentSymbol to = mapper.readValue(json, DocumentSymbol.class);
		assertObjects(from, to);

		from = DocumentSymbol.documentSymbol()
				.name("name")
				.detail("detail")
				.kind(SymbolKind.Array)
				.deprecated(true)
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.selectionRange()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.child()
					.name("name1")
					.and()
				.child()
					.name("name2")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, DocumentSymbol.class);
		assertObjects(from, to);

		from = DocumentSymbol.documentSymbol()
				.name("name")
				.detail("detail")
				.kind(SymbolKind.Array)
				.deprecated(true)
				.range()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.selectionRange()
					.start()
						.line(1)
						.character(1)
						.and()
					.end()
						.line(2)
						.character(2)
						.and()
					.and()
				.child(DocumentSymbol.documentSymbol().name("name1").build())
				.child(DocumentSymbol.documentSymbol().name("name2").build())
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, DocumentSymbol.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("DocumentSymbol1.json");
		to = mapper.readValue(expect, DocumentSymbol.class);
		assertObjects(from, to);
	}

	@Test
	public void testSymbolInformation() throws Exception {
		SymbolInformation from = new SymbolInformation();
		String json = mapper.writeValueAsString(from);
		SymbolInformation to = mapper.readValue(json, SymbolInformation.class);
		assertObjects(from, to);

		from = SymbolInformation.symbolInformation()
				.name("name")
				.kind(SymbolKind.Array)
				.deprecated(true)
				.location()
					.uri("uri")
					.range()
						.start()
							.line(1)
							.character(1)
							.and()
						.end()
							.line(2)
							.character(2)
							.and()
						.and()
					.and()
				.containerName("containerName")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, SymbolInformation.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("SymbolInformation1.json");
		to = mapper.readValue(expect, SymbolInformation.class);
		assertObjects(from, to);
	}

	@Test
	public void testRange() throws Exception {
		Range from = new Range();
		String json = mapper.writeValueAsString(from);
		Range to = mapper.readValue(json, Range.class);
		assertObjects(from, to);

		from = Range.range()
				.start()
					.line(1)
					.character(1)
					.and()
				.end()
					.line(2)
					.character(2)
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, Range.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("Range1.json");
		to = mapper.readValue(expect, Range.class);
		assertObjects(from, to);
	}

	@Test
	public void testRenameParams() throws Exception {
		RenameParams from = new RenameParams();
		String json = mapper.writeValueAsString(from);
		RenameParams to = mapper.readValue(json, RenameParams.class);
		assertObjects(from, to);

		from = RenameParams.renameParams()
				.textDocument()
					.uri("uri")
					.and()
				.position()
					.line(1)
					.character(1)
					.and()
				.newName("newName")
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, RenameParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("RenameParams1.json");
		to = mapper.readValue(expect, RenameParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testTextDocumentPositionParams() throws Exception {
		TextDocumentPositionParams from = new TextDocumentPositionParams();
		String json = mapper.writeValueAsString(from);
		TextDocumentPositionParams to = mapper.readValue(json, TextDocumentPositionParams.class);
		assertObjects(from, to);

		from = TextDocumentPositionParams.textDocumentPositionParams()
				.textDocument()
					.uri("uri")
					.and()
				.position()
					.line(1)
					.character(1)
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, TextDocumentPositionParams.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("TextDocumentPositionParams1.json");
		to = mapper.readValue(expect, TextDocumentPositionParams.class);
		assertObjects(from, to);
	}

	@Test
	public void testTextDocumentEdit() throws Exception {
		TextDocumentEdit from = new TextDocumentEdit();
		String json = mapper.writeValueAsString(from);
		TextDocumentEdit to = mapper.readValue(json, TextDocumentEdit.class);
		assertObjects(from, to);

		from = TextDocumentEdit.textDocumentEdit()
				.textDocument()
					.uri("uri")
					.version(1)
					.and()
				.edits()
					.range()
						.start()
							.line(0)
							.character(0)
							.and()
						.end()
							.line(1)
							.character(1)
							.and()
						.and()
					.newText("newText")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, TextDocumentEdit.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("TextDocumentEdit1.json");
		to = mapper.readValue(expect, TextDocumentEdit.class);
		assertObjects(from, to);
	}

	@Test
	public void testWorkspaceEdit() throws Exception {
		WorkspaceEdit from = new WorkspaceEdit();
		String json = mapper.writeValueAsString(from);
		WorkspaceEdit to = mapper.readValue(json, WorkspaceEdit.class);
		assertObjects(from, to);

		from = WorkspaceEdit.workspaceEdit()
				.changes("uri1")
					.range()
						.start()
							.line(0)
							.character(0)
							.and()
						.end()
							.line(1)
							.character(1)
							.and()
						.and()
					.newText("newText")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, WorkspaceEdit.class);
		assertObjects(from, to);

		from = WorkspaceEdit.workspaceEdit()
				.changes("uri1")
					.range(Range.from(0, 0, 1, 1))
					.newText("newText")
					.and()
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, WorkspaceEdit.class);
		assertObjects(from, to);

		String expect = loadResourceAsString("WorkspaceEdit1.json");
		to = mapper.readValue(expect, WorkspaceEdit.class);
		assertObjects(from, to);

		from = WorkspaceEdit.workspaceEdit()
				.build();

		expect = loadResourceAsString("WorkspaceEdit2.json");
		to = mapper.readValue(expect, WorkspaceEdit.class);
		assertObjects(from, to);

		List<TextEdit> edits = new ArrayList<>();
		from = WorkspaceEdit.workspaceEdit()
				.changes("uri1", edits)
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, WorkspaceEdit.class);
		assertObjects(from, to);

		edits = new ArrayList<>();
		edits.add(TextEdit.textEdit().newText("xxx").range(Range.from(1, 1, 1, 1)).build());
		from = WorkspaceEdit.workspaceEdit()
				.changes("uri1", edits)
				.build();

		json = mapper.writeValueAsString(from);
		to = mapper.readValue(json, WorkspaceEdit.class);
		assertObjects(from, to);
	}

	private static String loadResourceAsString(String resource) throws IOException {
		return loadResourceAsString(new ClassPathResource("org/springframework/dsl/lsp/server/domain/" + resource));
	}

	private static String loadResourceAsString(Resource resource) throws IOException {
		return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
	}

	private static void assertObjects(Object from, Object to) {
		assertThat(from).isNotSameAs(to);
		assertThat(from).isEqualTo(to);
		assertThat(from).isEqualToComparingFieldByField(to);
	}
}
