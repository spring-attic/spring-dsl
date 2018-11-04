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
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentItem {

	private String uri;
	private String languageId;
	private int version;
	private String text;

	/**
	 * Instantiates a new text document item.
	 */
	public TextDocumentItem() {
	}

	/**
	 * Instantiates a new text document item.
	 *
	 * @param uri the uri
	 * @param languageId the language id
	 * @param version the version
	 * @param text the text
	 */
	public TextDocumentItem(String uri, String languageId, int version, String text) {
		super();
		this.uri = uri;
		this.languageId = languageId;
		this.version = version;
		this.text = text;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + version;
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
		TextDocumentItem other = (TextDocumentItem) obj;
		if (languageId == null) {
			if (other.languageId != null)
				return false;
		} else if (!languageId.equals(other.languageId))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (version != other.version)
			return false;
		return true;
	}


	/**
	 * Builder interface for {@link TextDocumentItem}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextDocumentItemBuilder<P> extends DomainBuilder<TextDocumentItem, P>{

		/**
		 * Sets an uri.
		 *
		 * @param uri the uri
		 * @return the builder for chaining
		 */
		TextDocumentItemBuilder<P> uri(String uri);

		/**
		 * Sets a languageId.
		 *
		 * @param languageId the languageId
		 * @return the builder for chaining
		 */
		TextDocumentItemBuilder<P> languageId(String languageId);

		/**
		 * Sets a version.
		 *
		 * @param version the version
		 * @return the builder for chaining
		 */
		TextDocumentItemBuilder<P> version(int version);

		/**
		 * Sets a text.
		 *
		 * @param text the text
		 * @return the builder for chaining
		 */
		TextDocumentItemBuilder<P> text(String text);
	}

	/**
	 * Gets a builder for {@link TextDocumentItem}
	 *
	 * @return the text document item builder
	 */
	public static <P> TextDocumentItemBuilder<P> textDocumentItem() {
		return new InternalTextDocumentItemBuilder<>(null);
	}

	protected static <P> TextDocumentItemBuilder<P> textDocumentItem(P parent) {
		return new InternalTextDocumentItemBuilder<>(parent);
	}

	private static class InternalTextDocumentItemBuilder<P> extends AbstractDomainBuilder<TextDocumentItem, P> implements TextDocumentItemBuilder<P>{

		private String uri;
		private String languageId;
		private int version;
		private String text;

		InternalTextDocumentItemBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentItemBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public TextDocumentItemBuilder<P> languageId(String languageId) {
			this.languageId = languageId;
			return this;
		}

		@Override
		public TextDocumentItemBuilder<P> version(int version) {
			this.version = version;
			return this;
		}

		@Override
		public TextDocumentItemBuilder<P> text(String text) {
			this.text = text;
			return this;
		}

		@Override
		public TextDocumentItem build() {
			return new TextDocumentItem(uri, languageId, version, text);
		}
	}
}
