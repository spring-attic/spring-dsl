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
package org.springframework.dsl.lsp.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.domain.InitializeResult;
import org.springframework.dsl.domain.InitializedParams;
import org.springframework.dsl.domain.TextDocumentSyncKind;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession;
import org.springframework.dsl.lsp.LspSystemConstants;
import org.springframework.dsl.lsp.LspVersionDetector;
import org.springframework.dsl.lsp.LspVersionDetector.LspVersion;
import org.springframework.dsl.lsp.server.config.DslConfigurationProperties;
import org.springframework.dsl.lsp.server.jsonrpc.LspSessionState;
import org.springframework.dsl.lsp.server.support.LspExiter;
import org.springframework.dsl.service.DefaultDocumentStateTracker;
import org.springframework.dsl.service.DslServiceRegistry;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * A generic {@code JsonRpcController} implementation base root level features
 * what a {@code Language Server} should provide.
 *
 * @author Janne Valkealahti
 *
 */
@JsonRpcController
public class RootLanguageServerController {

	private static final Logger log = LoggerFactory.getLogger(RootLanguageServerController.class);
	private final DslServiceRegistry registry;
	private LspExiter lspExiter = LspExiter.NOOP_LSPEXITER;
	private DslConfigurationProperties properties;

	/**
	 * Instantiate a base language server controller.
	 *
	 * @param dslServiceRegistry the dsl service registry
	 * @param properties the properties
	 */
	public RootLanguageServerController(DslServiceRegistry dslServiceRegistry, DslConfigurationProperties properties) {
		this.registry = dslServiceRegistry;
		this.properties = properties;
	}

	/**
	 * Sets the lsp exiter.
	 *
	 * @param lspExiter the new lsp exiter
	 */
	@Autowired(required = false)
	public void setLspExiter(LspExiter lspExiter) {
		Assert.notNull(lspExiter, "lspExiter cannot be null");
		this.lspExiter = lspExiter;
	}

	@JsonRpcRequestMapping(method = "initialize")
	@JsonRpcResponseResult
	Mono<InitializeResult> initialize(InitializeParams params, JsonRpcSession session) {
		log.debug("initialize {}", params);
		LspVersion lspVersion = LspVersionDetector.detect(params);
		log.info("Negotiated lsp version to {}", lspVersion);
		// initialize is a first request from a lsp client, thus we return response having
		// capabilities and also create a session what further communication can use.
		return Mono.fromSupplier(() -> {
			return InitializeResult.initializeResult()
				.capabilities()
					.hoverProvider(!registry.getHoverers().isEmpty())
					.renameProvider(!registry.getRenamers().isEmpty())
					.documentSymbolProvider(!registry.getSymbolizers().isEmpty())
					.completionProvider(!registry.getCompletioners().isEmpty())
						.resolveProvider(false)
						.and()
					.textDocumentSyncKind(lspVersion.is2x() ? TextDocumentSyncKind.Incremental : null)
					.textDocumentSyncOptions(lspVersion.is3x())
						.openClose(true)
						// TODO: think how to use sync kind None
						.change(TextDocumentSyncKind.Incremental)
//						.change(documentStateTracker.isIncrementalChangesSupported() ? TextDocumentSyncKind.Incremental
//							: TextDocumentSyncKind.Full)
						.and()
					.and()
				.build();
		}).doOnSuccess(result -> {
			// TODO: just a conceptual tweak now to see how session is used
			session.getAttributes().put(LspSystemConstants.SESSION_ATTRIBUTE_LSP_SESSION_STATE, LspSessionState.CREATED);
			session.getAttributes().put(LspSystemConstants.SESSION_ATTRIBUTE_DOCUMENT_STATE_TRACKER,
					new DefaultDocumentStateTracker());
			session.getAttributes().put(LspSystemConstants.SESSION_ATTRIBUTE_LSP_VERSION, lspVersion);
		});
	}

	@JsonRpcRequestMapping(method = "initialized")
	@JsonRpcNotification
	public void initialized(InitializedParams params, JsonRpcSession session) {
		log.debug("initialized {}", params);
		session.getAttributes().put(LspSystemConstants.SESSION_ATTRIBUTE_LSP_SESSION_STATE, LspSessionState.INITIALIZED);
	}

	@JsonRpcRequestMapping(method = "shutdown")
	@JsonRpcResponseResult
	public Mono<Object> shutdown() {
		log.debug("shutdown");
		if (properties.getLsp().getServer().isForceJvmExitOnShutdown()) {
			return Mono.defer(() -> {
				lspExiter.exit(0);
				return Mono.empty();
			});
		}
		return Mono.empty();
	}

	@JsonRpcRequestMapping(method = "exit")
	@JsonRpcNotification
	public void exit() {
		log.debug("exit");
		lspExiter.exit(0);
	}
}
