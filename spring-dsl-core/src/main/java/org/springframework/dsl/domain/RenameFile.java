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

import org.springframework.dsl.domain.RenameFileOptions.RenameFileOptionsBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class RenameFile {

	private RenameFileKind kind;
	private String oldUri;
	private String newUri;
	private RenameFileOptions options;

	public RenameFile() {
	}

	public RenameFile(RenameFileKind kind, String oldUri, String newUri, RenameFileOptions options) {
		this.kind = kind;
		this.oldUri = oldUri;
		this.newUri = newUri;
		this.options = options;
	}

	public RenameFileKind getKind() {
		return kind;
	}

	public void setKind(RenameFileKind kind) {
		this.kind = kind;
	}

	public String getOldUri() {
		return oldUri;
	}

	public void setOldUri(String oldUri) {
		this.oldUri = oldUri;
	}

	public String getNewUri() {
		return newUri;
	}

	public void setNewUri(String newUri) {
		this.newUri = newUri;
	}

	public RenameFileOptions getOptions() {
		return options;
	}

	public void setOptions(RenameFileOptions options) {
		this.options = options;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((oldUri == null) ? 0 : oldUri.hashCode());
		result = prime * result + ((newUri == null) ? 0 : newUri.hashCode());
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
		RenameFile other = (RenameFile) obj;
		if (kind != other.kind)
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (oldUri == null) {
			if (other.oldUri != null)
				return false;
		} else if (!oldUri.equals(other.oldUri))
			return false;
		if (newUri == null) {
			if (other.newUri != null)
				return false;
		} else if (!newUri.equals(other.newUri))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link RenameFile}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RenameFileBuilder<P> extends DomainBuilder<RenameFile, P> {

		/**
		 * Sets kind.
		 *
		 * @param kind the kind
		 * @return the rename file builder
		 */
		RenameFileBuilder<P> kind(RenameFileKind kind);

		/**
		 * Sets old uri.
		 *
		 * @param oldUri the old uri
		 * @return the rename file builder
		 */
		RenameFileBuilder<P> oldUri(String oldUri);

		/**
		 * Sets new uri.
		 *
		 * @param oldUri the new uri
		 * @return the rename file builder
		 */
		RenameFileBuilder<P> newUri(String newUri);

		/**
		 * Gets a builder for end {@link RenameFileOptions}.
		 *
		 * @return the rename file options builder
		 */
		RenameFileOptionsBuilder<RenameFileBuilder<P>> options();
	}

	/**
	 * Gets a builder for {@link RenameFile}
	 *
	 * @return the rename file builder
	 */
	public static <P> RenameFileBuilder<P> renameFile() {
		return new InternalRenameFileBuilder<>(null);
	}

	protected static <P> RenameFileBuilder<P> renameFile(P parent) {
		return new InternalRenameFileBuilder<>(parent);
	}

    private static class InternalRenameFileBuilder<P> extends AbstractDomainBuilder<RenameFile, P>
            implements RenameFileBuilder<P> {

		private RenameFileKind kind;
		private String oldUri;
		private String newUri;
		RenameFileOptionsBuilder<RenameFileBuilder<P>> options;

		InternalRenameFileBuilder(P parent) {
			super(parent);
		}

		@Override
		public RenameFileBuilder<P> kind(RenameFileKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public RenameFileBuilder<P> oldUri(String oldUri) {
			this.oldUri = oldUri;
			return this;
		}

		@Override
		public RenameFileBuilder<P> newUri(String newUri) {
			this.newUri = newUri;
			return this;
		}

		@Override
		public RenameFileOptionsBuilder<RenameFileBuilder<P>> options() {
			this.options = RenameFileOptions.renameFileOptions(this);
			return options;
		}

		@Override
		public RenameFile build() {
			return new RenameFile(kind, oldUri, newUri, options != null ? options.build() : null);
		}
	}
}
