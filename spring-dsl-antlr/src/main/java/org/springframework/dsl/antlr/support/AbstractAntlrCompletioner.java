/*
 * Copyright 2018-2019 the original author or authors.
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
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.Completioner;
import org.springframework.dsl.service.DslContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base implementation of a {@link Completioner} for {@code ANTRL} based
 * language services.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public abstract class AbstractAntlrCompletioner<T> extends AbstractAntlrDslService<T> implements Completioner {

	/**
	 * Instantiates a new abstract antlr completioner.
	 *
	 * @param languageId the language id
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultSupplier the antlr parse result supplier
	 */
	public AbstractAntlrCompletioner(LanguageId languageId, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultSupplier) {
		super(languageId, antlrParseService, antlrParseResultSupplier);
	}

	/**
	 * Instantiates a new abstract antlr completioner.
	 *
	 * @param languageIds the language ids
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultSupplier the antlr parse result supplier
	 */
	public AbstractAntlrCompletioner(List<LanguageId> languageIds, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultSupplier) {
		super(languageIds, antlrParseService, antlrParseResultSupplier);
	}

	@Override
	public Flux<CompletionItem> complete(DslContext context, Position position) {
		return getAntlrParseService().parse(context.getDocument(), getAntlrParseResultFunction())
				.map(r -> r.getCompletionItems(position))
				// TODO: remove cache
				.flatMapMany(r -> r.cache());
	}
}
