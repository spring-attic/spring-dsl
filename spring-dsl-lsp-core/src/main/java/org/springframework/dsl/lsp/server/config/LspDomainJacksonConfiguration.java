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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.domain.DiagnosticSeverity;
import org.springframework.dsl.domain.MarkupKind;
import org.springframework.dsl.domain.MessageType;
import org.springframework.dsl.domain.ServerCapabilities;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.jackson.JsonRpcJackson2ObjectMapperBuilderCustomizer;
import org.springframework.dsl.jsonrpc.support.JsonRpcRequestJsonDeserializer;
import org.springframework.dsl.jsonrpc.support.JsonRpcResponseJsonDeserializer;
import org.springframework.dsl.lsp.server.domain.DiagnosticSeverityDeserializer;
import org.springframework.dsl.lsp.server.domain.DiagnosticSeveritySerializer;
import org.springframework.dsl.lsp.server.domain.MarkupKindDeserializer;
import org.springframework.dsl.lsp.server.domain.MarkupKindSerializer;
import org.springframework.dsl.lsp.server.domain.MessageTypeDeserializer;
import org.springframework.dsl.lsp.server.domain.MessageTypeSerializer;
import org.springframework.dsl.lsp.server.domain.ServerCapabilitiesJsonDeserializer;
import org.springframework.dsl.lsp.server.domain.ServerCapabilitiesJsonSerializer;

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
		builder.serializerByType(MarkupKind.class, new MarkupKindSerializer());
		builder.deserializerByType(MarkupKind.class, new MarkupKindDeserializer());
		builder.serializerByType(MessageType.class, new MessageTypeSerializer());
		builder.deserializerByType(MessageType.class, new MessageTypeDeserializer());
		builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
	};

	@Bean
	public JsonRpcJackson2ObjectMapperBuilderCustomizer lspJackson2ObjectMapperBuilderCustomizer() {
		return DEFAULT_CUSTOMIZERS;
	}
}
