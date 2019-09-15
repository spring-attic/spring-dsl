/*
 * Copyright 2019 the original author or authors.
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
package org.springframework.dsl.lsp.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.domain.WorkspaceSymbolParams;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession;
import org.springframework.dsl.lsp.LspSystemConstants;
import org.springframework.dsl.service.DocumentStateTracker;
import org.springframework.dsl.service.DslContext;
import org.springframework.dsl.service.DslServiceRegistry;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@JsonRpcController
@JsonRpcRequestMapping(method = "workspace/")
public class WorkspaceLanguageServerController {

	private static final Logger log = LoggerFactory.getLogger(WorkspaceLanguageServerController.class);
	private final DslServiceRegistry registry;

	public WorkspaceLanguageServerController(DslServiceRegistry dslServiceRegistry) {
		Assert.notNull(dslServiceRegistry, "dslServiceRegistry must be set");
		this.registry = dslServiceRegistry;
	}

	@JsonRpcRequestMapping(method = "symbol")
	@JsonRpcResponseResult
	public Mono<List<SymbolInformation>> symbol(WorkspaceSymbolParams params, JsonRpcSession session) {
		log.debug("symbol {}", params);
		return Mono.defer(() -> {
			return Flux.fromIterable(getTracker(session).getDocuments())
				.flatMap(document -> {
					DslContext context = buildCommonDslContext(document, session);
					return Flux.fromIterable(registry.getSymbolizers(document.languageId()))
						.map(symbolizer -> symbolizer.symbolize(context, params.getQuery()))
						.flatMap(si -> si.symbolInformations());
				})
				.collectList();
		});
	}

	private static DslContext buildCommonDslContext(Document document, JsonRpcSession session) {
		return DslContext.builder()
			.document(document)
			.attribute(LspSystemConstants.CONTEXT_SESSION_ATTRIBUTE, session)
			.build();
	}

	private static DocumentStateTracker getTracker(JsonRpcSession session) {
		return session.getRequiredAttribute(LspSystemConstants.SESSION_ATTRIBUTE_DOCUMENT_STATE_TRACKER);
	}
}
