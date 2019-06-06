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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a {@link DocumentService} keeping everything
 * in-memory, effectively starting with empty repository.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultDocumentService implements DocumentService {

	// TODO: this service is work in progress as default implementation should
	//       provided hooks for user to provided their own ways to support
	//       getting/saving uri's per its content.

	private static final Logger log = LoggerFactory.getLogger(DefaultDocumentService.class);
	private final Map<String, String> repository = new HashMap<>();

	@Override
	public String get(String uri) {
		log.debug("Get request for document for uri {}", uri);
		return repository.getOrDefault(uri, "");
	}

	@Override
	public void save(String uri, String document) {
		log.debug("Save request for document for uri {} with content {}", uri, document);
		repository.put(uri, document);
	}
}
