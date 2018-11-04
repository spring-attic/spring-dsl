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

public class Synchronization extends DynamicRegistration {

	private Boolean willSave;
	private Boolean willSaveWaitUntil;
	private Boolean didSave;

	public Synchronization() {
	}

	public Synchronization(Boolean dynamicRegistration, Boolean willSave, Boolean willSaveWaitUntil, Boolean didSave) {
		super(dynamicRegistration);
		this.willSave = willSave;
		this.willSaveWaitUntil = willSaveWaitUntil;
		this.didSave = didSave;
	}

	public Boolean getWillSave() {
		return willSave;
	}

	public void setWillSave(Boolean willSave) {
		this.willSave = willSave;
	}

	public Boolean getWillSaveWaitUntil() {
		return willSaveWaitUntil;
	}

	public void setWillSaveWaitUntil(Boolean willSaveWaitUntil) {
		this.willSaveWaitUntil = willSaveWaitUntil;
	}

	public Boolean getDidSave() {
		return didSave;
	}

	public void setDidSave(Boolean didSave) {
		this.didSave = didSave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((didSave == null) ? 0 : didSave.hashCode());
		result = prime * result + ((willSave == null) ? 0 : willSave.hashCode());
		result = prime * result + ((willSaveWaitUntil == null) ? 0 : willSaveWaitUntil.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Synchronization other = (Synchronization) obj;
		if (didSave == null) {
			if (other.didSave != null)
				return false;
		} else if (!didSave.equals(other.didSave))
			return false;
		if (willSave == null) {
			if (other.willSave != null)
				return false;
		} else if (!willSave.equals(other.willSave))
			return false;
		if (willSaveWaitUntil == null) {
			if (other.willSaveWaitUntil != null)
				return false;
		} else if (!willSaveWaitUntil.equals(other.willSaveWaitUntil))
			return false;
		return true;
	}

	public interface SynchronizationBuilder<P> extends DomainBuilder<Synchronization, P> {

		SynchronizationBuilder<P> dynamicRegistration(Boolean dynamicRegistration);

		SynchronizationBuilder<P> willSave(Boolean willSave);

		SynchronizationBuilder<P> willSaveWaitUntil(Boolean willSaveWaitUntil);

		SynchronizationBuilder<P> didSave(Boolean didSave);
	}

	/**
	 * Gets a builder for {@link Synchronization}
	 *
	 * @return the versioned text document identifier builder
	 */
	public static <P> SynchronizationBuilder<P> synchronization() {
		return new InternalSynchronizationBuilder<>(null);
	}

	protected static <P> SynchronizationBuilder<P> synchronization(P parent) {
		return new InternalSynchronizationBuilder<>(parent);
	}

	private static class InternalSynchronizationBuilder<P>
			extends AbstractDomainBuilder<Synchronization, P> implements SynchronizationBuilder<P> {

		private Boolean dynamicRegistration;
		private Boolean willSave;
		private Boolean willSaveWaitUntil;
		private Boolean didSave;

		InternalSynchronizationBuilder(P parent) {
			super(parent);
		}

		@Override
		public SynchronizationBuilder<P> dynamicRegistration(Boolean dynamicRegistration) {
			this.dynamicRegistration = dynamicRegistration;
			return this;
		}

		@Override
		public SynchronizationBuilder<P> willSave(Boolean willSave) {
			this.willSave = willSave;
			return this;
		}

		@Override
		public SynchronizationBuilder<P> willSaveWaitUntil(Boolean willSaveWaitUntil) {
			this.willSaveWaitUntil = willSaveWaitUntil;
			return this;
		}

		@Override
		public SynchronizationBuilder<P> didSave(Boolean didSave) {
			this.didSave = didSave;
			return this;
		}

		@Override
		public Synchronization build() {
			return new Synchronization(dynamicRegistration, willSave, willSaveWaitUntil, didSave);
		}
	}
}
