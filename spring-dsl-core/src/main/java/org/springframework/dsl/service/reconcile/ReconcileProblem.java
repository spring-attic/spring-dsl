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

import org.springframework.dsl.domain.Range;

/**
 * Minimal interface that objects representing a reconciler problem must
 * implement.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public interface ReconcileProblem {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	ProblemType getType();

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	String getMessage();

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	Range getRange();
}
