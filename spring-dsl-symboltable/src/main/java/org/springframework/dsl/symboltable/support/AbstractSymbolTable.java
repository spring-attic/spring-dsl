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
package org.springframework.dsl.symboltable.support;

import java.util.List;

import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTable;
import org.springframework.dsl.symboltable.SymbolTableVisitor;
import org.springframework.dsl.symboltable.model.BaseScope;
import org.springframework.dsl.symboltable.model.GlobalScope;
import org.springframework.dsl.symboltable.model.PredefinedScope;

/**
 * Base implementation of a {@link SymbolTable} meant to provide generic
 * functionality for language features. Most languages having a need for a
 * symbol table will need its own specific tweaks but there are always some
 * number of shared features.
 * <p>
 * This implementation allows to define concept of a {@code global} and
 * {@code predefined} symbols and resolving everything what {@link SymbolTable}
 * defines.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractSymbolTable implements SymbolTable {

	private final BaseScope prefefined = new PredefinedScope();
	private final GlobalScope globals = new GlobalScope(prefefined);

	@Override
	public List<? extends Symbol> getAllSymbols() {
		return globals.getAllSymbols();
	}

	/**
	 * Define a predefined symbol.
	 *
	 * @param symbol the symbol
	 */
	public void definePredefined(Symbol symbol) {
		prefefined.define(symbol);
	}

	/**
	 * Define a global symbol.
	 *
	 * @param symbol the symbol
	 */
	public void defineGlobal(Symbol symbol) {
		globals.define(symbol);
	}

	/**
	 * Gets the global scope.
	 *
	 * @return the global scope
	 */
	public Scope getGlobalScope() {
		return globals;
	}

	@Override
	public void visitSymbolTable(SymbolTableVisitor visitor) {
		getGlobalScope().accept(visitor);
	}
}
