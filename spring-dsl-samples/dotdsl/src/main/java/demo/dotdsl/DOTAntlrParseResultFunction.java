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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.springframework.dsl.antlr.AntlrFactory;
import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.antlr.support.AbstractAntlrErrorListener;
import org.springframework.dsl.antlr.support.AbstractAntlrParseResultFunction;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.service.reconcile.ReconcileProblem;

import reactor.core.publisher.Mono;

/**
 * {@link AntlrParseResult} function for {@code dot} language.
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class DOTAntlrParseResultFunction
		extends AbstractAntlrParseResultFunction<Object, DOTLexer, DOTParser> {

	public DOTAntlrParseResultFunction(AntlrFactory<DOTLexer, DOTParser> antlrFactory) {
		super(antlrFactory);
	}

	@Override
	public Mono<? extends AntlrParseResult<Object>> apply(Document document) {
		return Mono.defer(() -> {
			List<ReconcileProblem> errors = new ArrayList<>();
			DOTParser parser = getParser(CharStreams.fromString(document.content()));
			parser.removeErrorListeners();
			parser.addErrorListener(new DOTErrorListener(errors));
			parser.graph();
			return Mono.just(AntlrParseResult.from(errors));
		});
	}

	private static class DOTErrorListener extends AbstractAntlrErrorListener {

		public DOTErrorListener(List<ReconcileProblem> errors) {
			super(errors);
		}
	}
}
//end::snippet1[]
