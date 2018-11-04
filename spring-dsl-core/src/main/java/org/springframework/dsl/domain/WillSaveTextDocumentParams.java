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
public class WillSaveTextDocumentParams {

	private TextDocumentIdentifier textDocument;
	private TextDocumentSaveReason reason;

	public WillSaveTextDocumentParams() {
	}

	public WillSaveTextDocumentParams(TextDocumentSaveReason reason) {
		this.reason = reason;
	}

	public WillSaveTextDocumentParams(TextDocumentIdentifier textDocument, TextDocumentSaveReason reason) {
		this.textDocument = textDocument;
		this.reason = reason;
	}

	public TextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	public TextDocumentSaveReason getReason() {
		return reason;
	}

	public void setReason(TextDocumentSaveReason reason) {
		this.reason = reason;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
		WillSaveTextDocumentParams other = (WillSaveTextDocumentParams) obj;
		if (reason != other.reason)
			return false;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link WillSaveTextDocumentParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface WillSaveTextDocumentParamsBuilder<P> extends DomainBuilder<WillSaveTextDocumentParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<WillSaveTextDocumentParamsBuilder<P>> textDocument();

		/**
		 * Sets a text document save reason.
		 *
		 * @param reason the reason
		 * @return the builder for chaining
		 */
		WillSaveTextDocumentParamsBuilder<P> textDocumentSaveReason(TextDocumentSaveReason reason);
	}

	/**
	 * Gets a builder for {@link WillSaveTextDocumentParams}
	 *
	 * @return the WillSaveTextDocumentParams builder
	 */
	public static <P> WillSaveTextDocumentParamsBuilder<P> willSaveTextDocumentParams() {
		return new InternalWillSaveTextDocumentParamsBuilder<>(null);
	}

	protected static <P> WillSaveTextDocumentParamsBuilder<P> willSaveTextDocumentParams(P parent) {
		return new InternalWillSaveTextDocumentParamsBuilder<>(parent);
	}

	private static class InternalWillSaveTextDocumentParamsBuilder<P> extends AbstractDomainBuilder<WillSaveTextDocumentParams, P>
			implements WillSaveTextDocumentParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<WillSaveTextDocumentParamsBuilder<P>> textDocument;
		private TextDocumentSaveReason reason;

		InternalWillSaveTextDocumentParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<WillSaveTextDocumentParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public WillSaveTextDocumentParamsBuilder<P> textDocumentSaveReason(TextDocumentSaveReason reason) {
			this.reason = reason;
			return this;
		}

		@Override
		public WillSaveTextDocumentParams build() {
			WillSaveTextDocumentParams willSaveTextDocumentParams = new WillSaveTextDocumentParams(reason);
			if (textDocument != null) {
				willSaveTextDocumentParams.setTextDocument(textDocument.build());
			}
			return willSaveTextDocumentParams;
		}
	}
}
