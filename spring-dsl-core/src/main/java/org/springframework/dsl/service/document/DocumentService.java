/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.service.document;

/**
 * Interface providing service handling document uris for get and save
 * operations.
 *
 * @author Janne Valkealahti
 *
 */
public interface DocumentService {

	/**
	 * Gets the content of a document.
	 *
	 * @param uri the uri
	 * @return the content
	 */
	String get(String uri);

	/**
	 * Save the content of a document.
	 *
	 * @param uri the uri
	 * @param document the document
	 */
	void save(String uri, String document);
}
