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

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dsl.lsp.server.controller.RootLanguageServerController;
import org.springframework.dsl.lsp.server.controller.TextDocumentLanguageServerController;

/**
 * {@link EnableAutoConfiguration Auto-configuration} configuring built-in
 * language server controllers.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
public class LanguageServerControllerAutoConfiguration {

	@Configuration
	@Import({ RootLanguageServerController.class, TextDocumentLanguageServerController.class })
	@ConditionalOnProperty(prefix = "spring.dsl.lsp.server.language-services", name = "enabled", havingValue = "true", matchIfMissing = true)
	public static class BuiltInControllerConfig {
	}
}
