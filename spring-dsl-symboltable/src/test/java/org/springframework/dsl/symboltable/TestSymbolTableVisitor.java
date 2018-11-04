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

import java.util.ArrayList;

class TestSymbolTableVisitor implements SymbolTableVisitor {

	public ArrayList<Scope> scopesEnter = new ArrayList<>();
	public ArrayList<Symbol> symbolsEnter = new ArrayList<>();
	public ArrayList<Scope> scopesExit = new ArrayList<>();
	public ArrayList<Symbol> symbolsExit = new ArrayList<>();
	public ArrayList<Object> all = new ArrayList<>();

	@Override
	public void enterVisitScope(Scope scope) {
		this.scopesEnter.add(scope);
		this.all.add(scope);
	}

	@Override
	public void enterVisitSymbol(Symbol symbol) {
		this.symbolsEnter.add(symbol);
		this.all.add(symbol);
	}

	@Override
	public void exitVisitScope(Scope scope) {
		this.scopesExit.add(scope);
		this.all.add(scope);
	}

	@Override
	public void exitVisitSymbol(Symbol symbol) {
		this.symbolsExit.add(symbol);
		this.all.add(symbol);
	}
}
