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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.dsl.domain.WorkspaceEdit;

public class WorkspaceEditSerializer extends JsonSerializer<WorkspaceEdit> {

	@Override
	public void serialize(WorkspaceEdit value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		if (value.getChanges() != null) {
			gen.writeObjectField("changes", value.getChanges());
		}

		if (value.getDocumentChangesTextDocumentEdits() != null) {
			gen.writeObjectField("documentChanges", value.getDocumentChangesTextDocumentEdits());
		} else if (value.getDocumentChangesCreateFiles() != null) {
			gen.writeObjectField("documentChanges", value.getDocumentChangesCreateFiles());
		} else if (value.getDocumentChangesRenameFiles() != null) {
			gen.writeObjectField("documentChanges", value.getDocumentChangesRenameFiles());
		} else if (value.getDocumentChangesDeleteFiles() != null) {
			gen.writeObjectField("documentChanges", value.getDocumentChangesDeleteFiles());
		}

		gen.writeEndObject();
	}
}
