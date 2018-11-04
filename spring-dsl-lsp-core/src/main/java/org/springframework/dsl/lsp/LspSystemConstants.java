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

import org.springframework.dsl.service.DocumentStateTracker;

/**
 * Various constants used by a system.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class LspSystemConstants {

	/**
	 * Session attribute if set to {@code TRUE} indicates that session has been initialized.
	 */
	public final static String SESSION_ATTRIBUTE_LSP_SESSION_STATE = "lspSessionState";

	/**
	 * Session attribute containing access to {@link DocumentStateTracker}.
	 */
	public final static String SESSION_ATTRIBUTE_DOCUMENT_STATE_TRACKER = "documentStateTracker";

	/**
	 * Session attribute containing negotiated lsp version.
	 */
	public final static String SESSION_ATTRIBUTE_LSP_VERSION = "lspVersion";

	/**
	 * Session attribute containing added lsp client
	 */
	public final static String SESSION_ATTRIBUTE_LSP_CLIENT = "lspClient";
}
