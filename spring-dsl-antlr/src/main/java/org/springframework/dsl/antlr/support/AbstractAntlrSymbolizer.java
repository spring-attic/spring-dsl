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
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.symbol.Symbolizer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base implementation of a {@link Symbolizer} for {@code ANTRL} based
 * language services.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public abstract class AbstractAntlrSymbolizer<T> extends AbstractAntlrDslService<T> implements Symbolizer {

	/**
	 * Instantiates a new abstract antlr symbolizer.
	 *
	 * @param languageId the language id
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultFunction the antlr parse result function
	 */
	public AbstractAntlrSymbolizer(LanguageId languageId, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultFunction) {
		super(languageId, antlrParseService, antlrParseResultFunction);
	}

	/**
	 * Instantiates a new abstract antlr symbolizer.
	 *
	 * @param languageIds the language ids
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultFunction the antlr parse result function
	 */
	public AbstractAntlrSymbolizer(List<LanguageId> languageIds, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultFunction) {
		super(languageIds, antlrParseService, antlrParseResultFunction);
	}

	@Override
	public Flux<DocumentSymbol> symbolize(Document document) {
		return getAntlrParseService().parse(document, getAntlrParseResultFunction())
				.map(r -> r.getDocumentSymbols())
				.flatMapMany(r -> r.cache());
	}
}
