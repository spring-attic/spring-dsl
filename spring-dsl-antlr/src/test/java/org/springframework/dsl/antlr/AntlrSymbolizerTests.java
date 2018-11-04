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
import org.springframework.dsl.antlr.support.DefaultAntlrParseService;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Flux;

public class AntlrSymbolizerTests {

	@Test
	public void testTest2Empty() {
		String input = "";
		assertTest2Symbols(input, Arrays.asList("org.springframework.statemachine.state.State",
				"org.springframework.statemachine.transition.Transition"));
	}

	@Test
	public void testTest2Simple() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "1.test2");
		assertTest2Symbols(input, Arrays.asList("org.springframework.statemachine.state.State",
				"org.springframework.statemachine.transition.Transition", "S1"));
	}

	private static void assertTest2Symbols(String input, List<String> expect) {
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultFunction = new Test2AntlrParseResultFunction();

		Test2AntlrSymbolizer symbolizer = new Test2AntlrSymbolizer(antlrParseService, antlrParseResultFunction);
		Flux<DocumentSymbol> symbolizes = symbolizer.symbolize(document);
		List<DocumentSymbol> items = symbolizes.toStream().collect(Collectors.toList());
		List<String> names = items.stream().map(item -> item.getName()).collect(Collectors.toList());
		assertThat(names, containsInAnyOrder(expect.toArray(new String[0])));
	}
}
