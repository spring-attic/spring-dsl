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
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

/**
 * {@code AntlrFactory} is a simple interface able to create a {@link Lexer} and
 * {@link Parser}.
 *
 * @author Janne Valkealahti
 *
 * @param <L> the type of lexer
 * @param <P> the type of parser
 *
 */
public interface AntlrFactory<L extends Lexer, P extends Parser> {

    /**
     * Creates a new Antlr {@link Lexer}.
     *
     * @param input the input
     * @return the lexer
     */
    L createLexer(CharStream input);

    /**
     * Creates a new Antlr {@link Parser}.
     *
     * @param tokenStream the token stream
     * @return the parser
     */
    P createParser(TokenStream tokenStream);
}
