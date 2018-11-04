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
package org.springframework.dsl.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.document.BadLocationException;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.DidCloseTextDocumentParams;
import org.springframework.dsl.domain.DidOpenTextDocumentParams;
import org.springframework.dsl.domain.DidSaveTextDocumentParams;
import org.springframework.dsl.domain.TextDocumentItem;
import org.springframework.dsl.domain.VersionedTextDocumentIdentifier;
import org.springframework.dsl.domain.WillSaveTextDocumentParams;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.model.TrackedDocument;

import reactor.core.publisher.Mono;

/**
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class DefaultDocumentStateTracker implements DocumentStateTracker {

	private static final Logger log = LoggerFactory.getLogger(DefaultDocumentStateTracker.class);
	private Map<String, TrackedDocument> documents = new HashMap<>();

	@Override
	public Document getDocument(String uri) {
		TrackedDocument trackedDocument = documents.get(uri);
		return trackedDocument != null ? trackedDocument.getDocument() : null;
	}

	@Override
	public boolean isIncrementalChangesSupported() {
		return true;
	}

	@Override
	public Mono<Document> didOpen(DidOpenTextDocumentParams params) {
		TextDocumentItem textDocument = params.getTextDocument();

		String uri = textDocument.getUri();
		LanguageId languageId = LanguageId.languageId(textDocument.getLanguageId());
		int version = textDocument.getVersion();
		String text = textDocument.getText();

		TrackedDocument trackedDocument = createDocument(uri, languageId, version, text).open();
		return Mono.just(trackedDocument.getDocument());
	}

	@Override
	public final Mono<Document> didChange(DidChangeTextDocumentParams params) {
		VersionedTextDocumentIdentifier identifier = params.getTextDocument();
		String url = identifier.getUri();
		if (url != null) {
			TrackedDocument trackedDocument = documents.get(url);

			try {
				TextDocument doc = trackedDocument.getDocument();
				doc.apply(params);
				return Mono.just(doc);

			} catch (BadLocationException e) {
				log.error("", e);
			}
		}

		return Mono.empty();
	}

	@Override
	public Mono<Document> didSave(DidSaveTextDocumentParams params) {
		return Mono.empty();
	}

	@Override
	public Mono<Document> didClose(DidCloseTextDocumentParams params) {
		log.debug("didClose {}", params);
//		TextDocumentIdentifier identifier = params.getTextDocument();
//		String url = identifier.getUri();
//		if (url != null) {
//			TrackedDocument trackedDocument = documents.get(url);
//		}

		return Mono.empty();
	}

	@Override
	public Mono<Document> willSave(WillSaveTextDocumentParams params) {
		log.debug("willSave {}", params);
		return Mono.empty();
	}

//	private synchronized TextDocument getOrCreateDocument(String url) {
//		TrackedDocument doc = documents.get(url);
//		if (doc==null) {
//			log.warn("Trying to get document ["+url+"] but it did not exists. Creating it with language-id 'plaintext'");
//			doc = createDocument(url, LanguageId.PLAINTEXT, 0, "");
//		}
//		return doc.getDocument();
//	}

	private synchronized TrackedDocument createDocument(String url, LanguageId languageId, int version, String text) {
		TrackedDocument trackedDocument = documents.get(url);
		if (trackedDocument != null) {
			log.warn("Creating document [{}] but it already exists. Reusing existing!", url);
			return trackedDocument;
		}
		trackedDocument = new TrackedDocument(new TextDocument(url, languageId, version, text));
		documents.put(url, trackedDocument);
		return trackedDocument;
	}
}
