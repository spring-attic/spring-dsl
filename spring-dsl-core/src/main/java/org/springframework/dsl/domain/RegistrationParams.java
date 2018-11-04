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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.domain.Registration.RegistrationBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code RegistrationParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class RegistrationParams {

	private List<Registration> registrations;

	/**
	 * Instantiates a new registration params.
	 */
	public RegistrationParams() {
	}

	/**
	 * Instantiates a new registration params.
	 *
	 * @param registrations the registrations
	 */
	public RegistrationParams(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registrations == null) ? 0 : registrations.hashCode());
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
		RegistrationParams other = (RegistrationParams) obj;
		if (registrations == null) {
			if (other.registrations != null)
				return false;
		} else if (!registrations.equals(other.registrations))
			return false;
		return true;
	}


	/**
	 * Builder interface for {@link RegistrationParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RegistrationParamsBuilder<P> extends DomainBuilder<RegistrationParams, P> {

		/**
		 * Gets a builder for {@link Registration}.
		 *
		 * @return the builder for chaining
		 */
		RegistrationBuilder<RegistrationParamsBuilder<P>> registration();
	}

	/**
	 * Gets a builder for {@link RegistrationParams}.
	 *
	 * @return the registration params builder
	 */
	public static <P> RegistrationParamsBuilder<P> registrationParams() {
		return new InternalRegistrationParamsBuilder<>(null);
	}

	protected static <P> RegistrationParamsBuilder<P> registrationParams(P parent) {
		return new InternalRegistrationParamsBuilder<>(parent);
	}

	private static class InternalRegistrationParamsBuilder<P>
			extends AbstractDomainBuilder<RegistrationParams, P> implements RegistrationParamsBuilder<P> {

		private List<RegistrationBuilder<RegistrationParamsBuilder<P>>> registrations = new ArrayList<>();

		InternalRegistrationParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public RegistrationBuilder<RegistrationParamsBuilder<P>> registration() {
			RegistrationBuilder<RegistrationParamsBuilder<P>> registrationBuilder = Registration.registration(this);
			this.registrations.add(registrationBuilder);
			return registrationBuilder;
		}

		@Override
		public RegistrationParams build() {
			RegistrationParams registrationParams = new RegistrationParams();
			if (!registrations.isEmpty()) {
				List<Registration> registration = new ArrayList<>();
				for (RegistrationBuilder<RegistrationParamsBuilder<P>> d : registrations) {
					registration.add(d.build());
				}
				registrationParams.setRegistrations(registration);
			}
			return registrationParams;
		}
	}
}
