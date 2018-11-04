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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dsl.symboltable.MemberSymbol;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableException;
import org.springframework.dsl.symboltable.Type;

/**
 * A symbol representing a collection of data like a struct or class. Each
 * member has a slot number indexed from 0 and we track data fields and methods
 * with different slot sequences. A DataAggregateSymbol can also be a member of
 * an aggregate itself (nested structs, ...).
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public abstract class DataAggregateSymbol extends SymbolWithScope implements MemberSymbol, Type {

	protected int nextFreeFieldSlot = 0; // next slot to allocate
	protected int typeIndex;

	/**
	 * Instantiates a new data aggregate symbol.
	 *
	 * @param name the name
	 */
	public DataAggregateSymbol(String name) {
		super(name);
	}

	@Override
	public void define(Symbol sym) {
		if (!(sym instanceof MemberSymbol)) {
			throw new SymbolTableException("sym is " + sym.getClass().getSimpleName() + " not MemberSymbol");
		}
		super.define(sym);
		setSlotNumber(sym);
	}

	@Override
	public List<MemberSymbol> getSymbols() {
		return super.getSymbols().stream()
				.filter(s -> s instanceof MemberSymbol)
				.map(s -> (MemberSymbol)s)
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, ? extends MemberSymbol> getMembers() {
		return super.getMembers().entrySet().stream()
			.filter(e -> e.getValue() instanceof MemberSymbol)
			.collect(Collectors.toMap(Map.Entry::getKey, e -> (MemberSymbol)e.getValue()));
	}

	/**
	 * Look up name within this scope only. Return any kind of MemberSymbol found or
	 * null if nothing with this name found as MemberSymbol.
	 */
	public Symbol resolveMember(String name) {
		Symbol s = symbols.get(name);
		if (s instanceof MemberSymbol) {
			return s;
		}
		return null;
	}

	/**
	 * Look for a field with this name in this scope only. Return {@code null} if no
	 * field found.
	 *
	 * @param name the field name
	 * @return the symbol
	 */
	public Symbol resolveField(String name) {
		Symbol s = resolveMember(name);
		if (s instanceof FieldSymbol) {
			return s;
		}
		return null;
	}

	/** get the number of fields defined specifically in this class */
	public int getNumberOfDefinedFields() {
		int n = 0;
		for (MemberSymbol s : getSymbols()) {
			if (s instanceof FieldSymbol) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Gets the total number of fields.
	 *
	 * @return the number of fields
	 */
	public int getNumberOfFields() {
		return getNumberOfDefinedFields();
	}

	/** Return the list of fields in this specific aggregate */
	public List<? extends FieldSymbol> getDefinedFields() {
		List<FieldSymbol> fields = new ArrayList<>();
		for (MemberSymbol s : getSymbols()) {
			if (s instanceof FieldSymbol) {
				fields.add((FieldSymbol) s);
			}
		}
		return fields;
	}

	public List<? extends FieldSymbol> getFields() {
		return getDefinedFields();
	}

	public void setSlotNumber(Symbol sym) {
		if (sym instanceof FieldSymbol) {
			FieldSymbol fsym = (FieldSymbol) sym;
			fsym.setSlotNumber(nextFreeFieldSlot++);
		}
	}

	@Override
	public int getSlotNumber() {
		return -1; // class definitions do not yield either field or method slots; they are just
					// nested
	}

	@Override
	public int getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}
}
