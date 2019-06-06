/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.jsonrpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * Annotation that indicates a method return value should be bound to the
 * {@code JSONRPC} responses as notifications.
 *
 * @author Janne Valkealahti
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcNotification {

	/**
	 * Defines {@code JsonRpcNotification} {@code method} response field. If omitted
	 * or empty, falls back to equivalent methods from a request.
	 *
	 * @return the method
	 */
	@AliasFor("method")
	String value() default "";

	/**
	 * Alias for {@link #value()}.
	 *
	 * @return the paths
	 * @see #value()
	 */
	@AliasFor("value")
	String method() default "";

}
