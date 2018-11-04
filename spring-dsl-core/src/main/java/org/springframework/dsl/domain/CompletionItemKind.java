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

public enum CompletionItemKind {

	Text(1),

	Method(2),

	Function(3),

	Constructor(4),

	Field(5),

	Variable(6),

	Class(7),

	Interface(8),

	Module(9),

	Property(10),

	Unit(11),

	Value(12),

	Enum(13),

	Keyword(14),

	Snippet(15),

	Color(16),

	File(17),

	Reference(18),

	Folder(19),

	EnumMember(20),

	Constant(21),

	Struct(22),

	Event(23),

	Operator(24),

	TypeParameter(25);

	private final int value;

	CompletionItemKind(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
