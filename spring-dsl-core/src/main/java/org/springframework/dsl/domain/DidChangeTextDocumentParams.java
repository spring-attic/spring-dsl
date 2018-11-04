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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.domain.TextDocumentContentChangeEvent.TextDocumentContentChangeEventBuilder;
import org.springframework.dsl.domain.VersionedTextDocumentIdentifier.VersionedTextDocumentIdentifierBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class DidChangeTextDocumentParams {

	private VersionedTextDocumentIdentifier textDocument;
	private List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<TextDocumentContentChangeEvent>();

	/**
	 * Gets the text document.
	 *
	 * @return the text document
	 */
	public VersionedTextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	/**
	 * Sets the text document.
	 *
	 * @param textDocument the new text document
	 */
	public void setTextDocument(VersionedTextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	public List<TextDocumentContentChangeEvent> getContentChanges() {
		return contentChanges;
	}

	public void setContentChanges(List<TextDocumentContentChangeEvent> contentChanges) {
		this.contentChanges = contentChanges;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentChanges == null) ? 0 : contentChanges.hashCode());
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
		DidChangeTextDocumentParams other = (DidChangeTextDocumentParams) obj;
		if (contentChanges == null) {
			if (other.contentChanges != null)
				return false;
		} else if (!contentChanges.equals(other.contentChanges))
			return false;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link DidChangeTextDocumentParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DidChangeTextDocumentParamsBuilder<P> extends DomainBuilder<DidChangeTextDocumentParams, P> {

		/**
		 * Gets a builder for start {@link VersionedTextDocumentIdentifier}.
		 *
		 * @return the versioned text document identifier builder
		 */
		VersionedTextDocumentIdentifierBuilder<DidChangeTextDocumentParamsBuilder<P>> textDocument();

		/**
		 * Gets a builder for start {@link TextDocumentContentChangeEvent}.
		 *
		 * @return the text document content change event builder
		 */
		TextDocumentContentChangeEventBuilder<DidChangeTextDocumentParamsBuilder<P>> contentChanges();
	}

	/**
	 * Gets a builder for {@link DidChangeTextDocumentParams}
	 *
	 * @return the range builder
	 */
	public static <P> DidChangeTextDocumentParamsBuilder<P> didChangeTextDocumentParams() {
		return new InternalDidChangeTextDocumentParamsBuilder<>(null);
	}

	protected static <P> DidChangeTextDocumentParamsBuilder<P> didChangeTextDocumentParams(P parent) {
		return new InternalDidChangeTextDocumentParamsBuilder<>(parent);
	}

	private static class InternalDidChangeTextDocumentParamsBuilder<P>
			extends AbstractDomainBuilder<DidChangeTextDocumentParams, P> implements DidChangeTextDocumentParamsBuilder<P> {

		private VersionedTextDocumentIdentifierBuilder<DidChangeTextDocumentParamsBuilder<P>> textDocument;
		private List<TextDocumentContentChangeEventBuilder<DidChangeTextDocumentParamsBuilder<P>>> contentChanges = new ArrayList<>();

		public InternalDidChangeTextDocumentParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public VersionedTextDocumentIdentifierBuilder<DidChangeTextDocumentParamsBuilder<P>> textDocument() {
			this.textDocument = VersionedTextDocumentIdentifier.versionedTextDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public TextDocumentContentChangeEventBuilder<DidChangeTextDocumentParamsBuilder<P>> contentChanges() {
			TextDocumentContentChangeEventBuilder<DidChangeTextDocumentParamsBuilder<P>> contentChange = TextDocumentContentChangeEvent
					.textDocumentContentChangeEvent(this);
			contentChanges.add(contentChange);
			return contentChange;
		}

		@Override
		public DidChangeTextDocumentParams build() {
			DidChangeTextDocumentParams params = new DidChangeTextDocumentParams();
			if (textDocument != null) {
				params.setTextDocument(textDocument.build());
			}
			List<TextDocumentContentChangeEvent> changes = new ArrayList<>();
			for (TextDocumentContentChangeEventBuilder<DidChangeTextDocumentParamsBuilder<P>> builder : contentChanges) {
				changes.add(builder.build());
			}
			params.setContentChanges(changes);
			return params;
		}
	}
}
