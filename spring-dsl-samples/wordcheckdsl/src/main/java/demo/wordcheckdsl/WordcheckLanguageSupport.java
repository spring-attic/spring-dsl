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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.AbstractDslService;

/**
 * Base class for all {@code wordcheck} language services.
 *
 * @author Janne Valkealahti
 * @author Kris De Volder
 *
 */
public abstract class WordcheckLanguageSupport extends AbstractDslService {

	private final static LanguageId LANGUAGEID = LanguageId.languageId("wordcheck", "Wordcheck Language");
	private WordcheckProperties properties = new WordcheckProperties();

	public WordcheckLanguageSupport() {
		super(Arrays.asList(LANGUAGEID));
	}

	public WordcheckProperties getProperties() {
		return properties;
	}

	@Autowired
	public void setProperties(WordcheckProperties properties) {
		this.properties = properties;
	}
}
