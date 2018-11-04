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
import org.springframework.dsl.symboltable.TypedSymbol;
import org.springframework.dsl.symboltable.support.Utils;

/**
 * This symbol represents a function ala C, not a method ala Java. You can
 * associate a node in the parse tree that is responsible for defining this
 * symbol.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public class FunctionSymbol extends SymbolWithScope implements TypedSymbol {

	protected Type retType;

	public FunctionSymbol(String name) {
		super(name);
	}

	@Override
	public Type getType() {
		return retType;
	}

	@Override
	public void setType(Type type) {
		retType = type;
	}

	/**
	 * Return the number of VariableSymbols specifically defined in the scope. This
	 * is useful as either the number of parameters or the number of parameters and
	 * locals depending on how you build the scope tree.
	 */
	public int getNumberOfVariables() {
		return Utils.filter(symbols.values(), s -> s instanceof VariableSymbol).size();
	}

	public int getNumberOfParameters() {
		return Utils.filter(symbols.values(), s -> s instanceof ParameterSymbol).size();
	}

	@Override
	public String toString() {
		return getName() + ":" + super.toString();
	}
}
