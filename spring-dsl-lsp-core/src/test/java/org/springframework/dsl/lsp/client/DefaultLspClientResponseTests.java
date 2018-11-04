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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.jsonrpc.codec.JsonRpcExtractorStrategies;
import org.springframework.dsl.jsonrpc.support.DefaultJsonRpcResponse;

import reactor.core.publisher.Mono;

public class DefaultLspClientResponseTests {

	@Test
	public void test1() {
		DefaultJsonRpcResponse response = new DefaultJsonRpcResponse("", "", "hi", "");
		JsonRpcExtractorStrategies strategies = JsonRpcExtractorStrategies.withDefaults();
		DefaultLspClientResponse clientResponse = new DefaultLspClientResponse(response, strategies);
		Mono<String> resultMono = clientResponse.resultToMono(String.class);
		String result = resultMono.block();
		assertThat(result).isEqualTo("hi");
	}

	@Test
	public void test2() {
		Position position = Position.from(1, 1);
		String json = "{\"line\":1,\"character\":1}";
		DefaultJsonRpcResponse response = new DefaultJsonRpcResponse("", "", json, "");
		JsonRpcExtractorStrategies strategies = JsonRpcExtractorStrategies.withDefaults();
		DefaultLspClientResponse clientResponse = new DefaultLspClientResponse(response, strategies);
		Mono<Position> resultMono = clientResponse.resultToMono(Position.class);
		Position result = resultMono.block();
		assertThat(result).isEqualTo(position);
	}

	public void test3() {
		LspClientResponse.create().build();
	}
}
