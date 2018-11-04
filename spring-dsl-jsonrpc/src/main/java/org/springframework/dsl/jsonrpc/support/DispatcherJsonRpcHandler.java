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
package org.springframework.dsl.jsonrpc.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.dsl.jsonrpc.JsonRpcHandler;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerAdapter;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerMapping;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResultHandler;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Central dispatcher for {@code JSONRCP Request} dispatching to
 * handlers/controllers. Dispatches to registered handlers for processing a
 * {@code JSONRCP} request, providing convenient mapping facilities.
 *
 * @author Janne Valkealahti
 *
 */
public class DispatcherJsonRpcHandler implements JsonRpcHandler, ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(DispatcherJsonRpcHandler.class);
	private static final Exception HANDLER_NOT_FOUND_EXCEPTION =
			new RuntimeException("No matching handler");

	private List<JsonRpcHandlerMapping> handlerMappings;
	private List<JsonRpcHandlerAdapter> handlerAdapters;
	private List<JsonRpcHandlerResultHandler> resultHandlers;

	public DispatcherJsonRpcHandler() {
	}

	public DispatcherJsonRpcHandler(ApplicationContext applicationContext) {
		initStrategies(applicationContext);
	}

	@Override
	public Mono<Void> handle(ServerJsonRpcExchange exchange) {
		log.trace("Handling exchange {}", exchange);
		return Flux.fromIterable(handlerMappings)
			.concatMap(mapping -> mapping.getHandler(exchange))
			.next()
			.switchIfEmpty(Mono.error(HANDLER_NOT_FOUND_EXCEPTION))
			.flatMap(handler -> invokeHandler(exchange, handler))
			.flatMap(result -> handleResult(exchange, result));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		initStrategies(applicationContext);
	}

	protected void initStrategies(ApplicationContext context) {
		Map<String, JsonRpcHandlerMapping> mappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, JsonRpcHandlerMapping.class, true, false);

		this.handlerMappings = new ArrayList<>(mappingBeans.values());
		AnnotationAwareOrderComparator.sort(this.handlerMappings);

		Map<String, JsonRpcHandlerAdapter> adapterBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, JsonRpcHandlerAdapter.class, true, false);

		this.handlerAdapters = new ArrayList<>(adapterBeans.values());
		AnnotationAwareOrderComparator.sort(this.handlerAdapters);

		Map<String, JsonRpcHandlerResultHandler> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, JsonRpcHandlerResultHandler.class, true, false);

		this.resultHandlers = new ArrayList<>(beans.values());
		AnnotationAwareOrderComparator.sort(this.resultHandlers);
	}


	private Mono<JsonRpcHandlerResult> invokeHandler(ServerJsonRpcExchange exchange, Object handler) {
		log.trace("invokeHandler {}", handler);
		for (JsonRpcHandlerAdapter handlerAdapter : this.handlerAdapters) {
			if (handlerAdapter.supports(handler)) {
				return handlerAdapter.handle(exchange, handler);
			}
		}
		return Mono.error(new IllegalStateException("No JsonRpcHandlerAdapter: " + handler));
	}

	private Mono<Void> handleResult(ServerJsonRpcExchange exchange, JsonRpcHandlerResult result) {
		return getResultHandler(result)
				.handleResult(exchange, result)
				.onErrorResume(ex -> result.applyExceptionHandler(ex)
						.flatMap(exceptionResult -> getResultHandler(exceptionResult)
								.handleResult(exchange, exceptionResult)));
	}

	private JsonRpcHandlerResultHandler getResultHandler(JsonRpcHandlerResult handlerResult) {
		log.debug("getResultHandler {}", handlerResult);
		for (JsonRpcHandlerResultHandler resultHandler : this.resultHandlers) {
			if (resultHandler.supports(handlerResult)) {
				return resultHandler;
			}
		}
		throw new IllegalStateException("No JsonRpcHandlerResultHandler for " + handlerResult.getReturnValue());
	}

}
