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
package demo.simpledsl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.service.symbol.Symbolizer;

import demo.simpledsl.SimpleLanguage.Line;
import reactor.core.publisher.Flux;

/**
 * A {@link Symbolizer} implementation for a {@code simple} sample language.
 *
 * @author Janne Valkealahti
 * @see EnableSimpleLanguage
 *
 */
//tag::snippet1[]
public class SimpleLanguageSymbolizer extends SimpleLanguageDslService implements Symbolizer {

	@Override
	public Flux<DocumentSymbol> symbolize(Document document) {
		return Flux.defer(() -> {
			List<DocumentSymbol> symbols = new ArrayList<>();
			for (Line line : SimpleLanguage.build(document).getLines()) {
				symbols.add(DocumentSymbol.documentSymbol()
					.name(line.getKeyToken().getType().toString())
					.kind(SymbolKind.String)
					.range(line.getKeyToken().getRange())
					.selectionRange(line.getKeyToken().getRange())
					.build());
			}
			return Flux.fromIterable(symbols);
		});
	}
}
//end::snippet1[]
