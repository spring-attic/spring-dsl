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
package org.springframework.dsl.antlr;

import java.util.List;

import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.dsl.symboltable.SymbolTable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface representing a parsing result from a {@link AntlrParseService}. All
 * methods in this interface are either returning a {@link Mono} or a
 * {@link Flux} with default implementations as {@link Mono#empty()} or
 * {@link Flux#empty()} respectively. This allows full extendability for future
 * needs and implementor can choose what to implement.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result
 */
public interface AntlrParseResult<T> {

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	default Mono<T> getResult() {
		return Mono.empty();
	}

	/**
	 * Gets the symbol table.
	 *
	 * @return the symbol table
	 */
	default Mono<SymbolTable> getSymbolTable() {
		return Mono.empty();
	}

	/**
	 * Gets the reconcile problems.
	 *
	 * @return the reconcile problems
	 */
	default Flux<ReconcileProblem> getReconcileProblems() {
		return Flux.empty();
	}

	/**
	 * Gets the completion items.
	 *
	 * @param position the position
	 * @return the completion items
	 */
	default Flux<CompletionItem> getCompletionItems(Position position) {
		return Flux.empty();
	}

	/**
	 * Gets the {@link Hover} in a given {@link Position}.
	 *
	 * @param position the position
	 * @return the hover
	 */
	default Mono<Hover> getHover(Position position) {
		return Mono.empty();
	}

	/**
	 * Gets the document symbols.
	 *
	 * @return the document symbols
	 */
	default Flux<DocumentSymbol> getDocumentSymbols() {
		return Flux.empty();
	}

	/**
	 * For convenience just return and implement
	 * {@link AntlrParseResult#getResult()} from a list of problems.
	 *
	 * @param result the result
	 * @return the antlr parse result
	 *
	 * @param <T> the type of a AntlrParseResult
	 */
	public static <T> AntlrParseResult<T> from(T result) {
		return new AntlrParseResult<T>() {

			@Override
			public Mono<T> getResult() {
				return Mono.just(result);
			}
		};
	}

	/**
	 * For convenience just return and implement
	 * {@link AntlrParseResult#getSymbolTable()} from a symbolTable.
	 *
	 * @param symbolTable the symbolTable
	 * @return the antlr parse result
	 *
	 * @param <T> the type of a AntlrParseResult
	 */
	public static <T> AntlrParseResult<T> from(SymbolTable symbolTable) {
		return new AntlrParseResult<T>() {

			@Override
			public Mono<SymbolTable> getSymbolTable() {
				return Mono.just(symbolTable);
			}
		};
	}

	/**
	 * For convenience just return and implement
	 * {@link AntlrParseResult#getResult()} from a list of problems.
	 *
	 * @param problems the problems
	 * @return the antlr parse result
	 *
	 * @param <T> the type of a AntlrParseResult
	 */
	public static <T> AntlrParseResult<T> from(List<ReconcileProblem> problems) {
		return new AntlrParseResult<T>() {

			@Override
			public Flux<ReconcileProblem> getReconcileProblems() {
				return Flux.fromIterable(problems);
			}
		};
	}
}
