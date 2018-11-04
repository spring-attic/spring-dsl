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

import org.springframework.dsl.jsonrpc.session.DefaultJsonRpcSessionManager;

public class MockServerJsonRpcExchange extends DefaultServerJsonRpcExchange {

	private MockServerJsonRpcExchange(MockJsonRpcInputMessage request) {
		super(request, new MockJsonRpcOutputMessage(), new DefaultJsonRpcSessionManager());
	}

	public static MockServerJsonRpcExchange from(MockJsonRpcInputMessage.Builder request) {
		return builder(request).build();
	}

	public static MockServerJsonRpcExchange.Builder builder(MockJsonRpcInputMessage request) {
		return new MockServerJsonRpcExchange.Builder(request);
	}

	public static MockServerJsonRpcExchange.Builder builder(MockJsonRpcInputMessage.Builder requestBuilder) {
		return new MockServerJsonRpcExchange.Builder(requestBuilder.build());
	}

	@Override
	public MockJsonRpcOutputMessage getResponse() {
		return (MockJsonRpcOutputMessage) super.getResponse();
	}

	public static class Builder {

		private final MockJsonRpcInputMessage request;

		public Builder(MockJsonRpcInputMessage request) {
			this.request = request;
		}

		public MockServerJsonRpcExchange build() {
			return new MockServerJsonRpcExchange(this.request);
		}
	}
}
