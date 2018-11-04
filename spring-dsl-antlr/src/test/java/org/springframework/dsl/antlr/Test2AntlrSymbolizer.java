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

import java.util.function.Function;

import org.springframework.dsl.antlr.support.AbstractAntlrSymbolizer;
import org.springframework.dsl.document.Document;

import reactor.core.publisher.Mono;

public class Test2AntlrSymbolizer extends AbstractAntlrSymbolizer<Object> {

	public Test2AntlrSymbolizer(AntlrParseService<Object> antlrParseService,
			Function<Document, Mono<? extends AntlrParseResult<Object>>> antlrParseResultSupplier) {
		super(TestAntrlUtils.TEST2_LANGUAGE_ID, antlrParseService, antlrParseResultSupplier);
	}
}
