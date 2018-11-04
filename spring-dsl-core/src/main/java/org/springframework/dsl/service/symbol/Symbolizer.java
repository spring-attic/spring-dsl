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
package org.springframework.dsl.service.symbol;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.service.DslService;

import reactor.core.publisher.Flux;

/**
 * Strategy interface providing {@link DocumentSymbol} info for a document.
 *
 * @author Janne Valkealahti
 *
 */
public interface Symbolizer extends DslService {

	/**
	 * Provide symbol information for a given document.
	 *
	 * @param document the document
	 * @return a {@link Flux} of {@link DocumentSymbol}
	 */
	Flux<DocumentSymbol> symbolize(Document document);
}
