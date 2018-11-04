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

import java.util.regex.Pattern;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.DocumentRegion;
import org.springframework.dsl.service.reconcile.DefaultReconcileProblem;
import org.springframework.dsl.service.reconcile.Linter;
import org.springframework.dsl.service.reconcile.ProblemSeverity;
import org.springframework.dsl.service.reconcile.ProblemType;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import reactor.core.publisher.Flux;

/**
 * A {@link Linter} for a {@code wordcheck} language.
 *
 * @author Janne Valkealahti
 * @author Kris De Volder
 * @see EnableWordcheckLanguage
 *
 */
//tag::snippet1[]
public class WordcheckLanguageLinter extends WordcheckLanguageSupport implements Linter {

	private static final Pattern SPACE = Pattern.compile("[^\\w]+");

	@Override
	public Flux<ReconcileProblem> lint(Document document) {
		return Flux.defer(() -> Flux.fromArray(new DocumentRegion(document).split(SPACE)))
				.filter(w -> w.length() > 0)
				.filter(w -> !getProperties().getWords().contains(w.toString()))
				.map(this::problem);
	}

	private ReconcileProblem problem(DocumentRegion region) {
		return new DefaultReconcileProblem(PROBLEM, "Bad word '" + region.toString() + "'", region.toRange());
	}

	private static ProblemType PROBLEM = new ProblemType() {

		@Override
		public ProblemSeverity getSeverity() {
			return ProblemSeverity.ERROR;
		}

		@Override
		public String getCode() {
			return "badword";
		}
	};
}
//end::snippet1[]
