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
 * Default implementation of a {@link ReconcileProblem}.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class DefaultReconcileProblem implements ReconcileProblem {

	private final ProblemType type;
	private final String message;
	private Range range;

	public DefaultReconcileProblem(String message, Range range) {
		this(null, message, range);
	}

	public DefaultReconcileProblem(ProblemType type, String message, Range range) {
		this.type = type;
		this.message = message;
		this.range = range;
	}

	@Override
	public ProblemType getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Range getRange() {
		return range;
	}
}
