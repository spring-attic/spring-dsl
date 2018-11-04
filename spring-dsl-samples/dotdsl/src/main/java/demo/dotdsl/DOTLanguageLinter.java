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

import java.util.function.Function;

import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.antlr.AntlrParseService;
import org.springframework.dsl.antlr.support.AbstractAntlrLinter;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.service.reconcile.Linter;

import reactor.core.publisher.Mono;

/**
 * {@link Linter} for {@code dot} language.
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class DOTLanguageLinter extends AbstractAntlrLinter<Object> {

	public DOTLanguageLinter(AntlrParseService<Object> service,
			Function<Document, Mono<? extends AntlrParseResult<Object>>> function) {
		super(DOTLanguageConfiguration.LANGUAGEID, service, function);
	}
}
//end::snippet1[]
