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
public class Registration {

	private String id;
	private String method;
	private Object registerOptions;

	/**
	 * Instantiates a new registration.
	 */
	public Registration() {
	}

	/**
	 * Instantiates a new registration.
	 *
	 * @param id the id
	 * @param method the method
	 */
	public Registration(String id, String method) {
		this(id, method, null);
	}

	/**
	 * Instantiates a new registration.
	 *
	 * @param id the id
	 * @param method the method
	 * @param registerOptions the register options
	 */
	public Registration(String id, String method, Object registerOptions) {
		this.id = id;
		this.method = method;
		this.registerOptions = registerOptions;
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

	public Object getRegisterOptions() {
		return registerOptions;
	}

	public void setRegisterOptions(Object registerOptions) {
		this.registerOptions = registerOptions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((registerOptions == null) ? 0 : registerOptions.hashCode());
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
		Registration other = (Registration) obj;
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
		if (registerOptions == null) {
			if (other.registerOptions != null)
				return false;
		} else if (!registerOptions.equals(other.registerOptions))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link Registration}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RegistrationBuilder<P> extends DomainBuilder<Registration, P>{

		/**
		 * Sets an id.
		 *
		 * @param id the id
		 * @return the builder for chaining
		 */
		RegistrationBuilder<P> id(String id);

		/**
		 * Sets a method.
		 *
		 * @param method the method
		 * @return the builder for chaining
		 */
		RegistrationBuilder<P> method(String method);

		/**
		 * Sets a register options.
		 *
		 * @param registerOptions the register options
		 * @return the builder for chaining
		 */
		RegistrationBuilder<P> registerOptions(Object registerOptions);
	}

	/**
	 * Gets a builder for {@link Registration}
	 *
	 * @return the registration builder
	 */
	public static <P> RegistrationBuilder<P> registration() {
		return new InternalRegistrationBuilder<>(null);
	}

	protected static <P> RegistrationBuilder<P> registration(P parent) {
		return new InternalRegistrationBuilder<>(parent);
	}

	private static class InternalRegistrationBuilder<P> extends AbstractDomainBuilder<Registration, P>
			implements RegistrationBuilder<P> {

		private String id;
		private String method;
		private Object registerOptions;

		InternalRegistrationBuilder(P parent) {
			super(parent);
		}

		@Override
		public RegistrationBuilder<P> id(String id) {
			this.id = id;
			return this;
		}

		@Override
		public RegistrationBuilder<P> method(String method) {
			this.method = method;
			return this;
		}

		@Override
		public RegistrationBuilder<P> registerOptions(Object registerOptions) {
			this.registerOptions = registerOptions;
			return this;
		}

		@Override
		public Registration build() {
			return new Registration(id, method, registerOptions);
		}
	}
}
