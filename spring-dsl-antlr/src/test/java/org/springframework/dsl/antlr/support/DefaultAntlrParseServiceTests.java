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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;
import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.dsl.symboltable.SymbolTable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Tests for {@link DefaultAntlrParseService}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultAntlrParseServiceTests {

	@Test
	public void test() {
		TextDocument document1 = new TextDocument("", LanguageId.TXT, 0, "");
		TextDocument document2 = new TextDocument("", LanguageId.TXT, 1, "");

		DefaultAntlrParseService<String> service = new DefaultAntlrParseService<>();

		Mono<AntlrParseResult<String>> mono1 = service.parse(document1, s1);
		assertThat(mono1).isNotNull();
		assertThat(mono1.block()).isNotNull();
		assertThat(mono1.block().getResult()).isNotNull();
		assertThat(mono1.block().getResult().block()).isEqualTo("hi1");

		Mono<AntlrParseResult<String>> mono2 = service.parse(document1, s2);
		assertThat(mono2).isNotNull();
		assertThat(mono2.block()).isNotNull();
		assertThat(mono2.block().getResult()).isNotNull();
		assertThat(mono2.block().getResult().block()).isEqualTo("hi1");
		assertThat(mono1.block()).isSameAs(mono2.block());

		Mono<AntlrParseResult<String>> mono3 = service.parse(document2, s2);
		assertThat(mono3).isNotNull();
		assertThat(mono3.block()).isNotNull();
		assertThat(mono3.block().getResult()).isNotNull();
		assertThat(mono3.block().getResult().block()).isEqualTo("hi2");
		assertThat(mono1.block()).isNotSameAs(mono3.block());
	}

	private Function<Document, ? extends Mono<? extends AntlrParseResult<String>>> s1 = (document) -> {
		return Mono.just(a1);
	};

	private Function<Document, ? extends Mono<? extends AntlrParseResult<String>>> s2 = (document) -> {
		return Mono.just(a2);
	};

	private static AntlrParseResult<String> a1 = new AntlrParseResult<String>() {

		@Override
		public Mono<String> getResult() {
			return Mono.just("hi1");
		}

		@Override
		public Mono<SymbolTable> getSymbolTable() {
			return Mono.empty();
		}

		@Override
		public Flux<ReconcileProblem> getReconcileProblems() {
			return Flux.empty();
		}
	};

	private static AntlrParseResult<String> a2 = new AntlrParseResult<String>() {

		@Override
		public Mono<String> getResult() {
			return Mono.just("hi2");
		}

		@Override
		public Mono<SymbolTable> getSymbolTable() {
			return Mono.empty();
		}

		@Override
		public Flux<ReconcileProblem> getReconcileProblems() {
			return Flux.empty();
		}
	};
}
