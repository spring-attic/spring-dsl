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

import org.springframework.dsl.domain.TextDocumentItem.TextDocumentItemBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class DidOpenTextDocumentParams {

	private TextDocumentItem textDocument;

	public DidOpenTextDocumentParams() {
	}

	public DidOpenTextDocumentParams(TextDocumentItem textDocument) {
		this.textDocument = textDocument;
	}

	public TextDocumentItem getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentItem textDocument) {
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
		DidOpenTextDocumentParams other = (DidOpenTextDocumentParams) obj;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link DidOpenTextDocumentParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DidOpenTextDocumentParamsBuilder<P> extends DomainBuilder<DidOpenTextDocumentParams, P> {

		/**
		 * Gets a builder for {@link TextDocumentItemBuilder}.
		 *
		 * @return the text document identifier builder
		 */
		TextDocumentItemBuilder<DidOpenTextDocumentParamsBuilder<P>> textDocument();
	}

	/**
	 * Gets a builder for {@link DidOpenTextDocumentParams}
	 *
	 * @return the range builder
	 */
	public static <P> DidOpenTextDocumentParamsBuilder<P> didOpenTextDocumentParams() {
		return new InternalDidOpenTextDocumentParamsBuilder<>(null);
	}

	protected static <P> DidOpenTextDocumentParamsBuilder<P> didOpenTextDocumentParams(P parent) {
		return new InternalDidOpenTextDocumentParamsBuilder<>(parent);
	}

	private static class InternalDidOpenTextDocumentParamsBuilder<P>
			extends AbstractDomainBuilder<DidOpenTextDocumentParams, P> implements DidOpenTextDocumentParamsBuilder<P> {

		private TextDocumentItemBuilder<DidOpenTextDocumentParamsBuilder<P>> textDocument;

		public InternalDidOpenTextDocumentParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentItemBuilder<DidOpenTextDocumentParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentItem.textDocumentItem(this);
			return textDocument;
		}

		@Override
		public DidOpenTextDocumentParams build() {
			DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
			if (textDocument != null) {
				params.setTextDocument(textDocument.build());
			}
			return params;
		}
	}
}
