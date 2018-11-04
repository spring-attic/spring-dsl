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
package org.springframework.dsl.jsonrpc.result.method;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestParams;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A {@link JsonRpcHandlerMethodArgumentResolver} implementation resolving
 * resolving generic classes annotated with {@link JsonRpcRequestParams} by
 * mapping a {@code params} from a {@code message}.
 * 
 * @author Janne Valkealahti
 *
 */
public class JsonRpcRequestParamsArgumentResolver implements JsonRpcHandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JsonRpcRequestParams.class);
	}

	@Override
	public Mono<Object> resolveArgument(MethodParameter parameter, ServerJsonRpcExchange exchange) {
		Class<?> type = parameter.getParameterType();
		Class<?> contextClass = (parameter != null ? parameter.getContainingClass() : null);

		Mono<String> bodyAsString = exchange.getRequest().getParams();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		JavaType javaType = typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
		ObjectReader forType = objectMapper.readerFor(javaType);
		try {
			return Mono.just(forType.readValue(bodyAsString.block().toString()));
		} catch (IOException e) {
			return Mono.error(e);
		}
	}

	public Mono<String> getBodyAsString(Flux<DataBuffer> body) {
		Charset charset = Charset.defaultCharset();
		DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
		return Flux.from(body)
				.reduce(bufferFactory.allocateBuffer(), (previous, current) -> {
					previous.write(current);
					DataBufferUtils.release(current);
					return previous;
				})
				.map(buffer -> dumpString(buffer, charset));
	}

	private static String dumpString(DataBuffer buffer, Charset charset) {
		Assert.notNull(charset, "'charset' must not be null");
		byte[] bytes = new byte[buffer.readableByteCount()];
		buffer.read(bytes);
		return new String(bytes, charset);
	}

}
