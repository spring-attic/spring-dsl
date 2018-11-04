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

import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.springframework.dsl.Test2Grammar.DefinitionsContext;
import org.springframework.dsl.Test2Grammar.SourceIdContext;
import org.springframework.dsl.Test2Grammar.TargetIdContext;
import org.springframework.dsl.Test2GrammarBaseVisitor;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.dsl.symboltable.SymbolTable;
import org.springframework.dsl.symboltable.model.ClassSymbol;
import org.springframework.dsl.symboltable.model.FieldSymbol;
import org.springframework.dsl.symboltable.model.TypeAlias;
import org.springframework.dsl.symboltable.support.DefaultSymbolTable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link ParseTreeVisitor} for {@code ANTLR test2 language}.
 *
 * @author Janne Valkealahti
 *
 */
public class Test2Visitor extends Test2GrammarBaseVisitor<AntlrParseResult<Object>> {

	private final List<ReconcileProblem> errors;

	public Test2Visitor(List<ReconcileProblem> errors) {
		this.errors = errors;
	}

	@Override
	public AntlrParseResult<Object> visitDefinitions(DefinitionsContext ctx) {
		DefaultSymbolTable symbolTable = new DefaultSymbolTable();
		// ClassSymbol stateMachineClassSymbol = new ClassSymbol("org.springframework.statemachine.StateMachine");
		ClassSymbol stateClassSymbol = new ClassSymbol("org.springframework.statemachine.state.State");
		stateClassSymbol.setRange(Range.from(0, 0, 0, 0));
		ClassSymbol transitionClassSymbol = new ClassSymbol("org.springframework.statemachine.transition.Transition");
		symbolTable.defineGlobal(stateClassSymbol);
		symbolTable.defineGlobal(transitionClassSymbol);

		if (ctx.machineObjectList() != null) {
			ctx.machineObjectList().state().forEach(stateContext -> {
				ClassSymbol classSymbol = new ClassSymbol(stateContext.id().getText());
				stateClassSymbol.define(classSymbol);
			});

			ctx.machineObjectList().transition().forEach(transitionContext -> {
				ClassSymbol classSymbol = new ClassSymbol(transitionContext.id().getText());
				transitionClassSymbol.define(classSymbol);
				transitionContext.transitionParameters().transitionParameter().stream().forEach(transitionParameter -> {
					SourceIdContext sourceId = transitionParameter.transitionType().sourceId();
					TargetIdContext targetId = transitionParameter.transitionType().targetId();

					if (sourceId != null) {
						FieldSymbol fieldSymbol = new FieldSymbol(sourceId.getText());
						fieldSymbol.setType(new TypeAlias("source", stateClassSymbol));
						fieldSymbol.setRange(Range.from(sourceId.getStart().getLine() - 1,
								sourceId.getStart().getCharPositionInLine(), sourceId.getStop().getLine() - 1,
								sourceId.getStop().getCharPositionInLine()));
						classSymbol.define(fieldSymbol);
					}
					if (targetId != null) {
						FieldSymbol fieldSymbol = new FieldSymbol(targetId.getText());
						fieldSymbol.setType(new TypeAlias("target", stateClassSymbol));
						fieldSymbol.setRange(Range.from(targetId.getStart().getLine() - 1,
								targetId.getStart().getCharPositionInLine(), targetId.getStop().getLine() - 1,
								targetId.getStop().getCharPositionInLine()));
						classSymbol.define(fieldSymbol);
					}
				});
			});
		}


		return new AntlrParseResult<Object>() {

			@Override
			public Mono<SymbolTable> getSymbolTable() {
				return Mono.just(symbolTable);
			}

			@Override
			public Flux<ReconcileProblem> getReconcileProblems() {
				return Flux.fromIterable(errors);
			}

			@Override
			public Flux<DocumentSymbol> getDocumentSymbols() {
				return getSymbolTable()
					.flatMapMany(st -> Flux.fromIterable(st.getAllSymbols()))
					.map(s -> DocumentSymbol.documentSymbol()
							.name(s.getName())
							.kind(SymbolKind.String)
							.range(s.getRange())
							.build());
			}
		};
	}
}
