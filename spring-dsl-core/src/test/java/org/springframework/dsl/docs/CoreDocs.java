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

import java.util.List;

import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.DslService;

@SuppressWarnings("unused")
public class CoreDocs {

	public void languageIds() {
// tag::snippet1[]
		LanguageId myLanguageId1 = LanguageId.languageId("mylanguage1", "My Language1 Description");
// end::snippet1[]
// tag::snippet3[]
		LanguageId myLanguageId2 = LanguageId.languageId("mylanguage2", "My Language2 Description",
				new String[] { "mylanguage1" });
		// myLanguageId2 is compatible with myLanguageId1
		myLanguageId2.isCompatibleWith(myLanguageId1);
// end::snippet3[]
	}

	interface DocsDslService extends DslService {

		@Override
// tag::snippet2[]
		List<LanguageId> getSupportedLanguageIds();
// end::snippet2[]
	}

}
