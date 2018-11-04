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

import org.springframework.dsl.domain.Range;

/**
 * A generic programming language symbol. A symbol has to have a name and a
 * scope in which it lives. It also helps to know the order in which symbols are
 * added to a scope because this often translates to register or parameter
 * numbers.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public interface Symbol {

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the scope.
	 *
	 * @return the scope
	 */
	Scope getScope();

	/**
	 * Sets the scope. This is not the enclosing scope.
	 *
	 * @param scope the new scope
	 */
	void setScope(Scope scope);

	/**
	 * Gets the insertion order number.
	 *
	 * @return the insertion order number
	 */
	int getInsertionOrderNumber();

	/**
	 * Sets the insertion order number.
	 *
	 * @param order the new insertion order number
	 */
	void setInsertionOrderNumber(int order);

	/**
	 * Gets the range of this symbol in a representing input language.
	 *
	 * @return the range
	 */
	Range getRange();

	/**
	 * Accepts a {@link SymbolTableVisitor}.
	 *
	 * @param visitor the visitor
	 */
	void accept(SymbolTableVisitor visitor);
}
