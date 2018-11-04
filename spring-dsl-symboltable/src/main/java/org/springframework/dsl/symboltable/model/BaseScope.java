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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableException;
import org.springframework.dsl.symboltable.SymbolTableVisitor;
import org.springframework.dsl.symboltable.support.Utils;

/**
 * An abstract base class that houses common functionality for scopes.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public abstract class BaseScope implements Scope {

	/**
	 * null if this scope is the root of the scope tree
	 */
	private Scope enclosingScope;

	/**
	 * All symbols defined in this scope; can include classes, functions, variables,
	 * or anything else that is a Symbol impl. It does NOT include non-Symbol-based
	 * things like LocalScope. See nestedScopes.
	 */
	protected final Map<String, Symbol> symbols = new LinkedHashMap<>();

	/**
	 * All directly contained scopes, typically LocalScopes within a LocalScope or a
	 * LocalScope within a FunctionSymbol. This does not include SymbolWithScope
	 * objects.
	 */
	protected final List<Scope> nestedScopesNotSymbols = new ArrayList<>();

	/**
	 * Instantiates a new base scope.
	 */
	public BaseScope() {
	}

	/**
	 * Instantiates a new base scope.
	 *
	 * @param enclosingScope the enclosing scope
	 */
	public BaseScope(Scope enclosingScope) {
		setEnclosingScope(enclosingScope);
	}

	@Override
	public void accept(SymbolTableVisitor visitor) {
		visitor.enterVisitScope(this);
		enterVisitSymbol(visitor);
		for (Scope s : getNestedScopes()) {
			if (!(s instanceof Symbol)) {
				s.accept(visitor);
			}
		}
		for (Symbol s : getSymbols()) {
			s.accept(visitor);
		}
		exitVisitSymbol(visitor);
		visitor.exitVisitScope(this);
	}

	protected void enterVisitSymbol(SymbolTableVisitor visitor) {}
	protected void exitVisitSymbol(SymbolTableVisitor visitor) {}

	@Override
	public Symbol getSymbol(String name) {
		return symbols.get(name);
	}

	@Override
	public void setEnclosingScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
	}

	@Override
	public List<Scope> getNestedScopedSymbols() {
		return getSymbols().stream()
				.filter(s -> s instanceof Scope)
				.map(s -> (Scope)s)
				.collect(Collectors.toList());
	}

	@Override
	public List<Scope> getNestedScopes() {
		ArrayList<Scope> all = new ArrayList<>();
		all.addAll(getNestedScopedSymbols());
		all.addAll(nestedScopesNotSymbols);
		return all;
	}

	/**
	 * Add a nested scope to this scope; could also be a FunctionSymbol if your
	 * language allows nested functions.
	 */
	@Override
	public void nest(Scope scope) {
		if (scope instanceof SymbolWithScope) {
			throw new SymbolTableException("Add SymbolWithScope instance " + scope.getName() + " via define()");
		}
		nestedScopesNotSymbols.add(scope);
	}

	@Override
	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if (s != null) {
			return s;
		}
		// if not here, check any enclosing scope
		Scope parent = getEnclosingScope();
		if (parent != null)
			return parent.resolve(name);
		return null; // not found
	}

	@Override
	public void define(Symbol sym) {
		if (symbols.containsKey(sym.getName())) {
			throw new SymbolTableException("duplicate symbol " + sym.getName());
		}
		sym.setScope(this);
		// set to insertion position from 0
		sym.setInsertionOrderNumber(symbols.size());
		symbols.put(sym.getName(), sym);
	}

	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}

	public Map<String, ? extends Symbol> getMembers() {
		return symbols;
	}

	public List<Scope> getAllNestedScopedSymbols() {
		List<Scope> scopes = new ArrayList<Scope>();
		Utils.getAllNestedScopedSymbols(this, scopes);
		return scopes;
	}

	/**
	 * Walk up enclosingScope until we find topmost. Note this is enclosing scope
	 * not necessarily parent. This will usually be a global scope or something,
	 * depending on your scope tree.
	 */
	public Scope getOuterMostEnclosingScope() {
		Scope s = this;
		while (s.getEnclosingScope() != null) {
			s = s.getEnclosingScope();
		}
		return s;
	}

	/**
	 * Walk up enclosingScope until we find an object of a specific type. E.g., if
	 * you want to get enclosing method, you would pass in MethodSymbol.class,
	 * unless of course you have created a subclass for your language
	 * implementation.
	 */
	public MethodSymbol getEnclosingScopeOfType(Class<?> type) {
		Scope s = this;
		while (s != null) {
			if (s.getClass() == type) {
				return (MethodSymbol) s;
			}
			s = s.getEnclosingScope();
		}
		return null;
	}

	@Override
	public List<Scope> getEnclosingPathToRoot() {
		List<Scope> scopes = new ArrayList<>();
		Scope s = this;
		while (s != null) {
			scopes.add(s);
			s = s.getEnclosingScope();
		}
		return scopes;
	}

	@Override
	public List<? extends Symbol> getSymbols() {
		Collection<Symbol> values = symbols.values();
		if (values instanceof List) {
			return (List<Symbol>) values;
		}
		return new ArrayList<>(values);
	}

	@Override
	public List<? extends Symbol> getAllSymbols() {
		List<Symbol> syms = new ArrayList<>();
		syms.addAll(getSymbols());
		for (Symbol s : symbols.values()) {
			if (s instanceof Scope) {
				Scope scope = (Scope) s;
				syms.addAll(scope.getAllSymbols());
			}
		}
		for (Scope s : getNestedScopes()) {
			if (!(s instanceof Symbol)) {
				syms.addAll(s.getAllSymbols());
			}
		}

		return syms;
	}

	@Override
	public int getNumberOfSymbols() {
		return symbols.size();
	}

	@Override
	public Set<String> getSymbolNames() {
		return symbols.keySet();
	}

	@Override
	public String toString() {
		return symbols.keySet().toString();
	}

	public String toScopeStackString(String separator) {
		return Utils.toScopeStackString(this, separator);
	}

	@Override
	public String toQualifierString(String separator) {
		return Utils.toQualifierString(this, separator);
	}

//	public String toTestString() {
//		return toTestString(", ", ".");
//	}
//
//	public String toTestString(String separator, String scopePathSeparator) {
//		List<? extends Symbol> allSymbols = this.getAllSymbols();
//		List<String> syms = Utils.map(allSymbols, s -> s.getScope().getName() + scopePathSeparator + s.getName());
//		return Utils.join(syms, separator);
//	}
}
