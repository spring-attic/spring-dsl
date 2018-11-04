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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.dsl.symboltable.model.ClassSymbol;
import org.springframework.dsl.symboltable.model.FieldSymbol;
import org.springframework.dsl.symboltable.model.NamedModifier;
import org.springframework.dsl.symboltable.model.PrimitiveType;
import org.springframework.dsl.symboltable.model.VisibilityModifier;
import org.springframework.dsl.symboltable.support.AbstractSymbolTable;
import org.springframework.dsl.symboltable.support.DefaultSymbolTable;

public class SymbolTableTests {

	@Test
	public void testClassWithFields() {
		//	private static class TestClass1 {
		//		public int variable1;
		//		protected Long variable2;
		//		private String variable3;
		//	}
		Modifier privateModifier = new VisibilityModifier("private");
		Modifier protectedModifier = new VisibilityModifier("protected");
		Modifier publicModifier = new VisibilityModifier("public");

		ClassSymbol cSymbol = new ClassSymbol("TestClass1");
		cSymbol.addModifier(privateModifier);

		FieldSymbol fSymbol1 = new FieldSymbol("variable1");
		fSymbol1.setType(new PrimitiveType("int"));
		fSymbol1.addModifier(privateModifier);

		FieldSymbol fSymbol2 = new FieldSymbol("variable2");
		fSymbol2.setType(new ClassSymbol("Long"));
		fSymbol2.addModifier(protectedModifier);

		FieldSymbol fSymbol3 = new FieldSymbol("variable3");
		fSymbol3.setType(new ClassSymbol("String"));
		fSymbol3.addModifier(publicModifier);

		cSymbol.define(fSymbol1);
		cSymbol.define(fSymbol2);
		cSymbol.define(fSymbol3);

		assertThat(fSymbol1.getScope()).isSameAs(cSymbol);
		assertThat(fSymbol1.getType().getName()).isEqualTo("int");
		assertThat(fSymbol1.getType()).isInstanceOf(PrimitiveType.class);
		assertThat(fSymbol1.getModifiers()).hasSize(1);

		assertThat(fSymbol2.getScope()).isSameAs(cSymbol);
		assertThat(fSymbol2.getType().getName()).isEqualTo("Long");
		assertThat(fSymbol2.getType()).isInstanceOf(ClassSymbol.class);
		assertThat(fSymbol2.getModifiers()).hasSize(1);

		assertThat(fSymbol3.getScope()).isSameAs(cSymbol);
		assertThat(fSymbol3.getType().getName()).isEqualTo("String");
		assertThat(fSymbol3.getType()).isInstanceOf(ClassSymbol.class);
		assertThat(fSymbol3.getModifiers()).hasSize(1);

		assertThat(cSymbol.getAllSymbols()).hasSize(3);
		assertThat(cSymbol.getNumberOfSymbols()).isEqualTo(3);
		assertThat(cSymbol.getNumberOfFields()).isEqualTo(3);
		assertThat(cSymbol.getSymbolNames()).hasSize(3);
		assertThat(cSymbol.getModifiers()).hasSize(1);

		assertThat(cSymbol.getSymbol("variable2")).isNotNull();
		assertThat(cSymbol.getSymbol("variable2").getScope()).isSameAs(cSymbol);
		assertThat(cSymbol.getSymbol("variable2").getClass()).isEqualTo(FieldSymbol.class);
	}

	@Test
	public void testStateConcepts() {
		StatemachineSymbolTable table = new StatemachineSymbolTable();

		ClassSymbol state1Symbol = new ClassSymbol("S1");
		state1Symbol.addModifier(new NamedModifier("initial"));

		table.defineState(state1Symbol);

		ClassSymbol state2Symbol = new ClassSymbol("S2");
		state2Symbol.addModifier(new NamedModifier("end"));

		table.defineState(state2Symbol);

		ClassSymbol transition1Symbol = new ClassSymbol("T1");

		FieldSymbol sourceVariable = new FieldSymbol("S1");
		sourceVariable.setType(StatemachineSymbolTable.SOURCE);
		transition1Symbol.define(sourceVariable);

		FieldSymbol targetVariable = new FieldSymbol("S2");
		targetVariable.setType(StatemachineSymbolTable.TARGET);
		transition1Symbol.define(targetVariable);

		table.defineTransition(transition1Symbol);

		List<? extends Symbol> stateSymbols = table.getAllSymbols().stream()
				.filter(s -> {
					return s.getScope().getName().equals("state");
				})
				.collect(Collectors.toList());
		assertThat(stateSymbols).hasSize(2);
	}

	@Test
	public void testVisitor() {
		DefaultSymbolTable table = new DefaultSymbolTable();
		ClassSymbol classA = new ClassSymbol("classA");
		table.defineGlobal(classA);
		TestSymbolTableVisitor visitor = new TestSymbolTableVisitor();
		table.visitSymbolTable(visitor);
		assertThat(visitor.scopesEnter).hasSize(2);
		assertThat(visitor.symbolsEnter).hasSize(1);
	}

	private static class StatemachineSymbolTable extends AbstractSymbolTable {

		public static final ClassSymbol STATE = new ClassSymbol("state");
		public static final ClassSymbol TRANSITION = new ClassSymbol("transition");
		public static final ClassSymbol SOURCE = new ClassSymbol("source");
		public static final ClassSymbol TARGET = new ClassSymbol("target");

		public StatemachineSymbolTable() {
			defineGlobal(STATE);
			defineGlobal(TRANSITION);
			defineGlobal(SOURCE);
			defineGlobal(TARGET);
		}

		public void defineState(ClassSymbol state) {
			STATE.define(state);
		}

		public void defineTransition(ClassSymbol transition) {
			TRANSITION.define(transition);
		}
	}
}
