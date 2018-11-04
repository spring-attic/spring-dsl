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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dsl.symboltable.MemberSymbol;
import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;

/**
 * A {@link Symbol} representing a {@code class}. It is a kind of data aggregate
 * that has much in common with a {@code struct}.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public class ClassSymbol extends DataAggregateSymbol {

	/**
	 * If this is not set then symbol is just an {@code Object} and doesn't have a
	 * super class.
	 */
	protected String superClassName;

	/**
	 * next slot to allocate
	 */
	protected int nextFreeMethodSlot = 0;

	/**
	 * Instantiates a new class symbol.
	 *
	 * @param name the name
	 */
	public ClassSymbol(String name) {
		super(name);
	}

	@Override
	public Symbol resolve(String name) {
		Symbol s = resolveMember(name);
		if (s != null) {
			return s;
		}
		// if not a member, check any enclosing scope. it might be a global variable for
		// example
		Scope parent = getEnclosingScope();
		if (parent != null) {
			return parent.resolve(name);
		}
		// not found
		return null;
	}

	/**
	 * Look for a member with this name in this scope or any super class. Return
	 * null if no member found.
	 */
	@Override
	public Symbol resolveMember(String name) {
		Symbol s = symbols.get(name);
		if (s instanceof MemberSymbol) {
			return s;
		}
		// walk superclass chain
		List<ClassSymbol> superClassScopes = getSuperClassScopes();
		if (superClassScopes != null) {
			for (ClassSymbol sup : superClassScopes) {
				s = sup.resolveMember(name);
				if (s instanceof MemberSymbol) {
					return s;
				}
			}
		}
		return null;
	}

	@Override
	public void setSlotNumber(Symbol sym) {
		if (sym instanceof MethodSymbol) {
			MethodSymbol msym = (MethodSymbol) sym;
			// handle inheritance. If not found in this scope, check superclass
			// if any.
			ClassSymbol superClass = getSuperClassScope();
			if (superClass != null) {
				MethodSymbol superMethodSym = superClass.resolveMethod(sym.getName());
				if (superMethodSym != null) {
					msym.slot = superMethodSym.slot;
				}
			}
			if (msym.slot == -1) {
				msym.slot = nextFreeMethodSlot++;
			}
		} else {
			super.setSlotNumber(sym);
		}
	}

	@Override
	public List<? extends FieldSymbol> getFields() {
		List<FieldSymbol> fields = new ArrayList<>();
		ClassSymbol superClassScope = getSuperClassScope();
		if (superClassScope != null) {
			fields.addAll(superClassScope.getFields());
		}
		fields.addAll(getDefinedFields());
		return fields;
	}

	@Override
	public int getNumberOfFields() {
		int n = 0;
		ClassSymbol superClassScope = getSuperClassScope();
		if (superClassScope != null) {
			n += superClassScope.getNumberOfFields();
		}
		n += getNumberOfDefinedFields();
		return n;
	}

	/** Return the set of all methods defined within this class */
	public Set<MethodSymbol> getDefinedMethods() {
		Set<MethodSymbol> methods = new LinkedHashSet<>();
		for (MemberSymbol s : getSymbols()) {
			if (s instanceof MethodSymbol) {
				methods.add((MethodSymbol) s);
			}
		}
		return methods;
	}

	/** Return the set of all methods either inherited or not */
	public Set<MethodSymbol> getMethods() {
		Set<MethodSymbol> methods = new LinkedHashSet<>();
		ClassSymbol superClassScope = getSuperClassScope();
		if (superClassScope != null) {
			methods.addAll(superClassScope.getMethods());
		}
		methods.removeAll(getDefinedMethods()); // override method from superclass
		methods.addAll(getDefinedMethods());
		return methods;
	}

	/**
	 * Look for a method with this name in this scope or any super class. Return
	 * null if no method found.
	 */
	public MethodSymbol resolveMethod(String name) {
		Symbol s = resolveMember(name);
		if (s instanceof MethodSymbol) {
			return (MethodSymbol) s;
		}
		return null;
	}

	public void setSuperClass(String superClassName) {
		this.superClassName = superClassName;
		nextFreeMethodSlot = getNumberOfMethods();
	}

	public String getSuperClassName() {
		return superClassName;
	}

	/** get the number of methods defined specifically in this class */
	public int getNumberOfDefinedMethods() {
		int n = 0;
		for (MemberSymbol s : getSymbols()) {
			if (s instanceof MethodSymbol) {
				n++;
			}
		}
		return n;
	}

	/** get the total number of methods visible to this class */
	public int getNumberOfMethods() {
		int n = 0;
		ClassSymbol superClassScope = getSuperClassScope();
		if (superClassScope != null) {
			n += superClassScope.getNumberOfMethods();
		}
		n += getNumberOfDefinedMethods();
		return n;
	}

	/**
	 * Gets the {@link ClassSymbol} associated with superClassName or {@code null}
	 * if superclass is not resolved looking up the enclosing scope chain.
	 *
	 * @return the super class scope
	 */
	public ClassSymbol getSuperClassScope() {
		if (superClassName != null) {
			if (getEnclosingScope() != null) {
				Symbol superClass = getEnclosingScope().resolve(superClassName);
				if (superClass instanceof ClassSymbol) {
					return (ClassSymbol) superClass;
				}
			}
		}
		return null;
	}

	/** Multiple superclass or interface implementations and the like... */
	public List<ClassSymbol> getSuperClassScopes() {
		ClassSymbol superClassScope = getSuperClassScope();
		if (superClassScope != null) {
			return Collections.singletonList(superClassScope);
		}
		return null;
	}

	@Override
	public String toString() {
		return getName() + ":" + super.toString();
	}
}
