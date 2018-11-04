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
package org.springframework.dsl.service.reconcile;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.PublishDiagnosticsParams;
import org.springframework.dsl.service.DslService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Strategy interface for reconciling a {@code Document}. This interface is more
 * aligned with {@code LSP} protocol as it returns
 * {@link PublishDiagnosticsParams}. While not required but most likely actual
 * implementation will use {@link Linter}.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 * @see Linter
 */
public interface Reconciler extends DslService {

	/**
	 * Reconcile a {@link Document} and return {@link Mono} for completion.
	 *
	 * @param document the document
	 * @return a {@link Mono} indicating reconcile operation completion
	 */
	Flux<PublishDiagnosticsParams> reconcile(Document document);
}
