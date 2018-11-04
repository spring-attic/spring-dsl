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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;
import org.springframework.dsl.Test1Lexer;
import org.springframework.dsl.Test1Parser;
import org.springframework.dsl.Test2Grammar;
import org.springframework.dsl.Test2Lexer;
import org.springframework.dsl.antlr.AntlrCompletionResult;
import org.springframework.dsl.antlr.AntlrFactory;
import org.springframework.dsl.antlr.support.DefaultAntlrCompletionEngine;
import org.springframework.dsl.domain.Position;

/**
 * Tests for {@link DefaultAntlrCompletionEngine}. These test depends on an
 * antlr {@code Test*.g4} test grammars. {@link DefaultAntlrCompletionEngine}
 * depends on a real code generated {@link Parser} and {@link Lexer} classes
 * from a {@code grammar}. This generation is done during a test phase.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultAntlrCompletionEngineTests {

	private AntlrFactory<Test1Lexer, Test1Parser> test1AntlrFactory = new AntlrFactory<Test1Lexer, Test1Parser>() {

		@Override
		public Test1Parser createParser(TokenStream tokenStream) {
			return new Test1Parser(tokenStream);
		}

		@Override
		public Test1Lexer createLexer(CharStream input) {
			return new Test1Lexer(input);
		}
	};

	private AntlrFactory<Test2Lexer, Test2Grammar> test2AntlrFactory = new AntlrFactory<Test2Lexer, Test2Grammar>() {

		@Override
		public Test2Grammar createParser(TokenStream tokenStream) {
			return new Test2Grammar(tokenStream);
		}

		@Override
		public Test2Lexer createLexer(CharStream input) {
			return new Test2Lexer(input);
		}
	};

	@Test
	public void test1() {
		String input = "";

		Test1Lexer lexer = test1AntlrFactory.createLexer(CharStreams.fromString(input));
		Test1Parser parser = test1AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.r();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 0), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(1);
		assertThat(candidates.getTokens().containsKey(Test1Lexer.AB)).isTrue();
	}

	@Test
	public void test2() {
		String input = "";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 0), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(5);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.STATEMACHINE)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.STATE)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.TRANSITION)).isTrue();
	}


	@Test
	public void test3() {
		String input = "statemachine M1 {";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 17), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(3);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.STATE)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.TRANSITION)).isTrue();
	}

	@Test
	public void test4() {
		String input = "statemachine M1 { state S1 {";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 28), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(3);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.INITIAL)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.END)).isTrue();
	}

	@Test
	public void test41() {
		String input = "statemachine M1 { state S1 {\n}";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 28), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(3);

		assertThat(candidates.getTokens().containsKey(Test2Lexer.INITIAL)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.END)).isTrue();
	}

	@Test
	public void test5() {
		String input = "statemachine M1 { state S1 {} }";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 13), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(1);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.ID)).isTrue();
	}

	@Test
	public void test6() {
		String input = "statemachine M1 { state S1 {} }";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 28), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(3);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.INITIAL)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.END)).isTrue();
	}

	@Test
	public void test61() {
		String input = "statemachine M1 {\nstate S1 {\n}\n}";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(2, 0), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		assertThat(candidates.getTokens().size()).isEqualTo(3);
		assertThat(candidates.getTokens().containsKey(Test2Lexer.INITIAL)).isTrue();
		assertThat(candidates.getTokens().containsKey(Test2Lexer.END)).isTrue();
	}

	@Test
	public void test7() {
		String input = "s";

		Test2Lexer lexer = test2AntlrFactory.createLexer(CharStreams.fromString(input));
		Test2Grammar parser = test2AntlrFactory.createParser(new CommonTokenStream(lexer));

		parser.definitions();

		DefaultAntlrCompletionEngine core = new DefaultAntlrCompletionEngine(parser);
		AntlrCompletionResult candidates = core.collectResults(new Position(0, 1), null);

		assertThat(candidates).isNotNull();
		assertThat(candidates.getTokens()).isNotNull();
		// currently for this plain invalid input we don't get anything
		assertThat(candidates.getTokens().size()).isEqualTo(0);
	}
}
