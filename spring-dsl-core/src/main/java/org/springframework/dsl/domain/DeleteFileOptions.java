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

public class DeleteFileOptions {

	private Boolean override;
	private Boolean ignoreIfExists;

	public DeleteFileOptions() {
	}

	public DeleteFileOptions(Boolean override, Boolean ignoreIfExist) {
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
		DeleteFileOptions other = (DeleteFileOptions) obj;
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
	 * Builder interface for {@link DeleteFileOptions}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DeleteFileOptionsBuilder<P> extends DomainBuilder<DeleteFileOptions, P> {

		/**
		 * Sets override.
		 *
		 * @param override the override
		 * @return the delete file options builder
		 */
		DeleteFileOptionsBuilder<P> override(boolean override);

		/**
		 * Sets ingoreIfExists.
		 *
		 * @param ignoreIfExists the ignoreIfExists
		 * @return the delete file options builder
		 */
		DeleteFileOptionsBuilder<P> ignoreIfExists(boolean ignoreIfExists);
	}

	/**
	 * Gets a builder for {@link DeleteFileOptions}
	 *
	 * @return the delete file options builder
	 */
	public static <P> DeleteFileOptionsBuilder<P> deleteFileOptions() {
		return new InternalDeleteFileOptionsBuilder<>(null);
	}

	protected static <P> DeleteFileOptionsBuilder<P> deleteFileOptions(P parent) {
		return new InternalDeleteFileOptionsBuilder<>(parent);
	}

	private static class InternalDeleteFileOptionsBuilder<P> extends AbstractDomainBuilder<DeleteFileOptions, P>
			implements DeleteFileOptionsBuilder<P> {

		private Boolean override;
		private Boolean ignoreIfExists;

		InternalDeleteFileOptionsBuilder(P parent) {
			super(parent);
		}

		@Override
		public DeleteFileOptionsBuilder<P> override(boolean override) {
			this.override = override;
			return this;
		}

		@Override
		public DeleteFileOptionsBuilder<P> ignoreIfExists(boolean ignoreIfExists) {
			this.ignoreIfExists = ignoreIfExists;
			return this;
		}

		@Override
		public DeleteFileOptions build() {
			return new DeleteFileOptions(override, ignoreIfExists);
		}
	}
}
