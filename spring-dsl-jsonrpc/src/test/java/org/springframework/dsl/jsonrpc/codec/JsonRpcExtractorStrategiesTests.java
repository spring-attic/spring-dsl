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
package org.springframework.dsl.jsonrpc.codec;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcExtractorStrategiesTests {

	@Test
	public void test1() {
		JsonRpcExtractorStrategies strategies = JsonRpcExtractorStrategies.builder().build();
		ObjectMapper objectMapper = strategies.objectMapper();
		assertThat(objectMapper).isNotNull();
		assertThat(objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion())
				.isEqualTo(JsonInclude.Include.USE_DEFAULTS);
	}

	@Test
	public void test2() {
		JsonRpcExtractorStrategies strategies = JsonRpcExtractorStrategies.withDefaults();
		ObjectMapper objectMapper = strategies.objectMapper();
		assertThat(objectMapper).isNotNull();
		assertThat(objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion())
				.isEqualTo(JsonInclude.Include.USE_DEFAULTS);
	}

	@Test
	public void test3() {
		JsonRpcExtractorStrategies strategies = JsonRpcExtractorStrategies.builder()
				.jackson(builder -> {
					builder.serializationInclusion(JsonInclude.Include.NON_NULL);
				})
				.build();
		ObjectMapper objectMapper = strategies.objectMapper();
		assertThat(objectMapper).isNotNull();
		assertThat(objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion())
				.isEqualTo(JsonInclude.Include.NON_NULL);
	}
}
