/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.service;

import org.springframework.dsl.domain.FoldingRange;

import reactor.core.publisher.Flux;

/**
 * Strategy interface providing {@link FoldingRange} info for current document.
 *
 * @author Janne Valkealahti
 *
 */
public interface Folderer extends DslService {

	/**
	 * Provide folding range information for a given document.
	 *
	 * @param context the context
	 * @return a folding range info
	 */
	Flux<FoldingRange> fold(DslContext context);
}
