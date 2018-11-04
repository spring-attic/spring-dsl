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
package org.springframework.dsl.symboltable;

/**
 * This interface is a tag that indicates the implementing object is a kind of
 * type. Every type knows its name. In languages like C where we need a
 * tree-like structure to represent a type, one could return a string
 * representation as the name.
 * <p>
 * The types are typically things like {@code struct} or {@code classes} and
 * {@code primitive types}, as well as the type trees used for languages like C.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public interface Type {

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * It is useful during type computation and code gen to assign an int index to
	 * the primitive types and possibly user-defined types like structs and classes.
	 *
	 * @return Return 0-indexed type index or -1 if no index.
	 */
	public int getTypeIndex();
}
