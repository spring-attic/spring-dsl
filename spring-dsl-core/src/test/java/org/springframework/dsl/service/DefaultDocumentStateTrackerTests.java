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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.DidOpenTextDocumentParams;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.domain.TextDocumentContentChangeEvent;
import org.springframework.dsl.domain.TextDocumentItem;
import org.springframework.dsl.domain.VersionedTextDocumentIdentifier;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.DefaultDocumentStateTracker;

public class DefaultDocumentStateTrackerTests {

	@Test
	public void testSimpleDocumentChangeFlow() {
		DefaultDocumentStateTracker tracker = new DefaultDocumentStateTracker();

		TextDocumentItem textDocumentItem = new TextDocumentItem("uri1", LanguageId.TXT.getIdentifier(), 0, "1");
		DidOpenTextDocumentParams didOpenparams = new DidOpenTextDocumentParams(textDocumentItem);

		tracker.didOpen(didOpenparams);
		assertThat(tracker.getDocument("uri1")).isNotNull();
		assertThat(tracker.getDocument("uri1").languageId()).isEqualTo(LanguageId.TXT);
		assertThat(tracker.getDocument("uri1").content().toString()).isEqualTo("1");

		DidChangeTextDocumentParams didChangeParams = new DidChangeTextDocumentParams();
		Position start = new Position(0, 1);
		Position end = new Position(0, 1);
		Range range = new Range(start, end);
		VersionedTextDocumentIdentifier identifier = new VersionedTextDocumentIdentifier(1);
		identifier.setUri("uri1");
		didChangeParams.setTextDocument(identifier);
		TextDocumentContentChangeEvent event = new TextDocumentContentChangeEvent(range, 0, "2");
		didChangeParams.setContentChanges(Arrays.asList(event));

		tracker.didChange(didChangeParams);
		assertThat(tracker.getDocument("uri1")).isNotNull();
		assertThat(tracker.getDocument("uri1").content().toString()).isEqualTo("12");

		start.setCharacter(2);
		end.setCharacter(2);
		identifier.setVersion(2);
		event.setText("3");

		tracker.didChange(didChangeParams);
		assertThat(tracker.getDocument("uri1")).isNotNull();
		assertThat(tracker.getDocument("uri1").content().toString()).isEqualTo("123");
		assertThat(tracker.getDocument("uri1").length()).isEqualTo(3);
		assertThat(tracker.getDocument("uri1").lineCount()).isEqualTo(1);

		didChangeParams = new DidChangeTextDocumentParams();
		start = new Position(0, 0);
		end = new Position(0, 3);
		range = new Range(start, end);
		identifier = new VersionedTextDocumentIdentifier(3);
		identifier.setUri("uri1");
		didChangeParams.setTextDocument(identifier);
		event = new TextDocumentContentChangeEvent(range, 3, "4\n5");
		didChangeParams.setContentChanges(Arrays.asList(event));

		tracker.didChange(didChangeParams);
		assertThat(tracker.getDocument("uri1")).isNotNull();
		assertThat(tracker.getDocument("uri1").content().toString()).isEqualTo("4\n5");
		assertThat(tracker.getDocument("uri1").lineCount()).isEqualTo(2);
	}
}
