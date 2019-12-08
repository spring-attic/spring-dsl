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
package org.springframework.dsl.lsp.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.domain.CompletionItemKind;
import org.springframework.dsl.domain.DiagnosticSeverity;
import org.springframework.dsl.domain.FoldingRangeKind;
import org.springframework.dsl.domain.MarkupKind;
import org.springframework.dsl.domain.MessageType;
import org.springframework.dsl.domain.ServerCapabilities;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.domain.WorkspaceEdit;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilderCustomizer;
import org.springframework.dsl.jsonrpc.support.JsonRpcRequestJsonDeserializer;
import org.springframework.dsl.jsonrpc.support.JsonRpcResponseJsonDeserializer;
import org.springframework.dsl.lsp.server.domain.CompletionItemKindDeserializer;
import org.springframework.dsl.lsp.server.domain.CompletionItemKindSerializer;
import org.springframework.dsl.lsp.server.domain.DiagnosticSeverityDeserializer;
import org.springframework.dsl.lsp.server.domain.DiagnosticSeveritySerializer;
import org.springframework.dsl.lsp.server.domain.FoldingRangeKindDeserializer;
import org.springframework.dsl.lsp.server.domain.FoldingRangeKindSerializer;
import org.springframework.dsl.lsp.server.domain.MarkupKindDeserializer;
import org.springframework.dsl.lsp.server.domain.MarkupKindSerializer;
import org.springframework.dsl.lsp.server.domain.MessageTypeDeserializer;
import org.springframework.dsl.lsp.server.domain.MessageTypeSerializer;
import org.springframework.dsl.lsp.server.domain.ServerCapabilitiesJsonDeserializer;
import org.springframework.dsl.lsp.server.domain.ServerCapabilitiesJsonSerializer;
import org.springframework.dsl.lsp.server.domain.SymbolKindDeserializer;
import org.springframework.dsl.lsp.server.domain.SymbolKindSerializer;
import org.springframework.dsl.lsp.server.domain.WorkspaceEditDeserializer;
import org.springframework.dsl.lsp.server.domain.WorkspaceEditSerializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Configuration for a specific LSP jackson related beans.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
public class LspDomainJacksonConfiguration {

	public static JsonRpcJackson2ObjectMapperBuilderCustomizer DEFAULT_CUSTOMIZERS = builder -> {
		builder.deserializerByType(JsonRpcRequest.class, new JsonRpcRequestJsonDeserializer());
		builder.deserializerByType(JsonRpcResponse.class, new JsonRpcResponseJsonDeserializer());
		builder.serializerByType(ServerCapabilities.class, new ServerCapabilitiesJsonSerializer());
		builder.deserializerByType(ServerCapabilities.class, new ServerCapabilitiesJsonDeserializer());
		builder.serializerByType(DiagnosticSeverity.class, new DiagnosticSeveritySerializer());
		builder.deserializerByType(DiagnosticSeverity.class, new DiagnosticSeverityDeserializer());
		builder.serializerByType(SymbolKind.class, new SymbolKindSerializer());
		builder.deserializerByType(SymbolKind.class, new SymbolKindDeserializer());
		builder.serializerByType(MarkupKind.class, new MarkupKindSerializer());
		builder.deserializerByType(MarkupKind.class, new MarkupKindDeserializer());
		builder.serializerByType(FoldingRangeKind.class, new FoldingRangeKindSerializer());
		builder.deserializerByType(FoldingRangeKind.class, new FoldingRangeKindDeserializer());
		builder.serializerByType(MessageType.class, new MessageTypeSerializer());
		builder.deserializerByType(MessageType.class, new MessageTypeDeserializer());
		builder.serializerByType(CompletionItemKind.class, new CompletionItemKindSerializer());
		builder.deserializerByType(CompletionItemKind.class, new CompletionItemKindDeserializer());
		builder.serializerByType(WorkspaceEdit.class, new WorkspaceEditSerializer());
		builder.deserializerByType(WorkspaceEdit.class, new WorkspaceEditDeserializer());
		builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
	};

	@Bean
	public JsonRpcJackson2ObjectMapperBuilderCustomizer lspJackson2ObjectMapperBuilderCustomizer() {
		return DEFAULT_CUSTOMIZERS;
	}
}
