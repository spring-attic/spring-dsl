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
package demo.showcase;

import java.util.Arrays;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.Hover;
import org.springframework.dsl.domain.MarkupKind;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.AbstractDslService;
import org.springframework.dsl.service.Hoverer;

import reactor.core.publisher.Mono;

/**
 * {@link Hoverer} always returning "showcase" as {@linkplain MarkupKind#plaintext}.
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class ShowcaseHoverer extends AbstractDslService implements Hoverer {

	public ShowcaseHoverer() {
		super(Arrays.asList(LanguageId.ALL));
	}

	@Override
	public Mono<Hover> hover(Document document, Position position) {
		return Mono.defer(() ->
			Mono.just(Hover.hover()
				.contents()
					.kind(MarkupKind.plaintext)
					.value("showcase")
					.and()
				.build()));
	}
}
//end::snippet1[]
