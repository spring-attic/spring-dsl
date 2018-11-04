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

import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.dsl.domain.Range;

public class DocumentRegionTests {

	private Document TDOC1 = new TextDocument(TextDocumentTests.DOC1);
	private Document TDOC2 = new TextDocument(TextDocumentTests.DOC2);
	private Document TDOC3 = new TextDocument(TextDocumentTests.DOC3);

	@Test
	public void test() {
		DocumentRegion documentRegion = new DocumentRegion(TDOC1, 0, 5);
		assertThat(documentRegion.toString()).isEqualTo(TextDocumentTests.DOC1);

		documentRegion = new DocumentRegion(TDOC1, Range.from(0, 0, 0, 5));
		assertThat(documentRegion.toString()).isEqualTo(TextDocumentTests.DOC1);

		documentRegion = new DocumentRegion(TDOC2, Range.from(1, 0, 1, 5));
		assertThat(documentRegion.toString()).isEqualTo("line2");

		documentRegion = new DocumentRegion(TDOC3, Range.from(2, 0, 2, 5));
		assertThat(documentRegion.toString()).isEqualTo("line3");
	}

	@Test
	public void testSplit() {
		DocumentRegion[] documentRegions = new DocumentRegion(TDOC3).split(Pattern.compile("[^\\w]+"));
		assertThat(documentRegions).hasSize(4);
		assertThat(documentRegions[0].toRange()).isEqualTo(Range.from(0, 0, 0, 5));
		assertThat(documentRegions[1].toRange()).isEqualTo(Range.from(1, 0, 1, 5));
		assertThat(documentRegions[2].toRange()).isEqualTo(Range.from(2, 0, 2, 5));
	}
}
