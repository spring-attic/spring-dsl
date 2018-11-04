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
package org.springframework.dsl.antlr.support;

import java.util.List;
import java.util.function.Function;

import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.antlr.AntlrParseService;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.reconcile.Linter;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base implementation of a {@link Linter} for {@code ANTRL} based
 * language services.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public abstract class AbstractAntlrLinter<T> extends AbstractAntlrDslService<T> implements Linter {

	/**
	 * Instantiates a new abstract antlr linter.
	 *
	 * @param languageId the language id
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultSupplier the antlr parse result supplier
	 */
	public AbstractAntlrLinter(LanguageId languageId, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultSupplier) {
		super(languageId, antlrParseService, antlrParseResultSupplier);
	}

	/**
	 * Instantiates a new abstract antlr linter.
	 *
	 * @param languageIds the language ids
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultSupplier the antlr parse result supplier
	 */
	public AbstractAntlrLinter(List<LanguageId> languageIds, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultSupplier) {
		super(languageIds, antlrParseService, antlrParseResultSupplier);
	}

	@Override
	public Flux<ReconcileProblem> lint(Document document) {
		return getAntlrParseService().parse(document, getAntlrParseResultFunction())
			.map(r -> r.getReconcileProblems())
			.flatMapMany(r -> r.cache());
	}
}
