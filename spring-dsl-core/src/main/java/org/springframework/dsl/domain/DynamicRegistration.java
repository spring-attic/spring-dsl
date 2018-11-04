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

public class DynamicRegistration {

	private Boolean dynamicRegistration;

	public DynamicRegistration() {
	}

	public DynamicRegistration(Boolean dynamicRegistration) {
		this.dynamicRegistration = dynamicRegistration;
	}

	public Boolean getDynamicRegistration() {
		return dynamicRegistration;
	}

	public void setDynamicRegistration(Boolean dynamicRegistration) {
		this.dynamicRegistration = dynamicRegistration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dynamicRegistration == null) ? 0 : dynamicRegistration.hashCode());
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
		DynamicRegistration other = (DynamicRegistration) obj;
		if (dynamicRegistration == null) {
			if (other.dynamicRegistration != null)
				return false;
		} else if (!dynamicRegistration.equals(other.dynamicRegistration))
			return false;
		return true;
	}

	public interface DynamicRegistrationBuilder<P> extends DomainBuilder<DynamicRegistration, P> {

		DynamicRegistrationBuilder<P> dynamicRegistration(Boolean dynamicRegistration);
	}

	public static <P extends DynamicRegistration> DynamicRegistrationBuilder<P> dynamicRegistration() {
		return new InternalDynamicRegistrationBuilder<>(null);
	}

	protected static <P extends DynamicRegistration> DynamicRegistrationBuilder<P> dynamicRegistration(P parent) {
		return new InternalDynamicRegistrationBuilder<>(parent);
	}

	private static class InternalDynamicRegistrationBuilder<P>
			extends AbstractDomainBuilder<DynamicRegistration, P> implements DynamicRegistrationBuilder<P> {

		private Boolean dynamicRegistration;

		InternalDynamicRegistrationBuilder(P parent) {
			super(parent);
		}

		@Override
		public InternalDynamicRegistrationBuilder<P> dynamicRegistration(Boolean dynamicRegistration) {
			this.dynamicRegistration = dynamicRegistration;
			return this;
		}

		@Override
		public DynamicRegistration build() {
			return new DynamicRegistration(dynamicRegistration);
		}
	}
}
