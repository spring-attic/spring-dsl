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
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.domain.SymbolInformation.SymbolInformationBuilder;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.service.symbol.SymbolizeInfo;
import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableVisitor;

import reactor.core.publisher.Flux;

/**
 * {@link SymbolTableVisitor} which builds a {@link DocumentSymbol}.
 *
 * @author Janne Valkealahti
 *
 */
public class DocumentSymbolTableVisitor implements SymbolTableVisitor {

	private static final Logger log = LoggerFactory.getLogger(DocumentSymbolTableVisitor.class);
	private final List<DocumentSymbol> documentSymbols = new ArrayList<>();
	private final List<SymbolInformation> symbolInformations = new ArrayList<>();
	private final Stack<BuilderHolder> stack = new Stack<>();
	private final String uri;

	/**
	 * Instantiates a new document symbol table visitor.
	 */
	public DocumentSymbolTableVisitor() {
		this(null);
	}

	/**
	 * Instantiates a new document symbol table visitor.
	 *
	 * @param uri the uri
	 */
	public DocumentSymbolTableVisitor(String uri) {
		this.uri = uri;
	}

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
		DocumentSymbolBuilder<?> documentSymbolBuilder = DocumentSymbol.documentSymbol()
			.name(symbol.getName())
			.kind(SymbolKind.String)
			.range(symbol.getRange())
			.selectionRange(symbol.getRange());
		SymbolInformationBuilder<?> symbolInformationBuilder = SymbolInformation.symbolInformation()
			.name(symbol.getName())
			.kind(SymbolKind.String)
			.location()
				.uri(uri)
				.range(symbol.getRange())
				.and();
		stack.push(new BuilderHolder(documentSymbolBuilder, symbolInformationBuilder));
	}

	public SymbolizeInfo getSymbolizeInfo() {
		return SymbolizeInfo.of(Flux.fromIterable(documentSymbols), Flux.fromIterable(symbolInformations));
	}

	@Override
	public void exitVisitSymbol(Symbol symbol) {
		log.debug("exitVisitSymbol {}", symbol);
		BuilderHolder builderHolder = stack.pop();
		if (stack.isEmpty()) {
			documentSymbols.add(builderHolder.documentSymbolBuilder.build());
			symbolInformations.add(builderHolder.symbolInformationBuilder.build());
		} else {
			stack.peek().documentSymbolBuilder.child(builderHolder.documentSymbolBuilder.build());
		}
	}

	protected SymbolKind getSymbolKind(Symbol symbol) {
		return SymbolKind.String;
	}

	private static class BuilderHolder {
		DocumentSymbolBuilder<?> documentSymbolBuilder;
		SymbolInformationBuilder<?> symbolInformationBuilder;

		public BuilderHolder(DocumentSymbolBuilder<?> documentSymbolBuilder,
				SymbolInformationBuilder<?> symbolInformationBuilder) {
			this.documentSymbolBuilder = documentSymbolBuilder;
			this.symbolInformationBuilder = symbolInformationBuilder;
		}
	}
}
