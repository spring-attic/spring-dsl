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
 * {@code LSP} domain object for a specification
 * {@code VersionedTextDocumentIdentifier}.
 *
 * @author Janne Valkealahti
 *
 */
public class VersionedTextDocumentIdentifier extends TextDocumentIdentifier {

	private Integer version;

	public VersionedTextDocumentIdentifier() {
	}

	public VersionedTextDocumentIdentifier(Integer version) {
		this(version, null);
	}

	public VersionedTextDocumentIdentifier(Integer version, String uri) {
		super(uri);
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		VersionedTextDocumentIdentifier other = (VersionedTextDocumentIdentifier) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link VersionedTextDocumentIdentifier}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface VersionedTextDocumentIdentifierBuilder<P> extends DomainBuilder<VersionedTextDocumentIdentifier, P> {

		/**
		 * Sets an uri.
		 *
		 * @param uri the uri
		 * @return the builder for chaining
		 */
		VersionedTextDocumentIdentifierBuilder<P> uri(String uri);

		/**
		 * Sets a version.
		 *
		 * @param version the version
		 * @return the builder for chaining
		 */
		VersionedTextDocumentIdentifierBuilder<P> version(Integer version);
	}

	/**
	 * Gets a builder for {@link VersionedTextDocumentIdentifier}
	 *
	 * @return the versioned text document identifier builder
	 */
	public static <P> VersionedTextDocumentIdentifierBuilder<P> versionedTextDocumentIdentifier() {
		return new InternalVersionedTextDocumentIdentifierBuilder<>(null);
	}

	protected static <P> VersionedTextDocumentIdentifierBuilder<P> versionedTextDocumentIdentifier(P parent) {
		return new InternalVersionedTextDocumentIdentifierBuilder<>(parent);
	}

	private static class InternalVersionedTextDocumentIdentifierBuilder<P>
			extends AbstractDomainBuilder<VersionedTextDocumentIdentifier, P> implements VersionedTextDocumentIdentifierBuilder<P> {

		private String uri;
		private Integer version;

		InternalVersionedTextDocumentIdentifierBuilder(P parent) {
			super(parent);
		}

		@Override
		public VersionedTextDocumentIdentifierBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public VersionedTextDocumentIdentifierBuilder<P> version(Integer version) {
			this.version = version;
			return this;
		}

		@Override
		public VersionedTextDocumentIdentifier build() {
			return new VersionedTextDocumentIdentifier(version, uri);
		}
	}
}
