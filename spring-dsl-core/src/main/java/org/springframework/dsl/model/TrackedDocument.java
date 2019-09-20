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
package org.springframework.dsl.model;

import org.springframework.dsl.document.TextDocumentState;

public class TrackedDocument {

	private final TextDocumentState state;
	private int openCount = 0;

	public TrackedDocument(TextDocumentState state) {
		this.state = state;
	}

	public TextDocumentState getDocument() {
		return state;
	}

	public TrackedDocument open() {
		openCount++;
		return this;
	}

	public boolean close() {
		openCount--;
		return openCount <= 0;
	}

	public int getOpenCount() {
		return openCount;
	}
}
