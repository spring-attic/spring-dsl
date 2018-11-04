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

import java.util.regex.Pattern;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.DocumentRegion;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.SymbolKind;
import org.springframework.dsl.service.symbol.Symbolizer;

import reactor.core.publisher.Flux;

/**
 * A {@link Symbolizer} implementation for a {@code wordcheck} sample language.
 *
 * @author Janne Valkealahti
 * @see EnableWordcheckLanguage
 *
 */
public class WordcheckLanguageSymbolizer extends WordcheckLanguageSupport implements Symbolizer {

	private static final Pattern SPACE = Pattern.compile("[^\\w]+");

	@Override
	public Flux<DocumentSymbol> symbolize(Document document) {
		return Flux.defer(() -> {
			return Flux.fromArray(new DocumentRegion(document).split(SPACE))
				.filter(w -> w.length() > 0)
				.map(r -> DocumentSymbol.documentSymbol()
					.name(r.toString())
					.kind(SymbolKind.String)
					.range(r.toRange())
					.selectionRange(r.toRange())
					.build());
		});
	}
}
