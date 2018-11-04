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
 * Interface for exiting the JVM. This abstraction is useful in cases where
 * {@code LSP} server is running embedded in a jvm with other services and raw
 * {@code JVM} exit is not possible. For tests order to allow classes that make
 * System.exit calls to be testable, since calling System.exit during a unit
 * test would cause the entire jvm to finish.
 * <p>
 * This interface is really meant to attempt an exit when {@code LSP} client
 * sends {@code exit} message and we want to make sure jvm exists fast in cases
 * where {@code IDE} controls process lifecycle. However when running i.e.
 * embedded in a webserver for {@code websocket} mode, we don't want
 * {@code LSP Client} to kill whole server with its {@code exit} message.
 *
 * @author Janne Valkealahti
 *
 */
public interface LspExiter {

	static final Logger log = LoggerFactory.getLogger(LspExiter.class);

	/**
	 * No-op implementations which does nothing.
	 */
	public final static LspExiter NOOP_LSPEXITER = new LspExiter() {

		@Override
		public void exit(int status) {
			// don't do anything
			log.trace("noop exiter called, doing nothing");
		}
	};

	/**
	 * Terminate the currently running Java Virtual Machine.
	 *
	 * @param status exit status.
	 * @throws SecurityException
	 *             if a security manager exists and its <code>checkExit</code>
	 *             method doesn't allow exit with the specified status.
	 * @see System#exit(int)
	 */
	void exit(int status);
}
