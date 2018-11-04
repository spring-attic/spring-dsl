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
import java.util.Collections;
import java.util.List;

import org.springframework.dsl.domain.Range;
import org.springframework.dsl.symboltable.Modifier;
import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableVisitor;
import org.springframework.dsl.symboltable.support.Utils;
import org.springframework.util.Assert;

/**
 * An abstract base class that houses common functionality for symbols like
 * classes and functions that are both symbols and scopes. There is some common
 * cut and paste functionality with {@link BaseSymbol} because of a lack of
 * multiple inheritance in Java but it is minimal.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public abstract class SymbolWithScope extends BaseScope implements Symbol, Scope {

	private final String name;
	private final ArrayList<Modifier> modifiers = new ArrayList<>();
	private int index;
	private Range range;

	/**
	 * Instantiates a new symbol with scope.
	 *
	 * @param name the name
	 */
	public SymbolWithScope(String name) {
		Assert.notNull(name, "Symbol name must be set");
		this.name = name;
	}

	@Override
	protected void enterVisitSymbol(SymbolTableVisitor visitor) {
		visitor.enterVisitSymbol(this);
	}

	@Override
	protected void exitVisitSymbol(SymbolTableVisitor visitor) {
		visitor.exitVisitSymbol(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Scope getScope() {
		return getEnclosingScope();
	}

	@Override
	public void setScope(Scope scope) {
		setEnclosingScope(scope);
	}

	@Override
	public Range getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(Range range) {
		this.range = range;
	}

	/**
	 * Return the name prefixed with the name of its enclosing scope using '.' (dot)
	 * as the scope separator.
	 */
	public String getQualifiedName() {
		return getEnclosingScope().getName() + "." + name;
	}

	/** Return the name prefixed with the name of its enclosing scope. */
	public String getQualifiedName(String scopePathSeparator) {
		return getEnclosingScope().getName() + scopePathSeparator + name;
	}

	/**
	 * Return the fully qualified name includes all scopes from the root down to
	 * this particular symbol.
	 */
	public String getFullyQualifiedName(String scopePathSeparator) {
		List<Scope> path = getEnclosingPathToRoot();
		Collections.reverse(path);
		return Utils.joinScopeNames(path, scopePathSeparator);
	}

	public void addModifier(Modifier modifier) {
		this.modifiers.add(modifier);
	}

	public List<Modifier> getModifiers() {
		return modifiers;
	}

	@Override
	public int getInsertionOrderNumber() {
		return index;
	}

	@Override
	public void setInsertionOrderNumber(int i) {
		this.index = i;
	}

	@Override
	public int getNumberOfSymbols() {
		return symbols.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Symbol)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		return name.equals(((Symbol) obj).getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
