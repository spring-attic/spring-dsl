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
import org.springframework.dsl.service.AbstractDslService;
import org.springframework.dsl.service.DslService;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * Base implementation of a {@link DslService} for {@code ANTRL} based language
 * services.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public abstract class AbstractAntlrDslService<T> extends AbstractDslService {

	private final AntlrParseService<T> antlrParseService;
	private final Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultFunction;

	/**
	 * Instantiates a new abstract antlr dsl service.
	 *
	 * @param languageId the language id
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultFunction the antlr parse result function
	 */
	public AbstractAntlrDslService(LanguageId languageId, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultFunction) {
		super(languageId);
		Assert.notNull(antlrParseService, "AntlrParseService cannot be null");
		Assert.notNull(antlrParseResultFunction, "Function cannot be null");
		this.antlrParseService = antlrParseService;
		this.antlrParseResultFunction = antlrParseResultFunction;
	}

	/**
	 * Instantiates a new abstract antlr dsl service.
	 *
	 * @param languageIds the language ids
	 * @param antlrParseService the antlr parse service
	 * @param antlrParseResultFunction the antlr parse result function
	 */
	public AbstractAntlrDslService(List<LanguageId> languageIds, AntlrParseService<T> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<T>>> antlrParseResultFunction) {
		super(languageIds);
		Assert.notNull(antlrParseService, "AntlrParseService cannot be null");
		Assert.notNull(antlrParseResultFunction, "Function cannot be null");
		this.antlrParseService = antlrParseService;
		this.antlrParseResultFunction = antlrParseResultFunction;
	}

	/**
	 * Gets the antlr parse service.
	 *
	 * @return the antlr parse service
	 */
	protected AntlrParseService<T> getAntlrParseService() {
		return antlrParseService;
	}

	/**
	 * Gets the antlr parse result function.
	 *
	 * @return the antlr parse result function
	 */
	protected Function<Document, Mono<? extends AntlrParseResult<T>>> getAntlrParseResultFunction() {
		return antlrParseResultFunction;
	}
}
