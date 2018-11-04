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
package org.springframework.dsl.domain;

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code Registration}.
 *
 * @author Janne Valkealahti
 *
 */
public class Unregistration {

	private String id;
	private String method;

	/**
	 * Instantiates a new unregistration.
	 */
	public Unregistration() {
	}

	/**
	 * Instantiates a new unregistration.
	 *
	 * @param id the id
	 * @param method the method
	 */
	public Unregistration(String id, String method) {
		this.id = id;
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unregistration other = (Unregistration) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link Unregistration}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface UnregistrationBuilder<P> extends DomainBuilder<Unregistration, P>{

		/**
		 * Sets an id.
		 *
		 * @param id the id
		 * @return the builder for chaining
		 */
		UnregistrationBuilder<P> id(String id);

		/**
		 * Sets a method.
		 *
		 * @param method the method
		 * @return the builder for chaining
		 */
		UnregistrationBuilder<P> method(String method);
	}

	/**
	 * Gets a builder for {@link UnregistrationBuilder}
	 *
	 * @return the unregistration builder
	 */
	public static <P> UnregistrationBuilder<P> unregistration() {
		return new InternalUnregistrationBuilder<>(null);
	}

	protected static <P> UnregistrationBuilder<P> unregistration(P parent) {
		return new InternalUnregistrationBuilder<>(parent);
	}

	private static class InternalUnregistrationBuilder<P> extends AbstractDomainBuilder<Unregistration, P>
			implements UnregistrationBuilder<P> {

		private String id;
		private String method;

		InternalUnregistrationBuilder(P parent) {
			super(parent);
		}

		@Override
		public UnregistrationBuilder<P> id(String id) {
			this.id = id;
			return this;
		}

		@Override
		public UnregistrationBuilder<P> method(String method) {
			this.method = method;
			return this;
		}

		@Override
		public Unregistration build() {
			return new Unregistration(id, method);
		}
	}
}
