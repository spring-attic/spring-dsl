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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.dsl.antlr.support.DefaultAntlrParseService;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;

import reactor.core.publisher.Mono;

public class AntlrHovererTests {

	@Test
	public void test() {
		String input = "";
		assertTest2Hoverers(input, Position.from(0, 0), "State");
	}

	private static void assertTest2Hoverers(String input, Position position, String expect) {
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrHoverer hoverer = new Test2AntlrHoverer(antlrParseService, antlrParseResultSupplier);
		Mono<Hover> hover = hoverer.hover(document, new Position(0, 0));
		Hover h = hover.block();
		assertThat(h, notNullValue());
		assertThat(h.getRange(), notNullValue());
		assertThat(h.getContents().getValue(), containsString(expect));
	}
}
