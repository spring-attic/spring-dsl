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
package org.springframework.dsl.antlr.support;

import java.util.function.Function;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.springframework.dsl.antlr.AntlrFactory;
import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.dsl.service.symbol.SymbolizeInfo;
import org.springframework.dsl.support.DslUtils;
import org.springframework.dsl.symboltable.SymbolTable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base implementation of a function creating {@link AntlrParseResult} from a
 * {@link Document}.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 * @param <L> the type of lexer
 * @param <P> the type of parser
 */
public abstract class AbstractAntlrParseResultFunction<T, L extends Lexer, P extends Parser>
		extends AntlrObjectSupport<L, P>
		implements Function<Document, Mono<? extends AntlrParseResult<T>>> {

	/**
	 * Instantiates a new abstract antlr parse result function.
	 *
	 * @param antlrFactory the antlr factory
	 */
	public AbstractAntlrParseResultFunction(AntlrFactory<L, P> antlrFactory) {
		super(antlrFactory);
	}

	@Override
	public Mono<? extends AntlrParseResult<T>> apply(Document document) {
		return Mono.defer(() -> {
			Mono<AntlrParseResult<T>> shared = parse(document).cache();
			return Mono.just(new AntlrParseResult<T>() {

				@Override
				public Mono<T> getResult() {
					return AbstractAntlrParseResultFunction.this.getResult(shared, document);
				}

				@Override
				public Mono<SymbolTable> getSymbolTable() {
					return AbstractAntlrParseResultFunction.this.getSymbolTable(shared, document);
				};

				@Override
				public Flux<ReconcileProblem> getReconcileProblems() {
					return AbstractAntlrParseResultFunction.this.getReconcileProblems(shared, document);
				}

				@Override
				public Flux<CompletionItem> getCompletionItems(Position position) {
					return AbstractAntlrParseResultFunction.this.getCompletionItems(shared, document, position);
				}

				@Override
				public SymbolizeInfo getSymbolizeInfo() {
					return AbstractAntlrParseResultFunction.this.getSymbolizeInfo(shared, document);
				}

				@Override
				public Mono<Hover> getHover(Position position) {
					return AbstractAntlrParseResultFunction.this.getHover(shared, document, position);
				}
			});
		});
	}

	protected Mono<AntlrParseResult<T>> parse(Document document) {
		return Mono.empty();
	}

	protected Mono<T> getResult(Mono<AntlrParseResult<T>> shared, Document document) {
		return shared.flatMap(r -> Mono.from(r.getResult()));
	}

	protected Mono<SymbolTable> getSymbolTable(Mono<AntlrParseResult<T>> shared, Document document) {
		return shared.flatMap(r -> Mono.from(r.getSymbolTable()));
	}

	protected Flux<ReconcileProblem> getReconcileProblems(Mono<AntlrParseResult<T>> shared, Document document) {
		return shared.flatMapMany(r -> Flux.from(r.getReconcileProblems()));
	}

	protected Flux<CompletionItem> getCompletionItems(Mono<AntlrParseResult<T>> shared, Document document, Position position) {
		return Flux.empty();
	}

	protected SymbolizeInfo getSymbolizeInfo(Mono<AntlrParseResult<T>> shared, Document document) {

		Mono<SymbolizeInfo> map = shared.map(r -> r.getSymbolizeInfo());
		Mono<Flux<DocumentSymbol>> map2 = map.map(si -> si.documentSymbols());
		Flux<DocumentSymbol> flatMapMany1 = map2.flatMapMany(x -> x);
		Mono<Flux<SymbolInformation>> map3 = map.map(si -> si.symbolInformations());
		Flux<SymbolInformation> flatMapMany2 = map3.flatMapMany(x -> x);

		SymbolizeInfo symbolizeInfo = SymbolizeInfo.of(flatMapMany1, flatMapMany2);
		return symbolizeInfo;
	}

	protected Mono<Hover> getHover(Mono<AntlrParseResult<T>> shared, Document document, Position position) {

		SymbolizeInfo symbolizeInfo = getSymbolizeInfo(shared, document);
		Flux<DocumentSymbol> documentSymbols = symbolizeInfo.documentSymbols();

		return documentSymbols
			.filter(s -> DslUtils.isPositionInRange(position, s.getRange()))
			.map(s -> Hover.hover()
				.contents()
					.value(s.getName())
					.and()
				.range(s.getRange())
				.build())
			.next();
	}
}
