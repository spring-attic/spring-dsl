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

import java.util.function.Function;

import org.springframework.dsl.document.Document;

import reactor.core.publisher.Mono;

/**
 * Strategy interface parsing a {@link Document} and returning {@link Mono} for
 * a resulting {@link AntlrParseResult}.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public interface AntlrParseService<T> {

	/**
	 * Parse a {@link Document} and use given {@link Function} to get a {@link AntlrParseResult}.
	 *
	 * @param document the document
	 * @param function the function
	 * @return the mono of a antrl parse result
	 */
	Mono<AntlrParseResult<T>> parse(Document document,
			Function<Document, ? extends Mono<? extends AntlrParseResult<T>>> function);
}
