/*
 * Copyright 2018-2019 the original author or authors.
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
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.DocumentSymbol.DocumentSymbolBuilder;
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.domain.SymbolInformation.SymbolInformationBuilder;
import org.springframework.dsl.service.symbol.SymbolizeInfo;
import org.springframework.dsl.symboltable.Scope;
import org.springframework.dsl.symboltable.Symbol;
import org.springframework.dsl.symboltable.SymbolTableVisitor;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;

/**
 * {@link SymbolTableVisitor} which builds a {@link SymbolizeInfo}.
 *
 * @author Janne Valkealahti
 *
 */
public class DocumentSymbolTableVisitor implements SymbolTableVisitor {

	private static final Logger log = LoggerFactory.getLogger(DocumentSymbolTableVisitor.class);

	// things are going to be inserted here when scopes and symbols are visited
	private final List<DocumentSymbol> documentSymbols = new ArrayList<>();
	private final List<SymbolInformation> symbolInformations = new ArrayList<>();

	// in flight stack for builders while visiting
	private final Stack<BuilderHolder> stack = new Stack<>();

	// uri passed to location in SymbolInformation
	private final String uri;

	// active symbol query to limit results
	private Function<Symbol, Boolean> symbolQuery = (symbol) -> true;

	// active symbols and scopes based on a used queries
	private HashSet<Scope> activeScopes = new HashSet<>();
	private HashSet<Symbol> activeSymbols = new HashSet<>();
	private Function<Scope, Boolean> scopeActiveQuery = (scope) -> false;
	private Function<Symbol, Boolean> symbolActiveQuery = (symbol) -> true;

	/**
	 * Instantiates a new document symbol table visitor.
	 */
	public DocumentSymbolTableVisitor() {
		this(null);
	}

	/**
	 * Instantiates a new document symbol table visitor. Given {@code uri} is passed
	 * to location uri in a {@link SymbolInformation}.
	 *
	 * @param uri the uri
	 */
	public DocumentSymbolTableVisitor(String uri) {
		this.uri = uri;
	}

	/**
	 * Conveniance method to build visitor with url and symbol query.
	 *
	 * @param uri the uri
	 * @param symbolQuery the symbor query
	 * @return document symbol table visitor
	 */
	public static DocumentSymbolTableVisitor from(String uri, Function<Symbol, Boolean> symbolQuery) {
		DocumentSymbolTableVisitor visitor = new DocumentSymbolTableVisitor(uri);
		visitor.setSymbolQuery(symbolQuery);
		return visitor;
	}

	@Override
	public void enterVisitScope(Scope scope) {
		log.debug("enterVisitScope {} {} {}", scope, scope != null ? scope.getClass().getSimpleName() : null,
				stack.size());

		boolean active = scopeActiveQuery.apply(scope);
		log.debug("Scope enter {} active state {}", scope, active);
		if (!active) {
			return;
		}
		activeScopes.add(scope);

		DocumentSymbolBuilder<?> documentSymbolBuilder = DocumentSymbol.documentSymbol()
			.name(scope.getName())
			.kind(scope.getScopeKind())
			.range(scope.getScopeRange())
			.selectionRange(scope.getScopeRange());
		SymbolInformationBuilder<?> symbolInformationBuilder = SymbolInformation.symbolInformation()
			.name(scope.getName())
			.kind(scope.getScopeKind())
			.location()
				.uri(uri)
				.range(scope.getScopeRange())
				.and();
		stack.push(new BuilderHolder(documentSymbolBuilder, symbolInformationBuilder));
	}

	@Override
	public void exitVisitScope(Scope scope) {
		log.debug("exitVisitScope {} {} {}", scope, scope != null ? scope.getClass().getSimpleName() : null,
				stack.size());

		boolean active = activeScopes.contains(scope);
		log.debug("Scope exit {} active state {}", scope, active);
		if (!active) {
			return;
		}

		BuilderHolder builderHolder = stack.pop();
		DocumentSymbol ds = builderHolder.documentSymbolBuilder.build();

		if (stack.isEmpty()) {
			documentSymbols.add(ds);
		} else {
			stack.peek().documentSymbolBuilder.child(ds);
		}
	}

	@Override
	public void enterVisitSymbol(Symbol symbol) {
		log.debug("enterVisitSymbol {} {} {}", symbol, symbol != null ? symbol.getClass().getSimpleName() : null,
				stack.size());

		boolean active = symbolActiveQuery.apply(symbol);
		log.debug("Symbol enter {} active state {}", symbol, active);
		if (!active) {
			return;
		}
		activeSymbols.add(symbol);

		DocumentSymbolBuilder<?> documentSymbolBuilder = DocumentSymbol.documentSymbol()
			.name(symbol.getName())
			.kind(symbol.getKind())
			.detail(symbol.getDetail())
			.range(symbol.getRange())
			.selectionRange(symbol.getRange());
		SymbolInformationBuilder<?> symbolInformationBuilder = SymbolInformation.symbolInformation()
			.name(symbol.getName())
			.kind(symbol.getKind())
			.containerName(symbol.getScope() != null ? symbol.getScope().getName() : null)
			.location()
				.uri(uri)
				.range(symbol.getRange())
				.and();
		stack.push(new BuilderHolder(documentSymbolBuilder, symbolInformationBuilder));
	}

	@Override
	public void exitVisitSymbol(Symbol symbol) {
		log.debug("exitVisitSymbol {} {} {}", symbol, symbol != null ? symbol.getClass().getSimpleName() : null,
				stack.size());

		boolean active = activeSymbols.contains(symbol);
		log.debug("Symbol exit {} active state {}", symbol, active);
		if (!active) {
			return;
		}

		Boolean match = symbolQuery.apply(symbol);
		BuilderHolder builderHolder = stack.pop();
		if (stack.isEmpty()) {
			documentSymbols.add(builderHolder.documentSymbolBuilder.build());
		} else {
			stack.peek().documentSymbolBuilder.child(builderHolder.documentSymbolBuilder.build());
		}
		// SymbolInformation is flat, add all symbols
		if (match != null && match) {
			symbolInformations.add(builderHolder.symbolInformationBuilder.build());
		}
	}

	public void setSymbolQuery(Function<Symbol, Boolean> query) {
		Assert.notNull(query, "symbolQuery cannot be null");
		this.symbolQuery = query;
	}

	public void setScopeActiveQuery(Function<Scope, Boolean> query) {
		Assert.notNull(query, "scopeActiveQuery cannot be null");
		this.scopeActiveQuery = query;
	}

	public void setSymbolActiveQuery(Function<Symbol, Boolean> query) {
		Assert.notNull(query, "symbolActiveQuery cannot be null");
		this.symbolActiveQuery = query;
	}

	public SymbolizeInfo getSymbolizeInfo() {
		return SymbolizeInfo.of(Flux.fromIterable(documentSymbols), Flux.fromIterable(symbolInformations));
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
