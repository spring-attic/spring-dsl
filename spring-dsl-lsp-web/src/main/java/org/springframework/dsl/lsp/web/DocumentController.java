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
package org.springframework.dsl.lsp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.service.document.DocumentService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing integration into a {@link DocumentService}.
 *
 * @author Janne Valkealahti
 *
 */
@RestController
@RequestMapping("${spring.dsl.lsp.web.document-services.base-path:/document}")
public class DocumentController {

	private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
	private DocumentService documentService;

	/**
	 * Instantiates a new document controller.
	 *
	 * @param documentService the document service
	 */
	public DocumentController(DocumentService documentService) {
		Assert.notNull(documentService, "documentService must be set");
		this.documentService = documentService;
	}

	@GetMapping
    public String getDocument(@RequestParam("uri") String uri) {
		log.debug("Get request for document {}", uri);
		return this.documentService.get(uri);
    }

	@PostMapping
    public void saveDocument(@RequestParam("uri") String uri, @RequestBody String document) {
		log.debug("Save request for document {}, with content {}", uri, document);
		this.documentService.save(uri, document);
    }
}
