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
 * {@code LSP} domain object for a specification {@code TextDocumentSyncOptions}.
 *
 * @author Janne Valkealahti
 *
 */
public class TextDocumentSyncOptions {

	private Boolean openClose;
	private TextDocumentSyncKind change;
	private Boolean willSave;
	private Boolean willSaveWaitUntil;
	// TODO: save?: SaveOptions;

	public Boolean getOpenClose() {
		return openClose;
	}

	public void setOpenClose(Boolean openClose) {
		this.openClose = openClose;
	}
	
	public TextDocumentSyncKind getChange() {
		return change;
	}
	
	public void setChange(TextDocumentSyncKind change) {
		this.change = change;
	}

	public Boolean getWillSave() {
		return willSave;
	}

	public void setWillSave(Boolean willSave) {
		this.willSave = willSave;
	}

	public Boolean getWillSaveWaitUntil() {
		return willSaveWaitUntil;
	}

	public void setWillSaveWaitUntil(Boolean willSaveWaitUntil) {
		this.willSaveWaitUntil = willSaveWaitUntil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((openClose == null) ? 0 : openClose.hashCode());
		result = prime * result + ((willSave == null) ? 0 : willSave.hashCode());
		result = prime * result + ((willSaveWaitUntil == null) ? 0 : willSaveWaitUntil.hashCode());
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
		TextDocumentSyncOptions other = (TextDocumentSyncOptions) obj;
		if (openClose == null) {
			if (other.openClose != null)
				return false;
		} else if (!openClose.equals(other.openClose))
			return false;
		if (willSave == null) {
			if (other.willSave != null)
				return false;
		} else if (!willSave.equals(other.willSave))
			return false;
		if (willSaveWaitUntil == null) {
			if (other.willSaveWaitUntil != null)
				return false;
		} else if (!willSaveWaitUntil.equals(other.willSaveWaitUntil))
			return false;
		return true;
	}
	
	/**
	 * Builder interface for {@link TextDocumentSyncOptions}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextDocumentSyncOptionsBuilder<P> extends DomainBuilder<TextDocumentSyncOptions, P> {

		/**
		 * Sets if OpenClose is enabled.
		 *
		 * @param openClose {@code true} if OpenClose is supported
		 * @return the builder for chaining
		 */		
		TextDocumentSyncOptionsBuilder<P> openClose(Boolean openClose);

		/**
		 * Sets if change is enabled.
		 *
		 * @param change value
		 * @return the builder for chaining
		 */		
		TextDocumentSyncOptionsBuilder<P> change(TextDocumentSyncKind change);

		/**
		 * Sets if willSave is enabled.
		 *
		 * @param willSave {@code true} if willSave is supported
		 * @return the builder for chaining
		 */		
		TextDocumentSyncOptionsBuilder<P> willSave(Boolean willSave);

		/**
		 * Sets if willSaveWaitUntil is enabled.
		 *
		 * @param willSaveWaitUntil {@code true} if willSaveWaitUntil is supported
		 * @return the builder for chaining
		 */		
		TextDocumentSyncOptionsBuilder<P> willSaveWaitUntil(Boolean willSaveWaitUntil);
	}

	/**
	 * Gets a builder for {@link TextDocumentSyncOptions}.
	 *
	 * @return the text document sync options builder
	 */
	public static <P> TextDocumentSyncOptionsBuilder<P> textDocumentSyncOptions() {
		return new InternalTextDocumentSyncOptionsBuilder<>(null);
	}

	protected static <P> TextDocumentSyncOptionsBuilder<P> textDocumentSyncOptions(P parent) {
		return new InternalTextDocumentSyncOptionsBuilder<>(parent);
	}
	
	private static class InternalTextDocumentSyncOptionsBuilder<P> extends AbstractDomainBuilder<TextDocumentSyncOptions, P>
			implements TextDocumentSyncOptionsBuilder<P> {

		private Boolean openClose;
		private TextDocumentSyncKind change;
		private Boolean willSave;
		private Boolean willSaveWaitUntil;
		
		InternalTextDocumentSyncOptionsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentSyncOptionsBuilder<P> openClose(Boolean openClose) {
			this.openClose = openClose;
			return this;
		}
		
		@Override
		public TextDocumentSyncOptionsBuilder<P> change(TextDocumentSyncKind change) {
			this.change = change;
			return this;
		}
		
		@Override
		public TextDocumentSyncOptionsBuilder<P> willSave(Boolean willSave) {
			this.willSave = willSave;
			return this;
		}
		
		@Override
		public TextDocumentSyncOptionsBuilder<P> willSaveWaitUntil(Boolean willSaveWaitUntil) {
			this.willSaveWaitUntil = willSaveWaitUntil;
			return this;
		}

		@Override
		public TextDocumentSyncOptions build() {
			TextDocumentSyncOptions textDocumentSyncOptions = new TextDocumentSyncOptions();
			textDocumentSyncOptions.setOpenClose(openClose);
			textDocumentSyncOptions.setChange(change);
			textDocumentSyncOptions.setWillSave(willSave);
			textDocumentSyncOptions.setWillSaveWaitUntil(willSaveWaitUntil);
			return textDocumentSyncOptions;
		}
	}
}
