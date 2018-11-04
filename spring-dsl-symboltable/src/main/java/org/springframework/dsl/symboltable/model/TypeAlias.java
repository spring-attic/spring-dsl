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
package org.springframework.dsl.symboltable.model;

import org.springframework.dsl.symboltable.Type;

/**
 * A "typedef int I;" in C results in a TypeAlias("I", ptrToIntegerType).
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public class TypeAlias extends BaseSymbol implements Type {

	private Type targetType;

	/**
	 * Instantiates a new type alias.
	 *
	 * @param name the name
	 * @param targetType the target type
	 */
	public TypeAlias(String name, Type targetType) {
		super(name);
		this.targetType = targetType;
	}

	@Override
	public int getTypeIndex() {
		return -1;
	}

	/**
	 * Gets the target type.
	 *
	 * @return the target type
	 */
	public Type getTargetType() {
		return targetType;
	}
}
