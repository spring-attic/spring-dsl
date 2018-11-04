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

import org.springframework.dsl.domain.TextDocumentIdentifier.TextDocumentIdentifierBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 *
 * @author Janne Valkealahti
 *
 */
public class DidSaveTextDocumentParams {

	private TextDocumentIdentifier textDocument;
	private String text;

	public DidSaveTextDocumentParams() {
	}

	public DidSaveTextDocumentParams(String text) {
		this.text = text;
	}

	public DidSaveTextDocumentParams(TextDocumentIdentifier textDocument, String text) {
		this.textDocument = textDocument;
		this.text = text;
	}

	public TextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
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
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((textDocument == null) ? 0 : textDocument.hashCode());
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
		DidSaveTextDocumentParams other = (DidSaveTextDocumentParams) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link DidSaveTextDocumentParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DidSaveTextDocumentParamsBuilder<P> extends DomainBuilder<DidSaveTextDocumentParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<DidSaveTextDocumentParamsBuilder<P>> textDocument();

		/**
		 * Sets a text.
		 *
		 * @param text the text
		 * @return the builder for chaining
		 */
		DidSaveTextDocumentParamsBuilder<P> text(String text);
	}

	/**
	 * Gets a builder for {@link DidSaveTextDocumentParams}
	 *
	 * @return the did save text document params builder
	 */
	public static <P> DidSaveTextDocumentParamsBuilder<P> didSaveTextDocumentParams() {
		return new InternalDidSaveTextDocumentParamsBuilder<>(null);
	}

	protected static <P> DidSaveTextDocumentParamsBuilder<P> didSaveTextDocumentParams(P parent) {
		return new InternalDidSaveTextDocumentParamsBuilder<>(parent);
	}

	private static class InternalDidSaveTextDocumentParamsBuilder<P> extends AbstractDomainBuilder<DidSaveTextDocumentParams, P>
			implements DidSaveTextDocumentParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<DidSaveTextDocumentParamsBuilder<P>> textDocument;
		private String text;

		InternalDidSaveTextDocumentParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<DidSaveTextDocumentParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public DidSaveTextDocumentParamsBuilder<P> text(String text) {
			this.text = text;
			return this;
		}

		@Override
		public DidSaveTextDocumentParams build() {
			DidSaveTextDocumentParams didSaveTextDocumentParams = new DidSaveTextDocumentParams(text);
			if (textDocument != null) {
				didSaveTextDocumentParams.setTextDocument(textDocument.build());
			}
			return didSaveTextDocumentParams;
		}
	}
}
