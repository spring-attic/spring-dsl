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

import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.dsl.jsonrpc.result.condition.JsonRcpRequestMethodsRequestCondition;
import org.springframework.dsl.jsonrpc.result.condition.JsonRpcRequestCondition;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class JsonRpcRequestMappingInfo implements JsonRpcRequestCondition<JsonRpcRequestMappingInfo> {

	@Nullable
	private final String name;

	private final JsonRcpRequestMethodsRequestCondition methodsCondition;

	/**
	 * Instantiates a new json rpc request mapping info.
	 *
	 * @param name the name
	 * @param methods the methods condition
	 */
	public JsonRpcRequestMappingInfo(@Nullable String name, JsonRcpRequestMethodsRequestCondition methods) {
		this.name = (StringUtils.hasText(name) ? name : null);
		this.methodsCondition = methods != null ? methods : new JsonRcpRequestMethodsRequestCondition();
	}

	@Override
	public JsonRpcRequestMappingInfo combine(JsonRpcRequestMappingInfo other) {
		JsonRcpRequestMethodsRequestCondition methods = methodsCondition.combine(other.methodsCondition);
		return new JsonRpcRequestMappingInfo(name, methods);
	}

	@Override
	public JsonRpcRequestMappingInfo getMatchingCondition(ServerJsonRpcExchange exchange) {
		JsonRcpRequestMethodsRequestCondition methods = methodsCondition.getMatchingCondition(exchange);
		if (methods == null) {
			return null;
		}
		return new JsonRpcRequestMappingInfo(name, methods);
	}

	@Override
	public int compareTo(JsonRpcRequestMappingInfo other, ServerJsonRpcExchange exchange) {
		int result = methodsCondition.compareTo(other.getMethodsCondition(), exchange);
		if (result != 0) {
			return result;
		}
		return 0;
	}

	/**
	 * Return the name for this mapping, or {@code null}.
	 *
	 * @return the name for this mapping
	 */
	@Nullable
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the JSONRPC request methods of this {@link JsonRpcRequestMappingInfo};
	 * or instance with 0 request methods, never {@code null}.
	 *
	 * @return the methods condition
	 */
	public JsonRcpRequestMethodsRequestCondition getMethodsCondition() {
		return methodsCondition;
	}

	public static Builder builder() {
		return new DefaultBuilder();
	}

	/**
	 * Defines a builder for creating a {@link JsonRpcRequestMappingInfo}.
	 */
	public interface Builder {

		/**
		 * Set the json rpc request method conditions.
		 *
		 * @param methods the methods
		 * @return the builder for chaining
		 */
		Builder methods(String... methods);

		/**
		 * Set the mapping name.
		 *
		 * @param name the mapping name
		 * @return the builder for chaining
		 */
		Builder mappingName(String name);

		/**
		 * Builds the {@link JsonRpcRequestMappingInfo}.
		 *
		 * @return the json rpc request mapping info
		 */
		JsonRpcRequestMappingInfo build();
	}

	private static class DefaultBuilder implements Builder {

		@Nullable
		private String[] methods;

		@Nullable
		private String mappingName;

		@Override
		public Builder methods(String... methods) {
			this.methods = methods;
			return this;
		}

		@Override
		public Builder mappingName(String name) {
			this.mappingName = name;
			return this;
		}

		@Override
		public JsonRpcRequestMappingInfo build() {
			return new JsonRpcRequestMappingInfo(mappingName, new JsonRcpRequestMethodsRequestCondition(methods));
		}
	}
}
