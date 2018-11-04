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
package org.springframework.dsl.document;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;

/**
 * Tests for {@link TextDocument}.
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentTests {

	public final static String DOC1 = "line1";
	public final static String DOC2 = "line1\n" + "line2\n";
	public final static String DOC3 = "line1\n" + "line2\n" + "line3\n";

	@Test
	public void testSimpleContent() {
		TextDocument textDocument = new TextDocument(DOC1);
		assertThat(textDocument.content()).isEqualTo(DOC1);
	}

	@Test
	public void testCaretPositions() {
		TextDocument textDocument = new TextDocument(DOC2);
		int caretPosition = textDocument.caret(new Position(1, 0));
		assertThat(caretPosition).isEqualTo(6);
	}

	@Test
	public void testPositions() {
		TextDocument textDocument = new TextDocument(DOC1);
		assertThat(textDocument.toPosition(0)).isEqualTo(Position.from(0, 0));
		assertThat(textDocument.toPosition(4)).isEqualTo(Position.from(0, 4));
		assertThat(textDocument.toPosition(5)).isEqualTo(Position.from(0, 5));

		textDocument = new TextDocument(DOC2);
		assertThat(textDocument.toPosition(6)).isEqualTo(Position.from(1, 0));
		assertThat(textDocument.toPosition(7)).isEqualTo(Position.from(1, 1));

		textDocument = new TextDocument(DOC3);
		assertThat(textDocument.toPosition(12)).isEqualTo(Position.from(2, 0));
	}

	@Test
	public void testRanges() {
		TextDocument textDocument = new TextDocument(DOC1);
		assertThat(textDocument.toRange(0, 0)).isEqualTo(Range.from(0, 0, 0, 0));
		assertThat(textDocument.toRange(0, 1)).isEqualTo(Range.from(0, 0, 0, 1));
		assertThat(textDocument.toRange(0, 5)).isEqualTo(Range.from(0, 0, 0, 5));
		textDocument = new TextDocument(DOC2);
		assertThat(textDocument.toRange(0, 6)).isEqualTo(Range.from(0, 0, 1, 0));
		assertThat(textDocument.toRange(0, 7)).isEqualTo(Range.from(0, 0, 1, 1));
		assertThat(textDocument.toRange(6, 0)).isEqualTo(Range.from(1, 0, 1, 0));
		assertThat(textDocument.toRange(6, 1)).isEqualTo(Range.from(1, 0, 1, 1));
		textDocument = new TextDocument(DOC3);
		assertThat(textDocument.toRange(0, 12)).isEqualTo(Range.from(0, 0, 2, 0));
		assertThat(textDocument.toRange(0, 13)).isEqualTo(Range.from(0, 0, 2, 1));
	}

	@Test
	public void testLineCount() {
		TextDocument textDocument = new TextDocument(DOC1);
		assertThat(textDocument.lineCount()).isEqualTo(1);
		textDocument = new TextDocument(DOC2);
		assertThat(textDocument.lineCount()).isEqualTo(3);
		textDocument = new TextDocument(DOC3);
		assertThat(textDocument.lineCount()).isEqualTo(4);
	}
}
