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
package demo.wordcheckdsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.model.LanguageId;

/**
 * Tests for {@link WordcheckLanguageSymbolizer}.
 *
 * @author Janne Valkealahti
 *
 */
public class WordcheckLanguageSymbolizerTests {

	@Test
	public void test() {
		WordcheckLanguageSymbolizer symbolizer = new WordcheckLanguageSymbolizer();
		symbolizer.getProperties().setWords(Arrays.asList("jack", "is", "a", "dull", "boy"));
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, "jack is a dull boy");
		List<String> symbols = symbolizer.symbolize(document).toStream().map(s -> s.getName())
				.collect(Collectors.toList());
		assertThat(symbols).containsExactlyInAnyOrder("jack", "is", "a", "dull", "boy");
	}
}
