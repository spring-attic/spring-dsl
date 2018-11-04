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
package org.springframework.dsl.jsonrpc;

import java.util.function.Function;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * Represent the result of the invocation of a handler or a handler method.
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcHandlerResult {

	/** The handler. */
	private final Object handler;

	/** The return value. */
	private final Object returnValue;

	/** The return type. */
	private final ResolvableType returnType;

	/** The exception handler. */
	private Function<Throwable, Mono<JsonRpcHandlerResult>> exceptionHandler;

	/**
	 * Instantiates a new handler result.
	 *
	 * @param handler the handler
	 * @param returnValue the return value
	 * @param returnType the return type
	 */
	public JsonRpcHandlerResult(Object handler, Object returnValue, MethodParameter returnType) {
		Assert.notNull(handler, "'handler' is required");
		Assert.notNull(returnType, "'returnType' is required");
		this.handler = handler;
		this.returnValue = returnValue;
		this.returnType = ResolvableType.forMethodParameter(returnType);
	}

	/**
	 * Sets the exception handler.
	 *
	 * @param function the function
	 * @return the handler result
	 */
	public JsonRpcHandlerResult setExceptionHandler(Function<Throwable, Mono<JsonRpcHandlerResult>> function) {
		this.exceptionHandler = function;
		return this;
	}

	/**
	 * Return the type of the value returned from the handler -- e.g. the return
	 * type declared on a controller method's signature. Also see
	 * {@link #getReturnTypeSource()} to obtain the underlying
	 * {@link MethodParameter} for the return type.
	 *
	 * @return the resolvable type
	 */
	public ResolvableType getReturnType() {
		return this.returnType;
	}

	/**
	 * Return the {@link MethodParameter} from which {@link #getReturnType()
	 * returnType} was created.
	 *
	 * @return the method parameter
	 */
	public MethodParameter getReturnTypeSource() {
		return (MethodParameter) this.returnType.getSource();
	}

	/**
	 * Checks for exception handler.
	 *
	 * @return true, if successful
	 */
	public boolean hasExceptionHandler() {
		return (this.exceptionHandler != null);
	}

	/**
	 * Apply exception handler.
	 *
	 * @param failure the failure
	 * @return the mono
	 */
	public Mono<JsonRpcHandlerResult> applyExceptionHandler(Throwable failure) {
		return (hasExceptionHandler() ? this.exceptionHandler.apply(failure) : Mono.error(failure));
	}

	/**
	 * Gets the return value.
	 *
	 * @return the return value
	 */
	public Object getReturnValue() {
		return this.returnValue;
	}
}
