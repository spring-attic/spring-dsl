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
 * This interface tags user-defined symbols that have static type information,
 * like variables and functions.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public interface TypedSymbol {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	Type getType();

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	void setType(Type type);
}
