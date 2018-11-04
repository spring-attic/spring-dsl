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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.springframework.dsl.Test1Lexer;
import org.springframework.dsl.Test1Parser;
import org.springframework.dsl.Test2Grammar;
import org.springframework.dsl.Test2Lexer;
import org.springframework.dsl.model.LanguageId;

public class TestAntrlUtils {

	public static LanguageId TEST1_LANGUAGE_ID = LanguageId.languageId("test1", "Antlr test1 Language");
	public static LanguageId TEST2_LANGUAGE_ID = LanguageId.languageId("test2", "Antlr test2 Language");

	public static AntlrFactory<Test1Lexer, Test1Parser> TEST1_ANTRL_FACTORY = new AntlrFactory<Test1Lexer, Test1Parser>() {

		@Override
		public Test1Parser createParser(TokenStream tokenStream) {
			return new Test1Parser(tokenStream);
		}

		@Override
		public Test1Lexer createLexer(CharStream input) {
			return new Test1Lexer(input);
		}
	};

	public static AntlrFactory<Test2Lexer, Test2Grammar> TEST2_ANTRL_FACTORY = new AntlrFactory<Test2Lexer, Test2Grammar>() {

		@Override
		public Test2Grammar createParser(TokenStream tokenStream) {
			return new Test2Grammar(tokenStream);
		}

		@Override
		public Test2Lexer createLexer(CharStream input) {
			return new Test2Lexer(input);
		}
	};
}
