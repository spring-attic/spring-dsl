/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.document.Region;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.domain.TextDocumentContentChangeEvent;
import org.springframework.dsl.model.LanguageId;

public class TextDocumentState {

	private static final Logger log = LoggerFactory.getLogger(TextDocumentState.class);
	private DocumentLineTracker lineTracker = new DefaultDocumentLineTracker();
	private DocumentText documentText;
	private LanguageId languageId;
	private String uri;
	private int version;

	/**
	 * Get a snapshot of a current document state as a {@link Document}.
	 *
	 * @return a snapshot as document
	 */
	public Document getDocument() {
		return new TextDocument(uri, languageId, version, documentText.toString());
	}

	public TextDocumentState(String content, String uri, LanguageId languageId) {
		setText(content);
		this.uri = uri;
		this.languageId = languageId;
	}

	public int toOffset(Position position) {
		Region region = lineTracker.getLineInformation(position.getLine());
		int lineStart = region.getOffset();
		return lineStart + position.getCharacter();
	}

	public DocumentText getText() {
		return documentText;
	}

	public String content() {
		return getText().toString();
	}

	public synchronized void setText(String content) {
		documentText = new DocumentText(content);
		lineTracker.set(documentText);
	}

	public synchronized void replace(int start, int len, String ins) {
		int end = start+len;
		documentText = documentText
			.delete(start, end)
			.insert(start, new DocumentText(ins));
		lineTracker.replace(start, len, new DocumentText(ins));
	}

	private void apply(TextDocumentContentChangeEvent change) {
		log.debug("Apply TextDocumentContentChangeEvent {}", change);
		log.trace("Old content before apply is '{}'", content());
		Range range = change.getRange();
		if (range == null) {
			//full sync mode
			setText(change.getText());
		} else {
			int start = toOffset(range.getStart());
			int end = toOffset(range.getEnd());
			replace(start, end-start, change.getText());
		}
		log.trace("New content after apply is '{}'", content());
	}

	public synchronized void apply(DidChangeTextDocumentParams params) {
		log.debug("Apply DidChangeTextDocumentParams {}", params);
		int newVersion = params.getTextDocument().getVersion();
		if (version < newVersion) {
			log.trace("Number of changes {}", params.getContentChanges().size());
			for (TextDocumentContentChangeEvent change : params.getContentChanges()) {
				apply(change);
			}
			this.version = newVersion;
		} else {
			log.warn("Change event with bad version ignored, current {} new {}: {}", version, newVersion, params);
		}
	}

}
