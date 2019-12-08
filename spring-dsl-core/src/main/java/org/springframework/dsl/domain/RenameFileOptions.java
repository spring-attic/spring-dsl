/*
 * Copyright 2019 the original author or authors.
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
package org.springframework.dsl.domain;

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class RenameFileOptions {

	private Boolean override;
	private Boolean ignoreIfExists;

	public RenameFileOptions() {
	}

	public RenameFileOptions(Boolean override, Boolean ignoreIfExist) {
		this.override = override;
		this.ignoreIfExists = ignoreIfExist;
	}

	public Boolean getOverride() {
		return override;
	}

	public void setOverride(Boolean override) {
		this.override = override;
	}

	public Boolean getIgnoreIfExists() {
		return ignoreIfExists;
	}

	public void setIgnoreIfExists(Boolean ignoreIfExists) {
		this.ignoreIfExists = ignoreIfExists;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ignoreIfExists == null) ? 0 : ignoreIfExists.hashCode());
		result = prime * result + ((override == null) ? 0 : override.hashCode());
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
		RenameFileOptions other = (RenameFileOptions) obj;
		if (ignoreIfExists == null) {
			if (other.ignoreIfExists != null)
				return false;
		} else if (!ignoreIfExists.equals(other.ignoreIfExists))
			return false;
		if (override == null) {
			if (other.override != null)
				return false;
		} else if (!override.equals(other.override))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link RenameFileOptions}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RenameFileOptionsBuilder<P> extends DomainBuilder<RenameFileOptions, P> {

		/**
		 * Sets override.
		 *
		 * @param override the override
		 * @return the rename file options builder
		 */
		RenameFileOptionsBuilder<P> override(boolean override);

		/**
		 * Sets ingoreIfExists.
		 *
		 * @param ignoreIfExists the ignoreIfExists
		 * @return the rename file options builder
		 */
		RenameFileOptionsBuilder<P> ignoreIfExists(boolean ignoreIfExists);
	}

	/**
	 * Gets a builder for {@link RenameFileOptions}
	 *
	 * @return the rename file options builder
	 */
	public static <P> RenameFileOptionsBuilder<P> renameFileOptions() {
		return new InternalRenameFileOptionsBuilder<>(null);
	}

	protected static <P> RenameFileOptionsBuilder<P> renameFileOptions(P parent) {
		return new InternalRenameFileOptionsBuilder<>(parent);
	}

	private static class InternalRenameFileOptionsBuilder<P> extends AbstractDomainBuilder<RenameFileOptions, P>
			implements RenameFileOptionsBuilder<P> {

		private Boolean override;
		private Boolean ignoreIfExists;

		InternalRenameFileOptionsBuilder(P parent) {
			super(parent);
		}

		@Override
		public RenameFileOptionsBuilder<P> override(boolean override) {
			this.override = override;
			return this;
		}

		@Override
		public RenameFileOptionsBuilder<P> ignoreIfExists(boolean ignoreIfExists) {
			this.ignoreIfExists = ignoreIfExists;
			return this;
		}

		@Override
		public RenameFileOptions build() {
			return new RenameFileOptions(override, ignoreIfExists);
		}
	}
}
