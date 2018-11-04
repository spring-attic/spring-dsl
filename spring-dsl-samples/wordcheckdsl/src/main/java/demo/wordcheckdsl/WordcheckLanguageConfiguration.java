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
package demo.wordcheckdsl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.service.Completioner;
import org.springframework.dsl.service.Renamer;
import org.springframework.dsl.service.reconcile.Linter;
import org.springframework.dsl.service.symbol.Symbolizer;

/**
 * Configuration for a {@code wordcheck} sample language supporting
 * {@link Completioner}, {@link Symbolizer}, {@link Renamer} and {@link Linter}.
 *
 * @author Janne Valkealahti
 * @author Kris De Volder
 * @see EnableWordcheckLanguage
 *
 */
//tag::snippet1[]
@Configuration
public class WordcheckLanguageConfiguration {

	@Bean
	public Completioner wordcheckLanguageCompletioner() {
		return new WordcheckLanguageCompletioner();
	}

	@Bean
	public Linter wordcheckLanguageLinter() {
		return new WordcheckLanguageLinter();
	}

	@Bean
	public Symbolizer wordcheckLanguageSymbolizer() {
		return new WordcheckLanguageSymbolizer();
	}

	@Bean
	public Renamer wordcheckLanguageRenamer() {
		return new WordcheckLanguageRenamer(wordcheckLanguageSymbolizer());
	}
}
//end::snippet1[]
