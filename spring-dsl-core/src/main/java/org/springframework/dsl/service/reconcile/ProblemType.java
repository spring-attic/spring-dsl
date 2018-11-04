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

/**
 * {@code ProblemType} represents type of a problem in a
 * {@link ReconcileProblem}. {@code ProblemType} usually have its severity and
 * possible {@code code} associated with it.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public interface ProblemType {

	/**
	 * Gets the severity.
	 *
	 * @return the severity
	 */
	ProblemSeverity getSeverity();

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	String getCode();
}
