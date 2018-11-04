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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerMapping;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.result.condition.JsonRpcRequestCondition;
import org.springframework.dsl.jsonrpc.result.method.AbstractHandlerMethodMapping;
import org.springframework.dsl.jsonrpc.result.method.HandlerMethod;
import org.springframework.dsl.jsonrpc.result.method.JsonRpcRequestMappingInfo;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * Implementation of {@link JsonRpcHandlerMapping} that creates
 * {@link JsonRpcRequestMappingInfo} instances from class-level and method-level
 * {@link JsonRpcRequestMapping @JsonRpcRequestMapping} annotations
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcRequestMappingHandlerMapping extends AbstractHandlerMethodMapping<JsonRpcRequestMappingInfo>
		implements EmbeddedValueResolverAware {

	@Nullable
	private StringValueResolver embeddedValueResolver;

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return (AnnotatedElementUtils.hasAnnotation(beanType, JsonRpcController.class) ||
				AnnotatedElementUtils.hasAnnotation(beanType, JsonRpcRequestMapping.class));
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	@Override
	protected HandlerMethod handleNoMatch(Set<JsonRpcRequestMappingInfo> mappings, ServerJsonRpcExchange exchange)
			throws Exception {
		return null;
	}

	@Override
	protected Comparator<JsonRpcRequestMappingInfo> getMappingComparator(ServerJsonRpcExchange exchange) {
		return (info1, info2) -> info1.compareTo(info2, exchange);
	}

	@Override
	protected JsonRpcRequestMappingInfo getMatchingMapping(JsonRpcRequestMappingInfo info,
			ServerJsonRpcExchange exchange) {
		return info.getMatchingCondition(exchange);
	}

	@Override
	protected JsonRpcRequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		JsonRpcRequestMappingInfo info = createRequestMappingInfo(method);
		if (info != null) {
			JsonRpcRequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
			if (typeInfo != null) {
				info = typeInfo.combine(info);
			}
		}
		return info;
	}

	/**
	 * Resolve placeholder values in the given array of patterns.
	 *
	 * @param patterns the patterns
	 * @return a new array with updated patterns
	 */
	protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
		if (this.embeddedValueResolver == null) {
			return patterns;
		} else {
			String[] resolvedPatterns = new String[patterns.length];
			for (int i = 0; i < patterns.length; i++) {
				resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
			}
			return resolvedPatterns;
		}
	}

	protected JsonRpcRequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		return null;
	}

	protected JsonRpcRequestCondition<?> getCustomMethodCondition(Method method) {
		return null;
	}

	private JsonRpcRequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
		JsonRpcRequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element,
				JsonRpcRequestMapping.class);
		JsonRpcRequestCondition<?> condition = (element instanceof Class ? getCustomTypeCondition((Class<?>) element)
				: getCustomMethodCondition((Method) element));
		return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
	}

	private JsonRpcRequestMappingInfo createRequestMappingInfo(JsonRpcRequestMapping requestMapping,
			JsonRpcRequestCondition<?> customCondition) {
		JsonRpcRequestMappingInfo.Builder builder = JsonRpcRequestMappingInfo
				.builder()
				.methods(requestMapping.method())
				.mappingName(requestMapping.name());

		if (customCondition != null) {
			// TODO: implement
//			builder.cu
		}

		return builder.build();
	}
}
