/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.document;

import org.springframework.dsl.DslException;

/**
 * This exception is raised when operations on a {@link Document} fail because
 * they are operating on invalid location.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class BadLocationException extends DslException {

	private static final long serialVersionUID = -8518215437927854537L;

	/**
	 * Instantiates a new bad location exception.
	 *
	 * @param message the message
	 * @param e the exception
	 */
	public BadLocationException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * Instantiates a new bad location exception.
	 *
	 * @param message the message
	 */
	public BadLocationException(String message) {
		super(message);
	}
}
