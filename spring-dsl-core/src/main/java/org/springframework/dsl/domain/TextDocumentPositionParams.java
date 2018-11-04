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

import org.springframework.dsl.domain.Position.PositionBuilder;
import org.springframework.dsl.domain.TextDocumentIdentifier.TextDocumentIdentifierBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code TextDocumentPositionParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentPositionParams {

	private TextDocumentIdentifier textDocument;
	private Position position;

	/**
	 * Instantiates a new TextDocumentPositionParams params.
	 */
	public TextDocumentPositionParams() {
	}

	public TextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		TextDocumentPositionParams other = (TextDocumentPositionParams) obj;
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!position.equals(other.position)) {
			return false;
		}
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
	 * Builder interface for {@link TextDocumentPositionParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextDocumentPositionParamsParamsBuilder<P> extends DomainBuilder<TextDocumentPositionParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<TextDocumentPositionParamsParamsBuilder<P>> textDocument();

		/**
		 * Gets a position builder.
		 *
		 * @return the builder for chaining
		 */
		PositionBuilder<TextDocumentPositionParamsParamsBuilder<P>> position();
	}

	/**
	 * Gets a builder for {@link TextDocumentPositionParams}
	 *
	 * @return the TextDocumentPositionParams params builder
	 */
	public static <P> TextDocumentPositionParamsParamsBuilder<P> textDocumentPositionParams() {
		return new InternalTextDocumentPositionParamsParamsBuilder<>(null);
	}

	protected static <P> TextDocumentPositionParamsParamsBuilder<P> textDocumentPositionParams(P parent) {
		return new InternalTextDocumentPositionParamsParamsBuilder<>(parent);
	}

	private static class InternalTextDocumentPositionParamsParamsBuilder<P> extends AbstractDomainBuilder<TextDocumentPositionParams, P>
			implements TextDocumentPositionParamsParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<TextDocumentPositionParamsParamsBuilder<P>> textDocument;
		private PositionBuilder<TextDocumentPositionParamsParamsBuilder<P>> position;

		InternalTextDocumentPositionParamsParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<TextDocumentPositionParamsParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public PositionBuilder<TextDocumentPositionParamsParamsBuilder<P>> position() {
			this.position = Position.position(this);
			return position;
		}

		@Override
		public TextDocumentPositionParams build() {
			TextDocumentPositionParams textDocumentPositionParamsParams = new TextDocumentPositionParams();
			if (textDocument != null) {
				textDocumentPositionParamsParams.setTextDocument(textDocument.build());
			}
			if (position != null) {
				textDocumentPositionParamsParams.setPosition(position.build());
			}
			return textDocumentPositionParamsParams;
		}
	}
}
