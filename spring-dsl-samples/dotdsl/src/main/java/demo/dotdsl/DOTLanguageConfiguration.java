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
package demo.dotdsl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.antlr.AntlrFactory;
import org.springframework.dsl.antlr.AntlrParseService;
import org.springframework.dsl.antlr.support.DefaultAntlrParseService;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.Completioner;

/**
 * Configuration for a {@code dot} language supporting {@link Completioner}.
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
@Configuration
public class DOTLanguageConfiguration {

	public final static LanguageId LANGUAGEID = LanguageId.languageId("dot", "Dot Language");

	@Bean
	public AntlrFactory<DOTLexer, DOTParser> dotAntlrFactory() {
		return new AntlrFactory<DOTLexer, DOTParser>() {

			@Override
			public DOTParser createParser(TokenStream tokenStream) {
				return new DOTParser(tokenStream);
			}

			@Override
			public DOTLexer createLexer(CharStream input) {
				return new DOTLexer(input);
			}
		};
	}

	@Bean
	public AntlrParseService<Object> dotAntlrParseService() {
		return new DefaultAntlrParseService<>();
	}

	@Bean
	public DOTAntlrParseResultFunction dotAntlrParseResultFunction() {
		return new DOTAntlrParseResultFunction(dotAntlrFactory());
	}

	@Bean
	public DOTLanguageLinter dotLanguageLinter() {
		return new DOTLanguageLinter(dotAntlrParseService(),
				dotAntlrParseResultFunction());
	}
}
//end::snippet1[]
