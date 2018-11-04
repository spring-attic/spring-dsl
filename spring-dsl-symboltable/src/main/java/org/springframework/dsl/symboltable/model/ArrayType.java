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
 * An element within a type type such is used in C or Java where we need to
 * indicate the type is an array of some element type like float[] or User[]. It
 * also tracks the size as some types indicate the size of the array.
 * 
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 * 
 */
public class ArrayType implements Type {

	protected final Type elemType;
	protected final int numElems; // some languages allow you to point at arrays of a specific size

	public ArrayType(Type elemType) {
		this.elemType = elemType;
		this.numElems = -1;
	}

	public ArrayType(Type elemType, int numElems) {
		this.elemType = elemType;
		this.numElems = numElems;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int getTypeIndex() {
		return -1;
	}

	@Override
	public String toString() {
		return elemType + "[]";
	}
}
