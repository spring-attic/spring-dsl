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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.dsl.antlr.support.DefaultAntlrParseService;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import reactor.core.publisher.Flux;

/**
 * Tests for {@link Test2AntlrLinter}.
 *
 * @author Janne Valkealahti
 *
 */
public class AntlrLinterTests {

	@Test
	public void testShouldNotHaveErrors() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "1.test2");
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrLinter linter = new Test2AntlrLinter(antlrParseService, antlrParseResultSupplier);
		Flux<ReconcileProblem> lint = linter.lint(document);
		List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
		assertThat(lints).isEmpty();
	}

	@Test
	public void testShouldHaveErrors() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "2.test2");
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrLinter linter = new Test2AntlrLinter(antlrParseService, antlrParseResultSupplier);

		Flux<ReconcileProblem> lint = linter.lint(document);
		List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
		assertThat(lints).hasSize(1);
		assertThat(lints.get(0).getMessage()).isEqualTo("missing '}' at 'state'");
		assertThat(lints.get(0).getRange()).isEqualTo(Range.from(3, 0, 3, 0));
	}

	@Test
	public void testShouldHaveErrors2() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "3.test2");
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrLinter linter = new Test2AntlrLinter(antlrParseService, antlrParseResultSupplier);

		Flux<ReconcileProblem> lint = linter.lint(document);
		List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
		assertThat(lints).hasSize(2);
		// TODO: it sucks, antlr don't give correct end position, find if we can fix it
		assertThat(lints.get(0).getRange()).isEqualTo(Range.from(10, 9, 10, 9));
		assertThat(lints.get(1).getRange()).isEqualTo(Range.from(14, 9, 14, 9));
	}

	@Test
	public void testShouldHaveErrors3() throws IOException {
		String input = TestResourceUtils.resourceAsString(getClass(), "5.test2");
		TextDocument document = new TextDocument("", LanguageId.TXT, 0, input);

		DefaultAntlrParseService<Object> antlrParseService = new DefaultAntlrParseService<>();
		Test2AntlrParseResultFunction antlrParseResultSupplier = new Test2AntlrParseResultFunction();

		Test2AntlrLinter linter = new Test2AntlrLinter(antlrParseService, antlrParseResultSupplier);

		Flux<ReconcileProblem> lint = linter.lint(document);
		List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
		assertThat(lints).hasSize(1);
		assertThat(lints.get(0).getRange()).isEqualTo(Range.from(0, 0, 0, 0));
		assertThat(lints.get(0).getMessage()).contains("extraneous input", "expecting");
	}
}
