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
package org.springframework.dsl.lsp.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.domain.MessageActionItem;
import org.springframework.dsl.domain.MessageParams;
import org.springframework.dsl.domain.ShowMessageRequestParams;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;

import reactor.core.publisher.Mono;

/**
 * A {@code JsonRpcController} implementation {@code window} features
 * what a {@code Language Client} should provide.
 *
 * @author Janne Valkealahti
 *
 */
@JsonRpcController
@JsonRpcRequestMapping(method = "window/")
public class WindowLanguageClientController {

	private static final Logger log = LoggerFactory.getLogger(WindowLanguageClientController.class);

	@JsonRpcRequestMapping(method = "showMessage")
	@JsonRpcNotification
	Mono<Void> showMessage(MessageParams messageParams) {
		log.debug("showMessage {}", messageParams);
		return Mono.empty();
	}

	@JsonRpcRequestMapping(method = "showMessageRequest")
	@JsonRpcResponseResult
	Mono<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		log.debug("showMessageRequest {}", requestParams);
		return Mono.empty();
	}

	@JsonRpcRequestMapping(method = "logMessage")
	@JsonRpcNotification
	Mono<Void> logMessage(MessageParams message) {
		log.debug("logMessage {}", message);
		return Mono.empty();
	}
}
