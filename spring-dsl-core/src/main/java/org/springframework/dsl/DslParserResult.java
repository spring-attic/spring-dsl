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
package org.springframework.dsl;

import java.util.List;

import org.springframework.dsl.service.reconcile.ReconcileProblem;

/**
 * Generic interface for a {@code parser} to return a result.
 *
 * @author Janne Valkealahti
 * @param <T> the type of a result
 */
public interface DslParserResult<T> {

	/**
	 * Get the parsed result.
	 *
	 * @return the parsed result
	 */
	T getResult();

	/**
	 * Convenience method checking if parsing resulted any errors.
	 *
	 * @return {@code true} if parsing resulted errors
	 */
	boolean hasErrors();

	/**
	 * Gets parsing errors.
	 *
	 * @return the parsing errors
	 */
	List<ReconcileProblem> getErrors();
}
