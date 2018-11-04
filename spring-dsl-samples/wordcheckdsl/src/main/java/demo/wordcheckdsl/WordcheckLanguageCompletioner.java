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
import org.springframework.dsl.document.DocumentRegion;
import org.springframework.dsl.domain.CompletionItem;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.service.Completioner;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A {@link Completioner} implementation for a {@code wordcheck} sample
 * language.
 *
 * @author Janne Valkealahti
 * @author Kris De Volder
 * @see EnableWordcheckLanguage
 *
 */
//tag::snippet1[]
public class WordcheckLanguageCompletioner extends WordcheckLanguageSupport implements Completioner {

	@Override
	public Flux<CompletionItem> complete(Document document, Position position) {
		return Flux.defer(() -> {
			Position start = Position.from(document.validatePosition(position));
			while (document.positionInBounds(start) && Character.isLetter(document.charAtPosition(start))
					&& start.getCharacter() > 0) {
				start.setCharacter(start.getCharacter() - 1);
			}
			DocumentRegion region = new DocumentRegion(document, Range.from(start, position));
			DocumentRegion trimmed = region.trim();

			return Flux.fromIterable(getProperties().getWords())
				.flatMap(word -> {
					if (FuzzyMatcher.matchScore(trimmed, word) != 0.0) {
						return Mono.just(CompletionItem.completionItem().label(word).build());
					}
					return Mono.empty();
				});
			});
	}
}
//end::snippet1[]
