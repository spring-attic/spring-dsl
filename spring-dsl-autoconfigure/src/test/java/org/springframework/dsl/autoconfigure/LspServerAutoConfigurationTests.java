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
package org.springframework.dsl.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.lsp.server.controller.RootLanguageServerController;

/**
 * Tests for {@link LspServerAutoConfiguration}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspServerAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(LspServerAutoConfiguration.class));

	@Test
	public void autoConfigNoProperties() {
		this.contextRunner
				.run((context) -> {
					assertThat(context).doesNotHaveBean(ReactiveAdapterRegistry.class);
				});
	}

	@Test
	public void autoConfigProperties() {
		this.contextRunner
				.withUserConfiguration(DslAutoConfiguration.class, LanguageServerControllerAutoConfiguration.class)
				.withPropertyValues("spring.dsl.lsp.server.mode=PROCESS")
				.run((context) -> {
					assertThat(context).hasSingleBean(ReactiveAdapterRegistry.class);
					assertThat(context).hasSingleBean(RootLanguageServerController.class);
				});
		this.contextRunner
				.withUserConfiguration(DslAutoConfiguration.class, LanguageServerControllerAutoConfiguration.class)
				.withPropertyValues("spring.dsl.lsp.server.mode=PROCESS", "spring.dsl.lsp.server.language-services.enabled=false")
				.run((context) -> {
					assertThat(context).hasSingleBean(ReactiveAdapterRegistry.class);
					assertThat(context).doesNotHaveBean(RootLanguageServerController.class);
				});
	}
}
