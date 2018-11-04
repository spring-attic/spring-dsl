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
package demo.dotdsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import reactor.core.publisher.Flux;

/**
 * Tests for {@link DOTLanguageLinter}.
 *
 * @author Janne Valkealahti
 *
 */
public class DOTLanguageLinterTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(UserConfigurations.of(DOTLanguageConfiguration.class));

	@Test
	public void test() {
		this.contextRunner
			.run((context) -> {
				assertThat(context).hasSingleBean(DOTLanguageLinter.class);
			});

		this.contextRunner
			.run((context) -> {
				DOTLanguageLinter linter = context.getBean(DOTLanguageLinter.class);
				TextDocument document = new TextDocument("", DOTLanguageConfiguration.LANGUAGEID, 0, "graph xxx {}");
				Flux<ReconcileProblem> lint = linter.lint(document);
				List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
				assertThat(lints).isEmpty();
			});

		this.contextRunner
			.run((context) -> {
				DOTLanguageLinter linter = context.getBean(DOTLanguageLinter.class);
				TextDocument document = new TextDocument("", DOTLanguageConfiguration.LANGUAGEID, 0, "");
				Flux<ReconcileProblem> lint = linter.lint(document);
				List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
				assertThat(lints).hasSize(1);
				assertThat(lints.get(0).getMessage()).contains("expecting", "GRAPH", "DIGRAPH");
			});

		this.contextRunner
			.run((context) -> {
				DOTLanguageLinter linter = context.getBean(DOTLanguageLinter.class);
				TextDocument document = new TextDocument("", DOTLanguageConfiguration.LANGUAGEID, 0, "xxx");
				Flux<ReconcileProblem> lint = linter.lint(document);
				List<ReconcileProblem> lints = lint.toStream().collect(Collectors.toList());
				assertThat(lints).hasSize(1);
				assertThat(lints.get(0).getMessage()).contains("expecting", "GRAPH", "DIGRAPH");
			});
	}
}
