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

import java.io.IOException;

import org.springframework.dsl.domain.CompletionOptions;
import org.springframework.dsl.domain.ServerCapabilities;
import org.springframework.dsl.domain.TextDocumentSyncKind;
import org.springframework.dsl.domain.TextDocumentSyncOptions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link JsonDeserializer} for {@link ServerCapabilities}.
 *
 * @author Janne Valkealahti
 *
 */
public class ServerCapabilitiesJsonDeserializer extends JsonDeserializer<ServerCapabilities> {

	@Override
	public ServerCapabilities deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ServerCapabilities object = new ServerCapabilities();
		JsonNode node = p.getCodec().readTree(p);

		JsonNode textDocumentSyncNode = node.get("textDocumentSync");
		if (textDocumentSyncNode != null) {
			if (textDocumentSyncNode.isObject()) {
				object.setTextDocumentSyncOptions(
						textDocumentSyncNode.traverse(p.getCodec()).readValueAs(TextDocumentSyncOptions.class));
			} else if (textDocumentSyncNode.isInt()) {
				object.setTextDocumentSyncKind(TextDocumentSyncKind.values()[textDocumentSyncNode.asInt()]);
			}
		}

		JsonNode hoverProviderNode = node.get("hoverProvider");
		if (hoverProviderNode != null && hoverProviderNode.isBoolean()) {
			object.setHoverProvider(hoverProviderNode.asBoolean());
		}

		JsonNode renameProviderNode = node.get("renameProvider");
		if (renameProviderNode != null && renameProviderNode.isBoolean()) {
			object.setRenameProvider(renameProviderNode.asBoolean());
		}

		JsonNode documentSymbolProviderNode = node.get("documentSymbolProvider");
		if (documentSymbolProviderNode != null && documentSymbolProviderNode.isBoolean()) {
			object.setDocumentSymbolProvider(documentSymbolProviderNode.asBoolean());
		}

		JsonNode completionProviderNode = node.get("completionProvider");
		if (completionProviderNode != null) {
			object.setCompletionProvider(
					completionProviderNode.traverse(p.getCodec()).readValueAs(CompletionOptions.class));
		}

		return object;
	}
}
