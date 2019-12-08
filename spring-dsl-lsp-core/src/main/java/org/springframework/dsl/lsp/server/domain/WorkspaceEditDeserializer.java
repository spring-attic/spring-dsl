/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.lsp.server.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.dsl.domain.CreateFile;
import org.springframework.dsl.domain.DeleteFile;
import org.springframework.dsl.domain.RenameFile;
import org.springframework.dsl.domain.TextDocumentEdit;
import org.springframework.dsl.domain.TextEdit;
import org.springframework.dsl.domain.WorkspaceEdit;

public class WorkspaceEditDeserializer extends JsonDeserializer<WorkspaceEdit> {

	@Override
	public WorkspaceEdit deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		WorkspaceEdit object = new WorkspaceEdit();
		JsonNode node = p.getCodec().readTree(p);

		handleChanges(p, node, object);
		handleDocumentChanges(p, node, object);

		return object;
	}

	private static void handleChanges(JsonParser parser, JsonNode node, WorkspaceEdit object)
			throws IOException, JsonProcessingException {
		JsonNode changesNode = node.get("changes");
		if (changesNode != null) {
			TypeReference<Map<String, List<TextEdit>>> typeRef = new TypeReference<Map<String, List<TextEdit>>>() {
			};
			Map<String, List<TextEdit>> changes = changesNode.traverse(parser.getCodec()).readValueAs(typeRef);
			object.setChanges(changes);
		}
	}

	private static void handleDocumentChanges(JsonParser parser, JsonNode node, WorkspaceEdit object)
			throws IOException, JsonProcessingException {
		JsonNode documentChangesNode = node.get("documentChanges");
		if (documentChangesNode == null) {
			return;
		}

		if (documentChangesNode instanceof ArrayNode) {
			ArrayNode arrayNode = (ArrayNode) documentChangesNode;
			if (arrayNode.size() == 0) {
				return;
			}

			// checking fields from first node to determine which type is it
			JsonNode firstNode = arrayNode.get(0);
			if (firstNode.has("textDocument")) {
				TypeReference<List<TextDocumentEdit>> typeRef = new TypeReference<List<TextDocumentEdit>>() {
				};
				List<TextDocumentEdit> changes = documentChangesNode.traverse(parser.getCodec()).readValueAs(typeRef);
				object.setDocumentChangesTextDocumentEdits(changes);
			} else if (firstNode.has("kind")) {
				// need to see kind field to determine type
				String kind = firstNode.get("kind").asText();
				if ("create".equals(kind)) {
					TypeReference<List<CreateFile>> typeRef = new TypeReference<List<CreateFile>>() {
					};
					List<CreateFile> files = documentChangesNode.traverse(parser.getCodec()).readValueAs(typeRef);
					object.setDocumentChangesCreateFiles(files);
				} else if ("rename".equals(kind)) {
					TypeReference<List<RenameFile>> typeRef = new TypeReference<List<RenameFile>>() {
					};
					List<RenameFile> files = documentChangesNode.traverse(parser.getCodec()).readValueAs(typeRef);
					object.setDocumentChangesRenameFiles(files);
				} else if ("delete".equals(kind)) {
					TypeReference<List<DeleteFile>> typeRef = new TypeReference<List<DeleteFile>>() {
					};
					List<DeleteFile> files = documentChangesNode.traverse(parser.getCodec()).readValueAs(typeRef);
					object.setDocumentChangesDeleteFiles(files);
				}
			}
		}
	}
}
