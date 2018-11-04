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
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Flux;

/**
 * Tests for {@link SimpleLanguageCompletioner}.
 *
 * @author Janne Valkealahti
 *
 */
public class SimpleLanguageCompletionerTests {

	private final SimpleLanguageCompletioner completioner = new SimpleLanguageCompletioner();

	@Test
	public void testCompletions() {
		Document document = new TextDocument("", LanguageId.TXT, 0, SimpleLanguageTests.content1);

		Flux<CompletionItem> complete = completioner.complete(document, new Position(0, 1));
		assertThat(complete).isNotNull();
		List<CompletionItem> items = complete.toStream().collect(Collectors.toList());
		assertThat(items).hasSize(0);
	}

	@Test
	public void testEmptyShouldReturnAllKeyTokens() {
		Document document = new TextDocument("", LanguageId.TXT, 0, "");
		Flux<CompletionItem> complete = completioner.complete(document, Position.from(0, 0));
		assertThat(complete).isNotNull();
		List<CompletionItem> items = complete.toStream().collect(Collectors.toList());
		assertThat(items).hasSize(4);
		List<String> labels = items.stream().flatMap(item -> Stream.of(item.getLabel())).collect(Collectors.toList());
		assertThat(labels).containsExactlyInAnyOrder("string", "int", "long", "double");
	}

	@Test
	public void testPartialKey() {
		Document document = new TextDocument("", LanguageId.TXT, 0, "s");
		Flux<CompletionItem> complete = completioner.complete(document, Position.from(0, 1));
		assertThat(complete).isNotNull();
		List<CompletionItem> items = complete.toStream().collect(Collectors.toList());
		assertThat(items).hasSize(1);
		List<String> labels = items.stream().flatMap(item -> Stream.of(item.getLabel())).collect(Collectors.toList());
		assertThat(labels).containsExactlyInAnyOrder("string");
	}
}
