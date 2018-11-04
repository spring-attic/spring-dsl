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
package org.springframework.dsl.antlr;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.dsl.antlr.support.AbstractAntlrCompletioner;
import org.springframework.dsl.antlr.support.DefaultAntlrParseService;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Flux;

/**
 * These tests are pretty much to verify functionality in
 * {@link AbstractAntlrCompletioner} using existing test {@code ANTLR} test
 * grammars.
 *
 * @author Janne Valkealahti
 *
 */
public class AntlrCompletionerTests {

	@Test
	public void testTest2Empty() {
		String input = "";
		assertTest2Completions(input, Position.from(0, 0), Arrays.asList("STATEMACHINE", "STATE", "TRANSITION"));
	}

	@Test
	public void testCompletesReferenceIds() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "2.test2");
		assertTest2Completions(input, Position.from(9, 8), Arrays.asList("S1", "S2", "S3"));
	}

	private static void assertTest2Completions(String input, Position position, List<String> expect) {
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrCompletioner completioner = new Test2AntlrCompletioner(antlrParseService, antlrParseResultSupplier);
		Flux<CompletionItem> completions = completioner.complete(document, new Position(9, 8));
		List<CompletionItem> items = completions.toStream().collect(Collectors.toList());
		List<String> labels = items.stream().map(item -> item.getLabel()).collect(Collectors.toList());
		assertThat(labels, containsInAnyOrder(expect.toArray(new String[0])));
	}
}
