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
package org.springframework.dsl.lsp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.lsp.LspVersionDetector.LspVersion;

/**
 * Tests for {@link LspVersionDetector}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspVersionDetectorTests {

	@Test
	public void test2x() {
		InitializeParams initializeParams = InitializeParams.initializeParams()
			.capabilities()
				.textDocument()
					.synchronization()
					.and()
				.and()
			.and()
			.build();
		assertThat(LspVersionDetector.detect(initializeParams)).isEqualTo(LspVersion.VERSION_2);
	}

	@Test
	public void testDefaultsTo30() {
		assertThat(LspVersionDetector.detect(InitializeParams.initializeParams().build()))
				.isEqualTo(LspVersion.VERSION_3_0);
	}

	@Test
	public void test3x() {
		InitializeParams initializeParams = InitializeParams.initializeParams()
			.capabilities()
				.textDocument()
					.synchronization()
						.didSave(true)
					.and()
				.and()
			.and()
			.build();
		assertThat(LspVersionDetector.detect(initializeParams)).isEqualTo(LspVersion.VERSION_3_0);

		initializeParams = InitializeParams.initializeParams()
				.capabilities()
					.textDocument()
						.synchronization()
							.willSave(true)
						.and()
					.and()
				.and()
				.build();
		assertThat(LspVersionDetector.detect(initializeParams)).isEqualTo(LspVersion.VERSION_3_0);

		initializeParams = InitializeParams.initializeParams()
				.capabilities()
					.textDocument()
						.synchronization()
							.willSaveWaitUntil(true)
						.and()
					.and()
				.and()
				.build();
		assertThat(LspVersionDetector.detect(initializeParams)).isEqualTo(LspVersion.VERSION_3_0);
	}
}
