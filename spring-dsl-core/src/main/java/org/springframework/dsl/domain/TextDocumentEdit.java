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

import org.springframework.dsl.domain.TextEdit.TextEditBuilder;
import org.springframework.dsl.domain.VersionedTextDocumentIdentifier.VersionedTextDocumentIdentifierBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code TextDocumentEdit}.
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentEdit {

	private VersionedTextDocumentIdentifier textDocument;
	private List<TextEdit> edits = new ArrayList<TextEdit>();

	public TextDocumentEdit() {
	}

	public VersionedTextDocumentIdentifier getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(VersionedTextDocumentIdentifier textDocument) {
		this.textDocument = textDocument;
	}

	public List<TextEdit> getEdits() {
		return edits;
	}

	public void setEdits(List<TextEdit> edits) {
		this.edits = edits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edits == null) ? 0 : edits.hashCode());
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
		TextDocumentEdit other = (TextDocumentEdit) obj;
		if (edits == null) {
			if (other.edits != null) {
				return false;
			}
		} else if (!edits.equals(other.edits)) {
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
	 * Builder interface for {@link TextDocumentEdit}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextDocumentEditBuilder<P> extends DomainBuilder<TextDocumentEdit, P> {

		/**
		 * Gets a builder for {@link VersionedTextDocumentIdentifier}.
		 *
		 * @return the versioned text document identifier builder
		 */
		VersionedTextDocumentIdentifierBuilder<TextDocumentEditBuilder<P>> textDocument();

		/**
		 * Gets a builder for {@link TextEdit}.
		 *
		 * @return the text edit builder
		 */
		TextEditBuilder<TextDocumentEditBuilder<P>> edits();
	}

	/**
	 * Gets a builder for {@link TextDocumentEdit}.
	 *
	 * @return the text document edit builder
	 */
	public static <P> TextDocumentEditBuilder<P> textDocumentEdit() {
		return new InternalTextDocumentEditBuilder<>(null);
	}

	protected static <P> TextDocumentEditBuilder<P> textDocumentEdit(P parent) {
		return new InternalTextDocumentEditBuilder<>(parent);
	}

	private static class InternalTextDocumentEditBuilder<P> extends
			AbstractDomainBuilder<TextDocumentEdit, P> implements TextDocumentEditBuilder<P> {

		private VersionedTextDocumentIdentifierBuilder<TextDocumentEditBuilder<P>> textDocument;
		private List<TextEditBuilder<TextDocumentEditBuilder<P>>> edits = new ArrayList<>();

		public InternalTextDocumentEditBuilder(P parent) {
			super(parent);
		}

		@Override
		public VersionedTextDocumentIdentifierBuilder<TextDocumentEditBuilder<P>> textDocument() {
			this.textDocument = VersionedTextDocumentIdentifier.versionedTextDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public TextEditBuilder<TextDocumentEditBuilder<P>> edits() {
			 TextEditBuilder<TextDocumentEditBuilder<P>> edit = TextEdit.textEdit(this);
			 edits.add(edit);
			 return edit;
		}

		@Override
		public TextDocumentEdit build() {
			TextDocumentEdit textDocumentEdit = new TextDocumentEdit();
			if (textDocument != null) {
				textDocumentEdit.setTextDocument(textDocument.build());
			}
			List<TextEdit> textEdits = new ArrayList<>();
			for (TextEditBuilder<TextDocumentEditBuilder<P>> builder : edits) {
				textEdits.add(builder.build());
			}
			textDocumentEdit.setEdits(textEdits);
			return textDocumentEdit;
		}
	}
}
