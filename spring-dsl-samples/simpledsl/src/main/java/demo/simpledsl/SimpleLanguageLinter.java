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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.service.reconcile.DefaultReconcileProblem;
import org.springframework.dsl.service.reconcile.Linter;
import org.springframework.dsl.service.reconcile.ProblemSeverity;
import org.springframework.dsl.service.reconcile.ProblemType;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import demo.simpledsl.SimpleLanguage.Line;
import demo.simpledsl.SimpleLanguage.Token;
import reactor.core.publisher.Flux;

/**
 * A {@link Linter} for a {@code simple} language.
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class SimpleLanguageLinter extends SimpleLanguageDslService implements Linter {

	@Override
	public Flux<ReconcileProblem> lint(Document document) {
		return Flux.defer(() -> {
			return Flux.fromIterable(lintProblems(document));
		});
	}

	private List<ReconcileProblem> lintProblems(Document document) {
		List<ReconcileProblem> problems = new ArrayList<>();
		SimpleLanguage simpleLanguage = SimpleLanguage.build(document);
		List<Line> lines = simpleLanguage.getLines();

		for (Line line : lines) {
			if (line.getKeyToken() != null && line.getKeyToken().getType() == null) {
				problems.add(
						new DefaultReconcileProblem(PROBLEM, "Unknown key type", line.getKeyToken().getRange()));
			} else if (line.getValueToken() == null) {
				problems.add(
						new DefaultReconcileProblem(PROBLEM, "Missing value", line.getKeyToken().getRange()));
			} else if (line.getKeyToken() != null && line.getKeyToken().getType() != null && line.getValueToken() != null) {
				Token token = line.getValueToken();
				if (token != null) {
					switch (line.getKeyToken().getType()) {
					case INT:
						try {
							Integer.parseInt(line.getValueToken().getValue());
						} catch (NumberFormatException e) {
							problems.add(
									new DefaultReconcileProblem(PROBLEM, "Not int type", line.getKeyToken().getRange()));
						}
						break;
					case DOUBLE:
						try {
							Double.parseDouble(line.getValueToken().getValue());
						} catch (NumberFormatException e) {
							problems.add(
									new DefaultReconcileProblem(PROBLEM, "Not double type", line.getKeyToken().getRange()));
						}
						break;
					case LONG:
						try {
							Long.parseLong(line.getValueToken().getValue());
						} catch (NumberFormatException e) {
							problems.add(
									new DefaultReconcileProblem(PROBLEM, "Not long type", line.getKeyToken().getRange()));
						}
						break;
					default:
						break;
					}
				}
			}
		}
		return problems;
	}

	private static ProblemType PROBLEM = new ProblemType() {

		@Override
		public ProblemSeverity getSeverity() {
			return ProblemSeverity.ERROR;
		}

		@Override
		public String getCode() {
			return "code";
		}
	};
}
//end::snippet1[]
