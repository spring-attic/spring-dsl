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

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.TextEdit;
import org.springframework.dsl.domain.WorkspaceEdit;
import org.springframework.dsl.service.Renamer;
import org.springframework.dsl.service.symbol.Symbolizer;
import org.springframework.dsl.support.DslUtils;
import org.springframework.util.ObjectUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A {@link Renamer} implementation for a {@code wordcheck} sample
 * language.
 *
 * @author Janne Valkealahti
 * @see EnableWordcheckLanguage
 *
 */
// TODO: tried to use share() as returning flux from symbolizer.symbolize,
//       but it broke things up, has to be a bug in reactor
//tag::snippet1[]
public class WordcheckLanguageRenamer extends WordcheckLanguageSupport implements Renamer {

	private final Symbolizer symbolizer;

	public WordcheckLanguageRenamer(Symbolizer symbolizer) {
		this.symbolizer = symbolizer;
	}

	@Override
	public Mono<WorkspaceEdit> rename(Document document, Position position, String newName) {
		Flux<DocumentSymbol> symbols = symbolizer.symbolize(document);
		Mono<DocumentSymbol> symbol = symbols
			.filter(s -> DslUtils.isPositionInRange(position, s.getRange())).next();
		return symbols
			.filterWhen(s -> symbol.map(sym -> ObjectUtils.nullSafeEquals(s.getName(), sym.getName())))
			.map(s -> TextEdit.textEdit()
				.newText(newName)
				.range(s.getRange())
				.build())
			.collectList()
			.map(list -> {
				return WorkspaceEdit.workspaceEdit()
					.changes(document.uri(), list)
					.build();
			});
	}
}
//end::snippet1[]
