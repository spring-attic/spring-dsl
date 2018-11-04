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
package org.springframework.dsl.docs;

import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;

@SuppressWarnings("unused")
public class DomainClassesDocs {

	public void position() {
// tag::snippet1[]
		Position position = Position.position()
			.line(1)
			.character(1)
			.build();
		
		position = Position.from(1, 1);
// end::snippet1[]		
	}
	
	public void rangeAndPosition() {
// tag::snippet2[]
		Range range = Range.range()
			.start()
				.line(1)
				.character(1)
				.and()
			.end()
				.line(2)
				.character(2)
				.and()
			.build();
		
		range = Range.from(1, 1, 2, 2);
// end::snippet2[]				
	}
}
