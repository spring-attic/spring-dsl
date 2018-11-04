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

import java.util.List;

import org.springframework.dsl.symboltable.Type;

/**
 * For C types like "void (*)(int)", we need that to be a pointer to a function
 * taking a single integer argument returning void.
 * 
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 * 
 */
public class FunctionType implements Type {

	protected final Type returnType;
	protected final List<Type> argumentTypes;

	public FunctionType(Type returnType, List<Type> argumentTypes) {
		this.returnType = returnType;
		this.argumentTypes = argumentTypes;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int getTypeIndex() {
		return -1;
	}

	public List<Type> getArgumentTypes() {
		return argumentTypes;
	}

	@Override
	public String toString() {
		return "*" + returnType;
	}
}
