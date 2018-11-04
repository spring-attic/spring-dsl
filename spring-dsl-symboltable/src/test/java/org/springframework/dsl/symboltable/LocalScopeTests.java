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
import org.springframework.dsl.symboltable.model.LocalScope;

public class LocalScopeTests {

	@Test
	public void test1() {
		LocalScope scope1 = new LocalScope(null);
		LocalScope scope2 = new LocalScope(scope1);
		scope1.nest(scope2);

		assertThat(scope1.getNestedScopes()).hasSize(1);

		ClassSymbol classA = new ClassSymbol("classA");
		scope2.define(classA);
		assertThat(scope2.getAllSymbols()).hasSize(1);
		assertThat(scope1.getAllSymbols()).hasSize(1);
	}

	@Test
	public void testVisitor() {
		LocalScope scope1 = new LocalScope(null);
		TestSymbolTableVisitor visitor1 = new TestSymbolTableVisitor();
		scope1.accept(visitor1);
		assertThat(visitor1.scopesEnter).hasSize(1);
		assertThat(visitor1.scopesEnter.get(0)).isSameAs(scope1);
		assertThat(visitor1.scopesExit).hasSize(1);
		assertThat(visitor1.scopesExit.get(0)).isSameAs(scope1);

		LocalScope scope2 = new LocalScope(null);
		LocalScope scope3 = new LocalScope(scope2);
		scope2.nest(scope3);

		TestSymbolTableVisitor visitor2 = new TestSymbolTableVisitor();
		scope2.accept(visitor2);
		assertThat(visitor2.scopesEnter).hasSize(2);
		assertThat(visitor2.scopesEnter.get(0)).isSameAs(scope2);
		assertThat(visitor2.scopesEnter.get(1)).isSameAs(scope3);
		assertThat(visitor2.scopesExit).hasSize(2);
		assertThat(visitor2.scopesExit.get(0)).isSameAs(scope3);
		assertThat(visitor2.scopesExit.get(1)).isSameAs(scope2);
	}
}
