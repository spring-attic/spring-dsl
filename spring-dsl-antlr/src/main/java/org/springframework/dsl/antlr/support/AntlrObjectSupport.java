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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.springframework.dsl.antlr.AntlrFactory;
import org.springframework.util.Assert;

/**
 * Base language support class for {@code ANTLR}.
 *
 * @author Janne Valkealahti
 *
 * @param <L> the type of lexer
 * @param <P> the type of parser
 *
 */
public abstract class AntlrObjectSupport<L extends Lexer, P extends Parser> {

	private final AntlrFactory<L, P> antlrFactory;

	/**
	 * Instantiates a new abstract antlr linter.
	 *
	 * @param antlrFactory the antlr factory
	 */
	public AntlrObjectSupport(AntlrFactory<L, P> antlrFactory) {
		Assert.notNull(antlrFactory, "AntlrFactory cannot be null");
		this.antlrFactory = antlrFactory;
	}

	/**
	 * Gets the antlr factory.
	 *
	 * @return the antlr factory
	 */
	protected AntlrFactory<L, P> getAntlrFactory() {
		return antlrFactory;
	}

	/**
	 * Gets the parser.
	 *
	 * @param input the input
	 * @return the parser
	 */
	protected P getParser(CharStream input) {
		L lexer = getAntlrFactory().createLexer(input);
		Assert.notNull(lexer, "Lexer must be set in AntlrFactory");
		return getAntlrFactory().createParser(new CommonTokenStream(lexer));
	}
}
