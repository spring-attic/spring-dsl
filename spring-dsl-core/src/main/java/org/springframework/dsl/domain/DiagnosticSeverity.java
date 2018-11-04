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
package org.springframework.dsl.domain;

/**
 * Enumeration of a {@code LSP} {@code DiagnosticSeverity} mapping to protocol
 * integer fields.
 *
 * @author Janne Valkealahti
 *
 */
public enum DiagnosticSeverity {

	Error(1),

	Warning(2),

	Information(3),

	Hint(4);

	private final int value;

	DiagnosticSeverity(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static DiagnosticSeverity forValue(int value) {
		DiagnosticSeverity[] allValues = DiagnosticSeverity.values();
		if (value < 1 || value > allValues.length)
			throw new IllegalArgumentException("Illegal enum value: " + value);
		return allValues[value - 1];
	}
}
