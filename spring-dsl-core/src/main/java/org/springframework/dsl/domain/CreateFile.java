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

import org.springframework.dsl.domain.CreateFileOptions.CreateFileOptionsBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class CreateFile {

	private CreateFileKind kind;
	private String uri;
	private CreateFileOptions options;

	public CreateFile() {
	}

	public CreateFile(CreateFileKind kind, String uri, CreateFileOptions options) {
		this.kind = kind;
		this.uri = uri;
		this.options = options;
	}

	public CreateFileKind getKind() {
		return kind;
	}

	public void setKind(CreateFileKind kind) {
		this.kind = kind;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public CreateFileOptions getOptions() {
		return options;
	}

	public void setOptions(CreateFileOptions options) {
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
		CreateFile other = (CreateFile) obj;
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
	 * Builder interface for {@link CreateFile}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CreateFileBuilder<P> extends DomainBuilder<CreateFile, P> {

		/**
		 * Sets kind.
		 *
		 * @param kind the kind
		 * @return the create file builder
		 */
		CreateFileBuilder<P> kind(CreateFileKind kind);

		/**
		 * Sets uri.
		 *
		 * @param uri the uri
		 * @return the create file builder
		 */
		CreateFileBuilder<P> uri(String uri);

		/**
		 * Gets a builder for end {@link CreateFileOptions}.
		 *
		 * @return the create file options builder
		 */
		CreateFileOptionsBuilder<CreateFileBuilder<P>> options();
	}

	/**
	 * Gets a builder for {@link CreateFile}
	 *
	 * @return the create file builder
	 */
	public static <P> CreateFileBuilder<P> createFile() {
		return new InternalCreateFileBuilder<>(null);
	}

	protected static <P> CreateFileBuilder<P> createFile(P parent) {
		return new InternalCreateFileBuilder<>(parent);
	}

    private static class InternalCreateFileBuilder<P> extends AbstractDomainBuilder<CreateFile, P>
            implements CreateFileBuilder<P> {

		private CreateFileKind kind;
		private String uri;
		CreateFileOptionsBuilder<CreateFileBuilder<P>> options;

		InternalCreateFileBuilder(P parent) {
			super(parent);
		}

		@Override
		public CreateFileBuilder<P> kind(CreateFileKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public CreateFileBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public CreateFileOptionsBuilder<CreateFileBuilder<P>> options() {
			this.options = CreateFileOptions.createFileOptions(this);
			return options;
		}

		@Override
		public CreateFile build() {
			return new CreateFile(kind, uri, options != null ? options.build() : null);
		}
	}
}
