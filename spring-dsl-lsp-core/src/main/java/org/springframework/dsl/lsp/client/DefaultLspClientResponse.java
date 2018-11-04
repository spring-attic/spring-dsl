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
package org.springframework.dsl.lsp.client;

import org.springframework.core.ResolvableType;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractor;
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractorStrategies;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

public class DefaultLspClientResponse implements LspClientResponse {

	private final JsonRpcResponse response;
	private final JsonRpcExtractorStrategies strategies;

	public DefaultLspClientResponse(JsonRpcResponse response, JsonRpcExtractorStrategies strategies) {
		this.response = response;
		this.strategies = strategies;
	}

	@Override
	public JsonRpcResponse response() {
		return response;
	}

	@Override
	public JsonRpcExtractorStrategies strategies() {
		return strategies;
	}

	@Override
	public <T> Mono<T> resultToMono(Class<? extends T> elementClass) {
		if (Void.class.isAssignableFrom(elementClass)) {
			return consumeAndCancel();
		} else {
			return result(toMono(elementClass));
		}
	}

	@Override
	public <T> T result(JsonRpcExtractor<T, JsonRpcResponse> extractor) {
		return extractor.extract(response, new JsonRpcExtractor.Context() {

			@Override
			public ObjectMapper objectMapper() {
				return strategies.objectMapper();
			}
		});
	}

	private <T> JsonRpcExtractor<Mono<T>, JsonRpcResponse> toMono(Class<? extends T> elementClass) {
		return toMono(ResolvableType.forClass(elementClass));
	}

	@SuppressWarnings("unchecked")
	private <T> JsonRpcExtractor<Mono<T>, JsonRpcResponse> toMono(ResolvableType elementType) {
		return (response, context) -> {
			try {
				Object jsonRpcResponse = null;
				if (elementType.isAssignableFrom(String.class)) {
					jsonRpcResponse = response.getResult();
				} else {
					jsonRpcResponse = context.objectMapper().readValue(response.getResult(), elementType.resolve());
				}
				return Mono.justOrEmpty((T)jsonRpcResponse);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	private <T> Mono<T> consumeAndCancel() {
		return Mono.empty();
//		return (Mono<T>) this.response.getBody()
//				.map(buffer -> {
//					DataBufferUtils.release(buffer);
//					throw new ReadCancellationException();
//				})
//				.onErrorResume(ReadCancellationException.class, ex -> Mono.empty())
//				.then();
	}
}
