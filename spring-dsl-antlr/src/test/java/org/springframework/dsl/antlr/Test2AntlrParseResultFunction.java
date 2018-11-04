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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.dsl.Test2Grammar;
import org.springframework.dsl.Test2Lexer;
import org.springframework.dsl.antlr.support.AbstractAntlrParseResultFunction;
import org.springframework.dsl.antlr.support.DefaultAntlrCompletionEngine;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.service.reconcile.DefaultReconcileProblem;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.dsl.symboltable.SymbolTable;
import org.springframework.dsl.symboltable.model.ClassSymbol;
import org.springframework.util.ObjectUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class Test2AntlrParseResultFunction
		extends AbstractAntlrParseResultFunction<Object, Test2Lexer, Test2Grammar> {

	public Test2AntlrParseResultFunction() {
		super(TestAntrlUtils.TEST2_ANTRL_FACTORY);
	}

	@Override
	protected Mono<AntlrParseResult<Object>> parse(Document document) {
		List<ReconcileProblem> errors = new ArrayList<>();
		Test2Grammar parser = getParser(CharStreams.fromString(document.content()));
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
        parser.removeErrorListeners();
        parser.addErrorListener(new Test2ErrorListener(errors));
        parser.definitions();

        parser = getParser(CharStreams.fromString(document.content()));
		ParseTree tree = parser.definitions();
		Test2Visitor visitor = new Test2Visitor(errors);
		AntlrParseResult<Object> result = visitor.visit(tree);
		return Mono.just(result);
	}

	@Override
	protected Flux<ReconcileProblem> getReconcileProblems(Mono<AntlrParseResult<Object>> shared, Document document) {
		Flux<ReconcileProblem> other = Flux.defer(() -> {
			List<ReconcileProblem> errors = new ArrayList<>();
			SymbolTable symbolTable = getSymbolTable(shared, document).block();
			List<String> states = symbolTable.getAllSymbols().stream()
				.filter(s -> s.getScope().getName() == "org.springframework.statemachine.state.State")
				.map(s -> s.getName())
				.collect(Collectors.toList());
			symbolTable.getAllSymbols().stream().forEach(s -> {
				if (s.getScope().getName() == "org.springframework.statemachine.transition.Transition") {
					if (s instanceof ClassSymbol) {
						((ClassSymbol)s).getFields().stream().forEach(f -> {
							if (!states.contains(f.getName())) {
								errors.add(new DefaultReconcileProblem("missing", f.getRange()));
							}
						});
					}
				}
			});
			return Flux.fromIterable(errors);
		});
		return Flux.concat(super.getReconcileProblems(shared, document), other);
	}

	@Override
	protected Flux<CompletionItem> getCompletionItems(Mono<AntlrParseResult<Object>> shared, Document document, Position position) {
		Test2Grammar p = getParser(CharStreams.fromString(document.content()));
		HashSet<Integer> preferredRules = new HashSet<>(
				Arrays.asList(Test2Grammar.RULE_sourceId, Test2Grammar.RULE_targetId));
		AntlrCompletionEngine completionEngine = new DefaultAntlrCompletionEngine(p, preferredRules, null);
		AntlrCompletionResult completionResult = completionEngine.collectResults(position, p.definitions());

		Flux<String> items1 = Flux.defer(() -> {
			ArrayList<String> completions = new ArrayList<String>();
			for (Entry<Integer, List<Integer>> e : completionResult.getTokens().entrySet()) {
				if (e.getKey() > 0) {
					Vocabulary vocabulary = p.getVocabulary();
					String displayName = vocabulary.getDisplayName(e.getKey());
					completions.add(displayName);
				}
			}
			return Flux.fromIterable(completions);
		});

		Flux<String> items2 = Flux.defer(() -> {
			SymbolTable symbolTable = getSymbolTable(shared, document).block();
			ArrayList<String> completions = new ArrayList<String>();
			for (Entry<Integer, List<Integer>> e : completionResult.getRules().entrySet()) {
				if (e.getKey() == Test2Grammar.RULE_sourceId) {
					symbolTable.getAllSymbols().stream().forEach(s -> {
						if (ObjectUtils.nullSafeEquals(s.getScope().getName(), "org.springframework.statemachine.state.State")) {
							completions.add(s.getName());
						}
					});
				}
			}
			return Flux.fromIterable(completions);
		});

		return Flux.concat(items2, items1)
			.flatMap(c -> {
				CompletionItem item = new CompletionItem();
				item.setLabel(c);
				return Mono.just(item);
			});
	}
}
