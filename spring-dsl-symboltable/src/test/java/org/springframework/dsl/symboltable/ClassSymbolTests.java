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

import org.junit.Test;
import org.springframework.dsl.symboltable.model.ClassSymbol;
import org.springframework.dsl.symboltable.model.FieldSymbol;
import org.springframework.dsl.symboltable.model.PredefinedScope;

public class ClassSymbolTests {

	@Test
	public void testBasics() {
		ClassSymbol sym1 = new ClassSymbol("sym1");
		assertThat(sym1.getName()).isEqualTo("sym1");
		assertThat(sym1.getNestedScopedSymbols()).hasSize(0);

		ClassSymbol sym2 = new ClassSymbol("sym2");
		sym1.define(sym2);
		assertThat(sym1.getNestedScopedSymbols()).hasSize(1);
		assertThat(sym1.getSymbols()).hasSize(1);
		assertThat(sym1.getMembers()).hasSize(1);
	}

	@Test
	public void testClassWithClassFields() {
		PredefinedScope scope = new PredefinedScope();

		ClassSymbol classA = new ClassSymbol("classA");
		classA.setSuperClass("classAParent");
		scope.define(classA);

		ClassSymbol classB = new ClassSymbol("classB");
		scope.define(classB);

		FieldSymbol field1 = new FieldSymbol("classA");
		classB.define(field1);

		assertThat(classB.resolveField("classA")).isEqualTo(classA);
	}

	@Test
	public void testSuperClass() {
		ClassSymbol classA = new ClassSymbol("classA");
		classA.setSuperClass("classAParent");
		ClassSymbol superClassScope1 = classA.getSuperClassScope();
		assertThat(superClassScope1).isNull();

		ClassSymbol classB = new ClassSymbol("classB");
		ClassSymbol classC = new ClassSymbol("classC");
		classB.setSuperClass("classC");
		classB.setEnclosingScope(classC);
		assertThat(classB.getEnclosingScope()).isSameAs(classC);
	}

	@Test
	public void testSuperClassWithRootScope() {
		PredefinedScope scope = new PredefinedScope();

		ClassSymbol classB = new ClassSymbol("classB");
		scope.define(classB);

		ClassSymbol classA = new ClassSymbol("classA");
		classA.setSuperClass("classB");
		classA.setScope(classB);
		assertThat(classA.getEnclosingScope()).isSameAs(classB);
		assertThat(classA.getEnclosingScope().getEnclosingScope()).isSameAs(scope);
	}

	@Test
	public void testVisitor() {
		ClassSymbol classA = new ClassSymbol("classA");
		TestSymbolTableVisitor visitor1 = new TestSymbolTableVisitor();
		classA.accept(visitor1);
		assertThat(visitor1.scopesEnter).hasSize(1);
		assertThat(visitor1.symbolsEnter).hasSize(1);
		assertThat(visitor1.scopesEnter.get(0)).isSameAs(classA);
		assertThat(visitor1.symbolsEnter.get(0)).isSameAs(classA);
	}
}
