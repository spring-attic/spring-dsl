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
import org.springframework.dsl.symboltable.Type;
import org.springframework.dsl.symboltable.support.Utils;
import org.springframework.util.Assert;

/**
 * An abstract base class used to house common functionality. You can associate
 * a node in the parse tree that is responsible for defining this symbol.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public abstract class BaseSymbol implements Symbol {

	private final String name;
	private final ArrayList<Modifier> modifiers = new ArrayList<>();
	private Type type; // If language statically typed, record type
	private Scope scope; // All symbols know what scope contains them.
	private int lexicalOrder; // order seen or insertion order from 0; compilers often need this
	private Range range;

	/**
	 * Instantiates a new base symbol.
	 *
	 * @param name the name
	 */
	public BaseSymbol(String name) {
		Assert.notNull(name, "Symbol name must be set");
		this.name = name;
	}

	@Override
	public void accept(SymbolTableVisitor visitor) {
		visitor.enterVisitSymbol(this);
		visitor.exitVisitSymbol(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
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
	 * Adds the modifier.
	 *
	 * @param modifier the modifier
	 */
	public void addModifier(Modifier modifier) {
		this.modifiers.add(modifier);
	}

	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	public List<Modifier> getModifiers() {
		return modifiers;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	@Override
	public int getInsertionOrderNumber() {
		return lexicalOrder;
	}

	@Override
	public void setInsertionOrderNumber(int i) {
		this.lexicalOrder = i;
	}

	public String getFullyQualifiedName(String scopePathSeparator) {
		List<Scope> path = scope.getEnclosingPathToRoot();
		Collections.reverse(path);
		String qualifier = Utils.joinScopeNames(path, scopePathSeparator);
		return qualifier + scopePathSeparator + name;
	}

	@Override
	public String toString() {
		String s = "";
		if (scope != null) {
			s = scope.getName() + ".";
		}
		if (type != null) {
			String ts = type.toString();
			if (type instanceof SymbolWithScope) {
				ts = ((SymbolWithScope) type).getFullyQualifiedName(".");
			}
			return '<' + s + getName() + ":" + ts + '>';
		}
		return s + getName();
	}
}
