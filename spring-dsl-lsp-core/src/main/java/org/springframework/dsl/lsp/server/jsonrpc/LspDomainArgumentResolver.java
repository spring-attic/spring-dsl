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
package org.springframework.dsl.lsp.server.jsonrpc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dsl.domain.CompletionParams;
import org.springframework.dsl.domain.DidChangeTextDocumentParams;
import org.springframework.dsl.domain.DidCloseTextDocumentParams;
import org.springframework.dsl.domain.DidOpenTextDocumentParams;
import org.springframework.dsl.domain.DidSaveTextDocumentParams;
import org.springframework.dsl.domain.DocumentSymbolParams;
import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.domain.InitializedParams;
import org.springframework.dsl.domain.RenameParams;
import org.springframework.dsl.domain.TextDocumentPositionParams;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcHandlerMethodArgumentResolver;
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
 * {@code LSP} domain objects based on a {@code params} in a {@code message}.
 * <p>
 * NOTE: We fully control this class and existing domain classes in a
 * {@code LSP} space so we can blindly resolve and deserialize those.
 *
 * @author Janne Valkealahti
 *
 */
public class LspDomainArgumentResolver implements JsonRpcHandlerMethodArgumentResolver {

	private Set<Class<?>> supportedClasses = Arrays.asList(
			InitializeParams.class,
			InitializedParams.class,
			DidChangeTextDocumentParams.class,
			DidCloseTextDocumentParams.class,
			DidOpenTextDocumentParams.class,
			DidSaveTextDocumentParams.class,
			CompletionParams.class,
			DocumentSymbolParams.class,
			TextDocumentPositionParams.class,
			RenameParams.class
			).stream().collect(Collectors.toSet());

	/**
	 * Instantiates a new lsp domain argument resolver.
	 */
	public LspDomainArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		return supportedClasses.contains(type);
	}

	@Override
	public Mono<Object> resolveArgument(MethodParameter parameter, ServerJsonRpcExchange exchange) {
		Class<?> type = parameter.getParameterType();
		Class<?> contextClass = (parameter != null ? parameter.getContainingClass() : null);

//		Flux<DataBuffer> body = exchange.getRequest().getBody();
//		Mono<String> bodyAsString = getBodyAsString(body);

		Mono<String> bodyAsString = exchange.getRequest().getParams();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		ObjectWriter writer = objectMapper.writer();
//		ObjectReader reader = objectMapper.reader();
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		JavaType javaType = typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
		ObjectReader forType = objectMapper.readerFor(javaType);
		try {
			return Mono.just(forType.readValue(bodyAsString.block().toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Mono.error(e);
		}
//		return Mono.just(BeanUtils.instantiateClass(type));
//		Object body = null;
//		if (exchange.getRequest() != null) {
//		}
//		Class<?> clazz = parameter.getParameterType();
//		return Mono.just(conversionService.convert(body, clazz));
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


//	private Flux<TokenBuffer> tokenize(Publisher<DataBuffer> input, boolean tokenizeArrayElements) {
//		Flux<DataBuffer> inputFlux = Flux.from(input);
//		JsonFactory factory = getObjectMapper().getFactory();
//		return Jackson2Tokenizer.tokenize(inputFlux, factory, tokenizeArrayElements);
//	}

}
