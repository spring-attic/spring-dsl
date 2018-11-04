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
package org.springframework.dsl.jsonrpc.result.method.annotation;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResultHandler;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.codec.JsonRpcMessageWriter;

import reactor.core.publisher.Mono;

/**
 * {@link JsonRpcHandlerResultHandler} which handles result from a methods
 * annotated with {@link JsonRpcNotification @JsonRpcNotification}.
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcNotificationResultHandler extends AbstractMessageWriterResultHandler
		implements JsonRpcHandlerResultHandler {

	public JsonRpcNotificationResultHandler(List<JsonRpcMessageWriter<?>> messageWriters,
			ReactiveAdapterRegistry adapterRegistry) {
		super(messageWriters, adapterRegistry);
		setOrder(100);
	}

	@Override
	public boolean supports(JsonRpcHandlerResult result) {
		MethodParameter parameter = result.getReturnTypeSource();
		Class<?> containingClass = parameter.getContainingClass();
		return (AnnotationUtils.findAnnotation(containingClass, JsonRpcNotification.class) != null ||
				parameter.getMethodAnnotation(JsonRpcNotification.class) != null);
	}

	@Override
	public Mono<Void> handleResult(ServerJsonRpcExchange exchange, JsonRpcHandlerResult result) {
		Object body = result.getReturnValue();
		MethodParameter bodyTypeParameter = result.getReturnTypeSource();
		return writeBody(body, bodyTypeParameter, exchange);
	}
}
