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
package org.springframework.dsl.domain;

/**
 * {@code LSP} domain object for a specification {@code TextDocumentSyncKind}.
 * <p>
 * Order of enum fields is important as those directly map to
 * {@code textDocumentSync} as number field in {@link ServerCapabilities} if
 * this enum is used.
 *
 * @author Janne Valkealahti
 * @see ServerCapabilities
 *
 */
public enum TextDocumentSyncKind {

	/**
	 * Documents should not be synced at all.
	 */
	None,

	/**
	 * Documents are synced by always sending the full content of the document.
	 */
	Full,

	/**
	 * Documents are synced by sending the full content on open. After that only
	 * incremental updates to the document are send.
	 */
	Incremental;
}
