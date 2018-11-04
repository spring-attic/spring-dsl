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
package org.springframework.dsl.lsp.server;

/**
 * A {@code PortInUseException} is thrown when a lsp server fails to start due
 * to a port already being in use.
 *
 * @author Janne Valkealahti
 *
 */
public class PortInUseException extends LspServerException {

	private static final long serialVersionUID = 4800962432248919233L;
	private final int port;

	/**
	 * Creates a new port in use exception for the given {@code port}.
	 *
	 * @param port the port that was in use
	 */
	public PortInUseException(int port) {
		super("Port " + port + " is already in use", null);
		this.port = port;
	}

	/**
	 * Returns the port that was in use.
	 *
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}
}
