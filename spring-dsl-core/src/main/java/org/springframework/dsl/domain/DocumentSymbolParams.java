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
 * {@code LSP} domain object for a specification {@code DocumentSymbolParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class DocumentSymbolParams {

	private TextDocumentIdentifier textDocument;

	/**
	 * Instantiates a new document symbol params.
	 */
	public DocumentSymbolParams() {
	}

	/**
	 * Instantiates a new document symbol params.
	 *
	 * @param textDocument the text document
	 */
	public DocumentSymbolParams(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	public TextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((textDocument == null) ? 0 : textDocument.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DocumentSymbolParams other = (DocumentSymbolParams) obj;
		if (textDocument == null) {
			if (other.textDocument != null) {
				return false;
			}
		} else if (!textDocument.equals(other.textDocument)) {
			return false;
		}
		return true;
	}

	/**
	 * Builder interface for {@link DocumentSymbolParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DocumentSymbolParamsBuilder<P> extends DomainBuilder<DocumentSymbolParams, P> {

		/**
		 * Gets a builder for start {@code textDocument}.
		 *
		 * @return the text document identifier builder
		 */
		TextDocumentIdentifierBuilder<DocumentSymbolParamsBuilder<P>> textDocument();
	}

	/**
	 * Gets a builder for {@link DocumentSymbolParams}
	 *
	 * @return the document symbol params builder
	 */
	public static <P> DocumentSymbolParamsBuilder<P> documentSymbolParams() {
		return new InternalDocumentSymbolParamsBuilder<>(null);
	}

	protected static <P> DocumentSymbolParamsBuilder<P> documentSymbolParams(P parent) {
		return new InternalDocumentSymbolParamsBuilder<>(parent);
	}

	private static class InternalDocumentSymbolParamsBuilder<P>
		extends AbstractDomainBuilder<DocumentSymbolParams, P> implements DocumentSymbolParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<DocumentSymbolParamsBuilder<P>> textDocument;

		InternalDocumentSymbolParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<DocumentSymbolParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public DocumentSymbolParams build() {
			DocumentSymbolParams params = new DocumentSymbolParams();
			if (textDocument != null) {
				params.setTextDocument(textDocument.build());
			}
			return params;
		}
	}
}
