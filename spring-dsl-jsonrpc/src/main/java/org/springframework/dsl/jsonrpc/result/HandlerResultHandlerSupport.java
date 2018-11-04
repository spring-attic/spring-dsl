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
package org.springframework.dsl.jsonrpc.result;

import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResultHandler;
import org.springframework.util.Assert;

/**
 * Base class for {@link JsonRpcHandlerResultHandler} and access to a
 * {@code ReactiveAdapter} registry.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class HandlerResultHandlerSupport implements Ordered {

	private final ReactiveAdapterRegistry adapterRegistry;
	private int order = LOWEST_PRECEDENCE;

	/**
	 * Instantiates a new handler result handler support.
	 *
	 * @param adapterRegistry the adapter registry
	 */
	protected HandlerResultHandlerSupport(ReactiveAdapterRegistry adapterRegistry) {
		Assert.notNull(adapterRegistry, "ReactiveAdapterRegistry is required");
		this.adapterRegistry = adapterRegistry;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the order for this result handler relative to others.
	 * <p>
	 * By default set to {@link Ordered#LOWEST_PRECEDENCE}, however see Javadoc of
	 * sub-classes which may change this default.
	 *
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Gets the configured {@link ReactiveAdapterRegistry}.
	 *
	 * @return the reactive adapter registry
	 */
	public ReactiveAdapterRegistry getAdapterRegistry() {
		return this.adapterRegistry;
	}
}
