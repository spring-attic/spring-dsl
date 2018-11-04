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

import java.util.List;
import java.util.Set;

import org.springframework.dsl.symboltable.model.BaseScope;

/**
 * {@code Scope} is a dictionary of symbols that are grouped together by some
 * lexical construct in the input language. Examples include {@code structs},
 * {@code functions}, {@code code blocks}, {@code argument lists}, etc...
 * <p>
 * Scopes all have an enclosing scope that encloses them lexically. In other
 * words, am I wrapped in a class? a function? a code block?
 * <p>
 * This is distinguished from the parent scope. The parent scope is usually the
 * enclosing scope, but in the case of inheritance, it is the superclass rather
 * than the enclosing scope. Otherwise, the global scope would be considered the
 * parent scope of a class. When resolving symbols, we look up the parent scope
 * chain not the enclosing scope chain.
 * <p>
 * For convenience of code using this library, a bunch of methods have been
 * added one can use to get lots of useful information from a scope, but they
 * don't necessarily define what a scope is.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public interface Scope {

	/**
	 * Gets a scope name. Often scopes have names like function or class names. For
	 * unnamed scopes like code blocks, you can just return "local" or something.
	 *
	 * @return the scope name
	 */
	String getName();

	/**
	 * Gets an enclosing scope in which this scope defined. null if no enclosing
	 * scope.
	 *
	 * @return the enclosing scope
	 */
	Scope getEnclosingScope();

	/**
	 * Sets the enclosing scope. E.g., if this scope is a function, the enclosing
	 * scope could be a class. The {@link BaseScope} class automatically adds this
	 * to nested scope list of s.
	 *
	 * @param scope the new enclosing scope
	 */
	void setEnclosingScope(Scope scope);

	/**
	 * Define a symbol in this scope, throw IllegalArgumentException if sym already
	 * defined in this scope. This alters sym:
	 *
	 * 1. Set insertion order number of sym 2. Set sym's scope to be the scope.
	 *
	 * The order in which symbols are defined must be preserved so that
	 * {@link #getSymbols()} returns the list in definition order.
	 *
	 * @param symbol the symbol
	 */
	void define(Symbol symbol);

	/**
	 * Resolve a {@link Symbol} from this scope or recursively in parent scope if
	 * not here.
	 *
	 * @param name the symbol name
	 * @return the resolved symbol
	 */
	Symbol resolve(String name);

	/**
	 * Gets the symbol if defined within this specific scope.
	 *
	 * @param name the symbol name
	 * @return the symbol
	 */
	Symbol getSymbol(String name);

	/**
	 * Add a nested local scope to this scope; it's like define() but for non
	 * SymbolWithScope objects. E.g., a FunctionSymbol will add a LocalScope for its
	 * block via this method.
	 *
	 * @param scope the scope
	 */
	void nest(Scope scope);

	/**
	 * Return a list of scopes nested within this scope. It has both ScopedSymbols
	 * and scopes without symbols, such as LocalScopes. This returns a superset or
	 * same set as {@link #getNestedScopedSymbols}. ScopedSymbols come first then
	 * all non-ScopedSymbols Scope objects. Insertion order is used within each
	 * sublist.
	 *
	 * @return all nested scopes
	 */
	List<Scope> getNestedScopes();

	/**
	 * Return (inclusive) list of all scopes on path to root scope. The first
	 * element is the current scope and the last is the root scope.
	 *
	 * @return all scopes to root
	 */
	List<Scope> getEnclosingPathToRoot();

	/**
	 * Return all immediately enclosed scoped symbols in insertion order. E.g., a
	 * class would return all nested classes and any methods. There does not have to
	 * be an explicit pointer to the nested scopes. This method generally searches
	 * the list of symbols looking for symbols that implement Scope. Gets only those
	 * scopes that are in the symbols list of "this" scope. E.g., does not get local
	 * scopes within a function. This returns a subset or same set as
	 * {@link #getNestedScopes}.
	 *
	 * @return all nested scopes
	 */
	List<Scope> getNestedScopedSymbols();

	/**
	 * Return the symbols defined within this scope. The order of insertion into the
	 * scope is the order returned in this list.
	 *
	 * @return all symbols
	 */
	List<? extends Symbol> getSymbols();

	/**
	 * Return all symbols found in all nested scopes. The order of insertion into
	 * the scope is the order returned in this list for each scope. The scopes are
	 * traversed in the order in which they are encountered in the input.
	 *
	 * @return all known symbols
	 */
	List<? extends Symbol> getAllSymbols();

	/**
	 * Gets the set of names associated with all symbols in the scope.
	 *
	 * @return the symbol names
	 */
	Set<String> getSymbolNames();

	/**
	 * Number of symbols in this specific scope.
	 *
	 * @return the number of symbols
	 */
	int getNumberOfSymbols();

	/**
	 * Return scopes from to current with separator in between.
	 *
	 * @param separator the separator
	 * @return the qualifier string
	 */
	public String toQualifierString(String separator);

	/**
	 * Accepts a {@link SymbolTableVisitor}.
	 *
	 * @param visitor the visitor
	 */
	void accept(SymbolTableVisitor visitor);
}
