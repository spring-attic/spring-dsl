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
package org.springframework.dsl.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.dsl.model.LanguageId;
import org.springframework.util.Assert;

/**
 * Base implementation of a {@link DslService}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractDslService implements DslService {

	private final List<LanguageId> languageIds;

	/**
	 * Instantiates a new abstract dsl service.
	 *
	 * @param languageId the language id
	 */
	public AbstractDslService(LanguageId languageId) {
		Assert.notNull(languageId, "languageId cannot be null");
		this.languageIds = Arrays.asList(languageId);
	}

	/**
	 * Instantiates a new abstract dsl service.
	 *
	 * @param languageIds the language ids
	 */
	public AbstractDslService(List<LanguageId> languageIds) {
		Assert.notNull(languageIds, "languageIds list cannot be null");
		this.languageIds = languageIds;
	}

	@Override
	public List<LanguageId> getSupportedLanguageIds() {
		return languageIds;
	}
}
