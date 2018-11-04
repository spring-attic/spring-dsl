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
package org.springframework.dsl.symboltable.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.DocumentSymbol.DocumentSymbolBuilder;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableVisitor;

/**
 * {@link SymbolTableVisitor} which builds a {@link DocumentSymbol}.
 *
 * @author Janne Valkealahti
 *
 */
public class DocumentSymbolTableVisitor implements SymbolTableVisitor {

	private static final Logger log = LoggerFactory.getLogger(DocumentSymbolTableVisitor.class);
	private final List<DocumentSymbol> documentSymbols = new ArrayList<>();
	private final Stack<DocumentSymbolBuilder<?>> stack = new Stack<>();

	@Override
	public void enterVisitScope(Scope scope) {
		log.debug("enterVisitScope {}", scope);
	}

	@Override
	public void exitVisitScope(Scope scope) {
		log.debug("exitVisitScope {}", scope);
	}

	@Override
	public void enterVisitSymbol(Symbol symbol) {
		log.debug("enterVisitSymbol {}", symbol);
		DocumentSymbolBuilder<?> builder = DocumentSymbol.documentSymbol()
			.name(symbol.getName())
			.kind(SymbolKind.String)
			.range(symbol.getRange())
			.selectionRange(symbol.getRange());
		stack.push(builder);
	}

	public List<DocumentSymbol> getDocumentSymbols() {
		return documentSymbols;
	}

	@Override
	public void exitVisitSymbol(Symbol symbol) {
		log.debug("exitVisitSymbol {}", symbol);
		DocumentSymbolBuilder<?> builder = stack.pop();
		if (stack.isEmpty()) {
			documentSymbols.add(builder.build());
		} else {
			stack.peek().child(builder.build());
		}
	}

	protected SymbolKind getSymbolKind(Symbol symbol) {
		return SymbolKind.String;
	}
}
