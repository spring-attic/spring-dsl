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
 * {@code LSP} domain object for a specification {@code RenameParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class RenameParams {

	private TextDocumentIdentifier textDocument;
	private Position position;
	private String newName;

	/**
	 * Instantiates a new rename params.
	 */
	public RenameParams() {
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

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newName == null) ? 0 : newName.hashCode());
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
		RenameParams other = (RenameParams) obj;
		if (newName == null) {
			if (other.newName != null) {
				return false;
			}
		} else if (!newName.equals(other.newName)) {
			return false;
		}
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
	 * Builder interface for {@link RenameParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RenameParamsBuilder<P> extends DomainBuilder<RenameParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<RenameParamsBuilder<P>> textDocument();

		/**
		 * Gets a position builder.
		 *
		 * @return the builder for chaining
		 */
		PositionBuilder<RenameParamsBuilder<P>> position();

		/**
		 * Sets a new name;
		 *
		 * @param newName the newName
		 * @return the builder for chaining
		 */
		RenameParamsBuilder<P> newName(String newName);
	}

	/**
	 * Gets a builder for {@link RenameParams}
	 *
	 * @return the rename params builder
	 */
	public static <P> RenameParamsBuilder<P> renameParams() {
		return new InternalRenameParamsBuilder<>(null);
	}

	protected static <P> RenameParamsBuilder<P> renameParams(P parent) {
		return new InternalRenameParamsBuilder<>(parent);
	}

	private static class InternalRenameParamsBuilder<P> extends AbstractDomainBuilder<RenameParams, P>
			implements RenameParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<RenameParamsBuilder<P>> textDocument;
		private PositionBuilder<RenameParamsBuilder<P>> position;
		private String newName;

		InternalRenameParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<RenameParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public PositionBuilder<RenameParamsBuilder<P>> position() {
			this.position = Position.position(this);
			return position;
		}

		@Override
		public RenameParamsBuilder<P> newName(String newName) {
			this.newName = newName;
			return this;
		}

		@Override
		public RenameParams build() {
			RenameParams renameParams = new RenameParams();
			if (textDocument != null) {
				renameParams.setTextDocument(textDocument.build());
			}
			if (position != null) {
				renameParams.setPosition(position.build());
			}
			renameParams.setNewName(newName);
			return renameParams;
		}
	}
}
