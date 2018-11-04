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

import org.springframework.dsl.domain.DiagnosticSeverity;

/**
 * Enumeration for a problem severities. Mostly just mapping to {@code LSP}'s
 * {@link DiagnosticSeverity} but having extra {@code IGNORE} type.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public enum ProblemSeverity {

	/**
	 * Problem is ignored.
	 */
	IGNORE,

	/**
	 * Problem on info level.
	 */
	INFO,

	/**
	 * Problem on warning level.
	 */
	WARNING,

	/**
	 * Problem on hint level.
	 */
	HINT,

	/**
	 * Problem on error level.
	 */
	ERROR;
}
