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

import org.springframework.dsl.domain.ServerCapabilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link JsonSerializer} for {@link ServerCapabilities}.
 *
 * @author Janne Valkealahti
 *
 */
public class ServerCapabilitiesJsonSerializer extends JsonSerializer<ServerCapabilities> {

	@Override
	public void serialize(ServerCapabilities value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeStartObject();

		if (value.getTextDocumentSyncOptions() != null) {
			gen.writeObjectField("textDocumentSync", value.getTextDocumentSyncOptions());
		} else if (value.getTextDocumentSyncKind() != null) {
			gen.writeNumberField("textDocumentSync", value.getTextDocumentSyncKind().ordinal());
		}

		if (value.getHoverProvider() != null) {
			gen.writeBooleanField("hoverProvider", value.getHoverProvider());
		}

		if (value.getRenameProvider() != null) {
			gen.writeBooleanField("renameProvider", value.getRenameProvider());
		}

		if (value.getDocumentSymbolProvider() != null) {
			gen.writeBooleanField("documentSymbolProvider", value.getDocumentSymbolProvider());
		}

		if (value.getCompletionProvider() != null) {
			gen.writeObjectField("completionProvider", value.getCompletionProvider());
		}

		gen.writeEndObject();
	}
}
