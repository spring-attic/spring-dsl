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
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentIdentifier {

	private String uri;

	/**
	 * Instantiates a new text document identifier.
	 */
	public TextDocumentIdentifier() {
	}

	/**
	 * Instantiates a new text document identifier.
	 *
	 * @param uri the uri
	 */
	public TextDocumentIdentifier(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TextDocumentIdentifier other = (TextDocumentIdentifier) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}


	/**
	 * Builder interface for {@link TextDocumentIdentifier}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextDocumentIdentifierBuilder<P> extends DomainBuilder<TextDocumentIdentifier, P> {

		/**
		 * Sets an uri.
		 *
		 * @param uri the uri
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<P> uri(String uri);
	}

	/**
	 * Gets a builder for {@link TextDocumentIdentifier}
	 *
	 * @return the text document identifier builder
	 */
	public static <P> TextDocumentIdentifierBuilder<P> textDocumentIdentifier() {
		return new InternalTextDocumentIdentifierBuilder<>(null);
	}

	protected static <P> TextDocumentIdentifierBuilder<P> textDocumentIdentifier(P parent) {
		return new InternalTextDocumentIdentifierBuilder<>(parent);
	}

	private static class InternalTextDocumentIdentifierBuilder<P>
			extends AbstractDomainBuilder<TextDocumentIdentifier, P> implements TextDocumentIdentifierBuilder<P> {

		private String uri;

		InternalTextDocumentIdentifierBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public TextDocumentIdentifier build() {
			return new TextDocumentIdentifier(uri);
		}
	}
}
