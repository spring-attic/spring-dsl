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
package org.springframework.dsl.lsp.server.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link LspExiter} interface that calls the standards
 * System.exit method. It should be noted that there will be no unit tests for
 * this class, since there is only one line of actual code, that would only be
 * testable by mocking System or Runtime.
 *
 * @author Janne Valkealahti
 *
 */
public class JvmLspExiter implements LspExiter {

	private static final Logger log = LoggerFactory.getLogger(JvmLspExiter.class);

	/**
	 * Delegate call to System.exit() with the argument provided.
	 *
	 * @see LspExiter#exit(int)
	 */
	@Override
	public void exit(int status) {
		log.trace("Exiting jvm with status {}, bye bye...", status);
		System.exit(status);
	}
}
