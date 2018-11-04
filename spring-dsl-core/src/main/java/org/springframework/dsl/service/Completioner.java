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
package org.springframework.dsl.service;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;

import reactor.core.publisher.Flux;

/**
 * Strategy interface defining a contract for providing completion from a
 * {@link Document} in a given {@link Position} and returning a {@link Flux} of
 * {@link CompletionItem}s.
 *
 * @author Janne Valkealahti
 *
 */
public interface Completioner extends DslService {

	/**
	 * Provide completion items for a document in a given position.
	 *
	 * @param document the document
	 * @param position the position
	 * @return the flux of completion items
	 */
	Flux<CompletionItem> complete(Document document, Position position);
}
