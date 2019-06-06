/*
 * Copyright 2018-2019 the original author or authors.
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

import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.Position;

import reactor.core.publisher.Mono;

/**
 * Strategy interface providing {@link Hover} info for current position in a
 * document.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public interface Hoverer extends DslService {

	/**
	 * Provide hover for a given position in a document.
	 *
	 * @param context the dsl context
	 * @param position the position in a document
	 * @return a {@link Mono} completing as {@link Hover}
	 */
	Mono<Hover> hover(DslContext context, Position position);
}
