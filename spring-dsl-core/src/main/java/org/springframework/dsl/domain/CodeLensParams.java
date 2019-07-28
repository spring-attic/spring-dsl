/*
 * Copyright 2019 the original author or authors.
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
 * {@code LSP} domain object for a specification {@code CodeLensParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class CodeLensParams {

	private TextDocumentIdentifier textDocument;

	/**
	 * Instantiates a new CodeLensParams params.
	 */
	public CodeLensParams() {
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
		CodeLensParams other = (CodeLensParams) obj;
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
	 * Builder interface for {@link CodeLensParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CodeLensParamsBuilder<P> extends DomainBuilder<CodeLensParams, P> {

		/**
		 * Gets a text document identifier builder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentIdentifierBuilder<CodeLensParamsBuilder<P>> textDocument();
	}

	/**
	 * Gets a builder for {@link CodeLensParams}
	 *
	 * @return the CodeLensParams params builder
	 */
	public static <P> CodeLensParamsBuilder<P> codeLensParams() {
		return new InternalCodeLensParamsBuilder<>(null);
	}

	protected static <P> CodeLensParamsBuilder<P> codeLensParams(P parent) {
		return new InternalCodeLensParamsBuilder<>(parent);
	}

	private static class InternalCodeLensParamsBuilder<P> extends AbstractDomainBuilder<CodeLensParams, P>
			implements CodeLensParamsBuilder<P> {

		private TextDocumentIdentifierBuilder<CodeLensParamsBuilder<P>> textDocument;

		InternalCodeLensParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentIdentifierBuilder<CodeLensParamsBuilder<P>> textDocument() {
			this.textDocument = TextDocumentIdentifier.textDocumentIdentifier(this);
			return textDocument;
		}

		@Override
		public CodeLensParams build() {
			CodeLensParams CodeLensParams = new CodeLensParams();
			if (textDocument != null) {
				CodeLensParams.setTextDocument(textDocument.build());
			}
			return CodeLensParams;
		}
	}
}
