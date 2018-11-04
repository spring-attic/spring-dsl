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

import java.util.List;

import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.service.reconcile.Reconciler;
import org.springframework.dsl.service.symbol.Symbolizer;

/**
 * A generic registry for services which can be requested by a {@link LanguageId}.
 *
 * @author Janne Valkealahti
 *
 */
public interface DslServiceRegistry {

	/**
	 * Gets all completioners.
	 *
	 * @return the completioners
	 */
	List<Completioner> getCompletioners();

	/**
	 * Gets all completioners for a language id.
	 *
	 * @param languageId the language id
	 * @return the {@link Completioner}s
	 */
	List<Completioner> getCompletioners(LanguageId languageId);

	/**
	 * Gets the hoverers.
	 *
	 * @return the hoverers
	 */
	List<Hoverer> getHoverers();

	/**
	 * Gets the hoverers for a language id.
	 *
	 * @param languageId the language id
	 * @return the hoverers
	 */
	List<Hoverer> getHoverers(LanguageId languageId);

	/**
	 * Gets the symbolizers.
	 *
	 * @return the symbolizers
	 */
	List<Symbolizer> getSymbolizers();

	/**
	 * Gets the symbolizers for a language id.
	 *
	 * @param languageId the language id
	 * @return the symbolizers
	 */
	List<Symbolizer> getSymbolizers(LanguageId languageId);

	/**
	 * Gets the reconcilers.
	 *
	 * @return the reconcilers
	 */
	List<Reconciler> getReconcilers();

	/**
	 * Gets the reconcilers for a language id.
	 *
	 * @param languageId the language id
	 * @return the reconcilers
	 */
	List<Reconciler> getReconcilers(LanguageId languageId);

	/**
	 * Gets the renamers.
	 *
	 * @return the renamers
	 */
	List<Renamer> getRenamers();

	/**
	 * Gets the renamers for a language id.
	 *
	 * @param languageId the language id
	 * @return the renamers
	 */
	List<Renamer> getRenamers(LanguageId languageId);
}
