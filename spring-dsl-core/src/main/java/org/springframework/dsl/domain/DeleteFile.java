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

import org.springframework.dsl.domain.DeleteFileOptions.DeleteFileOptionsBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class DeleteFile {

	private DeleteFileKind kind;
	private String uri;
	private DeleteFileOptions options;

	public DeleteFile() {
	}

	public DeleteFile(DeleteFileKind kind, String uri, DeleteFileOptions options) {
		this.kind = kind;
		this.uri = uri;
		this.options = options;
	}

	public DeleteFileKind getKind() {
		return kind;
	}

	public void setKind(DeleteFileKind kind) {
		this.kind = kind;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public DeleteFileOptions getOptions() {
		return options;
	}

	public void setOptions(DeleteFileOptions options) {
		this.options = options;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
		DeleteFile other = (DeleteFile) obj;
		if (kind != other.kind)
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link DeleteFile}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DeleteFileBuilder<P> extends DomainBuilder<DeleteFile, P> {

		/**
		 * Sets kind.
		 *
		 * @param kind the kind
		 * @return the delete file builder
		 */
		DeleteFileBuilder<P> kind(DeleteFileKind kind);


		/**
		 * Sets uri.
		 *
		 * @param uri the uri
		 * @return the delete file builder
		 */
		DeleteFileBuilder<P> uri(String uri);

		/**
		 * Gets a builder for end {@link DeleteFileOptions}.
		 *
		 * @return the delete file options builder
		 */
		DeleteFileOptionsBuilder<DeleteFileBuilder<P>> options();
	}

	/**
	 * Gets a builder for {@link DeleteFile}
	 *
	 * @return the Delete file builder
	 */
	public static <P> DeleteFileBuilder<P> deleteFile() {
		return new InternalDeleteFileBuilder<>(null);
	}

	protected static <P> DeleteFileBuilder<P> deleteFile(P parent) {
		return new InternalDeleteFileBuilder<>(parent);
	}

	private static class InternalDeleteFileBuilder<P> extends AbstractDomainBuilder<DeleteFile, P>
			implements DeleteFileBuilder<P> {

		private DeleteFileKind kind;
		private String uri;
		DeleteFileOptionsBuilder<DeleteFileBuilder<P>> options;

		InternalDeleteFileBuilder(P parent) {
			super(parent);
		}

		@Override
		public DeleteFileBuilder<P> kind(DeleteFileKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public DeleteFileBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public DeleteFileOptionsBuilder<DeleteFileBuilder<P>> options() {
			this.options = DeleteFileOptions.deleteFileOptions(this);
			return options;
		}

		@Override
		public DeleteFile build() {
			return new DeleteFile(kind, uri, options != null ? options.build() : null);
		}
	}
}
