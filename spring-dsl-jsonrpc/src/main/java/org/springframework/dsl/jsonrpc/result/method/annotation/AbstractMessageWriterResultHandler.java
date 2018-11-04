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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.codec.JsonRpcMessageWriter;
import org.springframework.dsl.jsonrpc.result.HandlerResultHandlerSupport;
import org.springframework.lang.Nullable;

import reactor.core.publisher.Mono;

/**
 * Abstract base class for result handlers that handle return values by writing
 * to the response with {@link JsonRpcMessageWriter}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractMessageWriterResultHandler extends HandlerResultHandlerSupport {

	private static final Logger log = LoggerFactory.getLogger(AbstractMessageWriterResultHandler.class);
	private final List<JsonRpcMessageWriter<?>> messageWriters;

	protected AbstractMessageWriterResultHandler(List<JsonRpcMessageWriter<?>> messageWriters,
			ReactiveAdapterRegistry adapterRegistry) {
		super(adapterRegistry);
		this.messageWriters = messageWriters;
	}

	protected Mono<Void> writeBody(@Nullable Object body, MethodParameter bodyParameter,
			ServerJsonRpcExchange exchange) {
		return this.writeBody(body, bodyParameter, null, exchange);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Mono<Void> writeBody(@Nullable Object body, MethodParameter bodyParameter,
			@Nullable MethodParameter actualParameter, ServerJsonRpcExchange exchange) {

		ResolvableType bodyType = ResolvableType.forMethodParameter(bodyParameter);
		ResolvableType actualType = (actualParameter == null ?
				bodyType : ResolvableType.forMethodParameter(actualParameter));
		Class<?> bodyClass = bodyType.resolve();
		ReactiveAdapter adapter = getAdapterRegistry().getAdapter(bodyClass, body);

		Publisher<?> publisher;
		ResolvableType elementType;
		if (adapter != null) {
			publisher = adapter.toPublisher(body);
			ResolvableType genericType = bodyType.getGeneric(0);
			elementType = getElementType(adapter, genericType);
		} else {
			publisher = Mono.justOrEmpty(body);
			elementType = ((bodyClass == null || bodyClass.equals(Object.class)) && body != null ?
					ResolvableType.forInstance(body) : bodyType);
		}

		if (void.class == elementType.getRawClass() || Void.class == elementType.getRawClass()) {
			return Mono.from((Publisher<Void>) publisher);
		}

		JsonRpcInputMessage request = exchange.getRequest();
		JsonRpcOutputMessage response = exchange.getResponse();

		log.debug("request id {} method {}", request.getId(), request.getMethod());

		Map<String, Object> hints = new HashMap<>();
		JsonRpcNotification notification = bodyParameter.getMethodAnnotation(JsonRpcNotification.class);
		if (notification != null) {
			hints.put("method", AnnotationUtils.getValue(notification, "method"));
		}

		for (JsonRpcMessageWriter<?> writer : messageWriters) {
			if (writer.canWrite(elementType)) {
//				return writer.write((Publisher) publisher, elementType, request, response, Collections.emptyMap());
				return writer.write((Publisher) publisher, elementType, request, response, hints);
			}
		}
		return Mono.error(new IllegalStateException("No writer for : " + elementType));
	}

	private ResolvableType getElementType(ReactiveAdapter adapter, ResolvableType genericType) {
		if (adapter.isNoValue()) {
			return ResolvableType.forClass(Void.class);
		}
		else if (genericType != ResolvableType.NONE) {
			return genericType;
		}
		else {
			return ResolvableType.forClass(Object.class);
		}
	}
}
