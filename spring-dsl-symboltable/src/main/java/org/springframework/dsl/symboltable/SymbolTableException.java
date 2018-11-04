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
package org.springframework.dsl.symboltable;

import java.io.IOException;

import org.springframework.dsl.DslException;

/**
 * Generic exception indicating error in a symbol table processing.
 *
 * @author Janne Valkealahti
 *
 */
public class SymbolTableException extends DslException {

	private static final long serialVersionUID = -8790334654939111605L;

	/**
	 * Instantiates a new symbol table exception.
	 *
	 * @param e the exception
	 */
	public SymbolTableException(IOException e) {
		super(e);
	}

	/**
	 * Instantiates a new symbol table exception.
	 *
	 * @param message the message
	 * @param e the exception
	 */
	public SymbolTableException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * Instantiates a new symbol table exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SymbolTableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new symbol table exception.
	 *
	 * @param message the message
	 */
	public SymbolTableException(String message) {
		super(message);
	}
}
