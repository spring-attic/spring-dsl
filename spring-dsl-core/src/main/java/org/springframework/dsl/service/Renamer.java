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
package org.springframework.dsl.service;

import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.WorkspaceEdit;

import reactor.core.publisher.Mono;

/**
 * Strategy interface providing {@link WorkspaceEdit} info for current position
 * with a new name in a document.
 *
 * @author Janne Valkealahti
 *
 */
public interface Renamer extends DslService {

	/**
	 * Provide {@link WorkspaceEdit} for a given {@link Position} with a {@code newName}.
	 *
	 * @param context the dsl context
	 * @param position the position in a document
	 * @param newName the new name
	 * @return a {@link Mono} completing as {@link WorkspaceEdit}
	 */
	Mono<WorkspaceEdit> rename(DslContext context, Position position, String newName);
}
