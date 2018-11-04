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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Flux;

/**
 * Tests for {@link WordcheckLanguageCompletioner}.
 *
 * @author Janne Valkealahti
 * @author Kris De Volder
 *
 */
public class WordcheckLanguageCompletionerTests {

	@Test
	public void test() {
		assertCompletion("",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Position.from(0, 0));

		assertCompletion("x",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Collections.emptyList(),
				Position.from(0, 1));

		assertCompletion("j",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack"),
				Position.from(0, 1));

		assertCompletion("ja",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack"),
				Position.from(0, 2));

		assertCompletion("jac",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack"),
				Position.from(0, 3));

		assertCompletion("jack",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack"),
				Position.from(0, 4));

		assertCompletion("jack ",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Position.from(0, 5));

		assertCompletion("jack i",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("is"),
				Position.from(0, 6));

		assertCompletion("jack is a",
				Arrays.asList("jack", "is", "a", "dull", "boy"),
				Arrays.asList("jack", "a"),
				Position.from(0, 9));
	}

	private static void assertCompletion(String text, List<String> words, List<String> expected, Position position) {
		Document document = new TextDocument("", LanguageId.TXT, 0, text);
		WordcheckLanguageCompletioner completioner = buildCompletioner(words);
		Flux<CompletionItem> complete = completioner.complete(document, position);
		assertThat(complete).isNotNull();
		List<CompletionItem> items = complete.toStream().collect(Collectors.toList());
		assertThat(items).hasSize(expected.size());
		List<String> labels = items.stream().flatMap(item -> Stream.of(item.getLabel())).collect(Collectors.toList());
		assertThat(labels).containsExactlyInAnyOrderElementsOf(expected);
	}

	private static WordcheckLanguageCompletioner buildCompletioner(List<String> words) {
		WordcheckProperties properties = new WordcheckProperties();
		properties.setWords(words);
		WordcheckLanguageCompletioner completioner = new WordcheckLanguageCompletioner();
		completioner.setProperties(properties);
		return completioner;
	}
}
