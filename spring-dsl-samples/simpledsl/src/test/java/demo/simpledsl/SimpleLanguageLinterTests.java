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
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

/**
 * Tests for {@code SimpleLanguageLinter}.
 *
 * @author Janne Valkealahti
 *
 */
public class SimpleLanguageLinterTests {

	private final SimpleLanguageLinter linter = new SimpleLanguageLinter();

	@Test
	public void testLints() {
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content1);
		List<ReconcileProblem> problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).isEmpty();

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content2);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).isEmpty();

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content3);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).isEmpty();

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content4);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).isEmpty();

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content5);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).hasSize(1);
		ReconcileProblem problem = problems.get(0);
		assertThat(problem.getRange().getStart().getLine()).isEqualTo(0);
		assertThat(problem.getRange().getStart().getCharacter()).isEqualTo(0);
		assertThat(problem.getRange().getEnd().getLine()).isEqualTo(0);
		assertThat(problem.getRange().getEnd().getCharacter()).isEqualTo(3);
	}

	@Test
	public void testUnknownKeyType() {
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content8);
		List<ReconcileProblem> problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).hasSize(1);
	}

	@Test
	public void testWrongValueType() {
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content7);
		List<ReconcileProblem> problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).hasSize(1);

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content9);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).hasSize(1);

		document = new TextDocument("fakeuri", LanguageId.TXT, 0, SimpleLanguageTests.content10);
		problems = linter.lint(document).toStream().collect(Collectors.toList());
		assertThat(problems).hasSize(1);
	}
}
