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

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.DidCloseTextDocumentParams;
import org.springframework.dsl.domain.DidOpenTextDocumentParams;
import org.springframework.dsl.domain.DidSaveTextDocumentParams;
import org.springframework.dsl.domain.WillSaveTextDocumentParams;

import reactor.core.publisher.Mono;

/**
 * {@code DocumentStateTracker} is an interface providing methods what a
 * {@code tracker} is able to do and callback's for @{@code LSP} {@code didOpen},
 * {@code didChanged}, {@code didSave} and {@code didClose}
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public interface DocumentStateTracker {

	/**
	 * Return information if this tracker can support incremental changes. Returning
	 * {@code FALSE} means that this tracker don't know how to handle incremental
	 * changes meaning full {@code document} content need to be dispatched.
	 *
	 * @return if incremental changes are supported
	 */
	boolean isIncrementalChangesSupported();

	/**
	 * Handle {@code LSP didOpen} and return a {@link Mono} for completion.
	 *
	 * @param params the {@link DidOpenTextDocumentParams}
	 * @return the {@link Mono} for completion
	 */
	Mono<Document> didOpen(DidOpenTextDocumentParams params);

	/**
	 * Handle {@code LSP didChange} and return a {@link Mono} for completion.
	 *
	 * @param params the {@link DidChangeTextDocumentParams}
	 * @return the {@link Mono} for completion
	 */
	Mono<Document> didChange(DidChangeTextDocumentParams params);

	/**
	 * Handle {@code LSP willSave} and return a {@link Mono} for completion.
	 *
	 * @param params the {@link WillSaveTextDocumentParams}
	 * @return the {@link Mono} for completion
	 */
	Mono<Document> willSave(WillSaveTextDocumentParams params);

	/**
	 * Handle {@code LSP didSave} and return a {@link Mono} for completion.
	 *
	 * @param params the {@link DidSaveTextDocumentParams}
	 * @return the {@link Mono} for completion
	 */
	Mono<Document> didSave(DidSaveTextDocumentParams params);

	/**
	 * Handle {@code LSP didClose} and return a {@link Mono} for completion.
	 *
	 * @param params the {@link DidCloseTextDocumentParams}
	 * @return the {@link Mono} for completion
	 */
	Mono<Document> didClose(DidCloseTextDocumentParams params);

	/**
	 * Gets a {@link Document} matching given {@code uri}.
	 *
	 * @param uri the document uri
	 * @return the known document for uri
	 */
	Document getDocument(String uri);
}
