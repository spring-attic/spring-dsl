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

public class ProblemTypes {

	/**
	 * Creates a new problem type. The newly created problem type is not 'equals' to any other
	 * problem type.
	 *
	 * @param typeName A unique name for this problem type. Note that it is the caller's responsibility that the typeName is unique.
	 *                 If this method is called more than once with identical typeName's it makes no attempts to veify that
	 *                 the name is uniquer, or to return the same object for the same typeName.
	 * @param defaultSeverity the default severity
	 * @return A newly create problem type.
	 */
	public static ProblemType create(String typeName, ProblemSeverity defaultSeverity) {
		return new ProblemType() {
			@Override
			public String toString() {
				return typeName;
			}
			@Override
			public ProblemSeverity getSeverity() {
				return defaultSeverity;
			}
			@Override
			public String getCode() {
				return typeName;
			}
		};
	}

}
