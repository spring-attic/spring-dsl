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

import org.springframework.dsl.domain.TextDocumentIdentifier.TextDocumentIdentifierBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code FoldingRangeParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class FoldingRangeParams {

	private TextDocumentIdentifier textDocument;

	/**
	 * Instantiates a new folding range params.
	 */
	public FoldingRangeParams() {
	}


	/**
	 * Instantiates a new folding range params.
	 *
	 * @param textDocument the text document identifier
	 */
	public FoldingRangeParams(TextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	/**
	 * Gets a {@link TextDocumentIdentifier}.
	 *
	 * @return the text document identifier
	 */
	public TextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	/**
	 * Sets a {@link TextDocumentIdentifier}.
	 *
	 * @param textDocument the text document identifier
	 */
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoldingRangeParams other = (FoldingRangeParams) obj;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FoldingRangeParams [textDocument=" + textDocument + "]";
	}

	/**
	 * Builder interface for {@link FoldingRangeParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface FoldingRangeParamsBuilder<P> extends DomainBuilder<FoldingRangeParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<FoldingRangeParamsBuilder<P>> textDocument();
	}

	/**
	 * Gets a builder for {@link FoldingRangeParams}
	 *
	 * @return the folding range params builder
	 */
	public static <P> FoldingRangeParamsBuilder<P> foldingRangeParams() {
		return new InternalFoldingRangeParamsBuilder<>(null);
	}

	protected static <P> FoldingRangeParamsBuilder<P> foldingRangeParams(P parent) {
		return new InternalFoldingRangeParamsBuilder<>(parent);
	}

	private static class InternalFoldingRangeParamsBuilder<P> extends AbstractDomainBuilder<FoldingRangeParams, P>
			implements FoldingRangeParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<FoldingRangeParamsBuilder<P>> textDocument;

		InternalFoldingRangeParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<FoldingRangeParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public FoldingRangeParams build() {
			FoldingRangeParams FoldingRangeParams = new FoldingRangeParams();
			if (textDocument != null) {
				FoldingRangeParams.setTextDocument(textDocument.build());
			}
			return FoldingRangeParams;
		}
	}
}
