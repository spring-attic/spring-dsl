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
package org.springframework.dsl.buildtests;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Base integration lsp tests using full command structures against various
 * supported implementations.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractLspIntegrationTests {

	protected ConfigurableApplicationContext serverContext;
	protected ConfigurableApplicationContext clientContext;

	@Before
	public void setup() {
		serverContext = buildServerContext();
		clientContext = buildClientContext();
		if (serverContext != null) {
			onServerContext(serverContext);
		}
		if (clientContext != null) {
			onClientContext(clientContext);
		}
	}

	@After
	public void clean() {
		if (serverContext != null) {
			serverContext.close();
		}
		if (clientContext != null) {
			clientContext.close();
		}
		serverContext = null;
		clientContext = null;
	}

	protected ConfigurableApplicationContext buildServerContext() {
		return null;
	}

	protected ConfigurableApplicationContext buildClientContext() {
		return null;
	}

	protected void onServerContext(ConfigurableApplicationContext context) {
	}

	protected void onClientContext(ConfigurableApplicationContext context) {
	}
}
