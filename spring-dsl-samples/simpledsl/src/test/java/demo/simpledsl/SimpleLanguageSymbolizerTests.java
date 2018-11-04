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
package demo.simpledsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Flux;

/**
 * Tests for {@code SimpleLanguageSymbolizer}.
 *
 * @author Janne Valkealahti
 *
 */
public class SimpleLanguageSymbolizerTests {

	private final SimpleLanguageSymbolizer symbolizer = new SimpleLanguageSymbolizer();

	@Test
	public void testSymbolize() {
		Document document = new TextDocument("", LanguageId.TXT, 0, SimpleLanguageTests.content1);

		Flux<DocumentSymbol> symbolize = symbolizer.symbolize(document);
		List<DocumentSymbol> symbols = symbolize.toStream().collect(Collectors.toList());
		assertThat(symbols).isNotNull();
		assertThat(symbols).hasSize(4);
		assertThat(symbols.get(0).getRange()).isEqualTo(Range.from(0, 0, 0, 3));
		assertThat(symbols.get(1).getRange()).isEqualTo(Range.from(1, 0, 1, 4));
		assertThat(symbols.get(2).getRange()).isEqualTo(Range.from(2, 0, 2, 6));
		assertThat(symbols.get(3).getRange()).isEqualTo(Range.from(3, 0, 3, 6));
	}
}
