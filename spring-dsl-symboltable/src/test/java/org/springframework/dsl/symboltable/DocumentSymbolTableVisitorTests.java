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
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.symboltable.model.ClassSymbol;
import org.springframework.dsl.symboltable.model.FieldSymbol;
import org.springframework.dsl.symboltable.model.PredefinedScope;
import org.springframework.dsl.symboltable.support.DocumentSymbolTableVisitor;

public class DocumentSymbolTableVisitorTests {

	@Test
	public void test1() {
		PredefinedScope scope = new PredefinedScope();

		ClassSymbol classA = new ClassSymbol("classA");
		scope.define(classA);

		ClassSymbol classB = new ClassSymbol("classB");
		scope.define(classB);

		FieldSymbol fieldA = new FieldSymbol("fieldA");
		classB.define(fieldA);

		DocumentSymbolTableVisitor visitor = new DocumentSymbolTableVisitor();
		scope.accept(visitor);
		assertThat(visitor.getDocumentSymbols()).hasSize(2);

		for (DocumentSymbol ds : visitor.getDocumentSymbols()) {
			if (ds.getName() == "classA") {
				assertThat(ds.getChildren()).isNull();
			}
			if (ds.getName() == "classB") {
				assertThat(ds.getChildren()).hasSize(1);
				assertThat(ds.getChildren().get(0).getName()).isEqualTo("fieldA");
			}
		}
	}
}
