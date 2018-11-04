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

import org.springframework.dsl.domain.CompletionOptions.CompletionOptionsBuilder;
import org.springframework.dsl.domain.TextDocumentSyncOptions.TextDocumentSyncOptionsBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code ServerCapabilities}.
 * <p>
 * In a spec field {@code textDocumentSync} can be either
 * {@code TextDocumentSyncOptions} matching {@link TextDocumentSyncOptions} or a
 * number matching ordinal in an enum {@link TextDocumentSyncKind}.
 *
 * @author Janne Valkealahti
 * @see TextDocumentSyncOptions
 * @see TextDocumentSyncKind
 *
 */
public class ServerCapabilities {

	private TextDocumentSyncOptions textDocumentSyncOptions;
	private TextDocumentSyncKind textDocumentSyncKind;
	private Boolean hoverProvider;
	private Boolean renameProvider;
	private CompletionOptions completionProvider;
	private Boolean documentSymbolProvider;

	public ServerCapabilities() {
	}

	public ServerCapabilities(TextDocumentSyncKind textDocumentSyncKind) {
		this.textDocumentSyncKind = textDocumentSyncKind;
	}

	public ServerCapabilities(TextDocumentSyncOptions textDocumentSyncOptions) {
		this.textDocumentSyncOptions = textDocumentSyncOptions;
	}

	public TextDocumentSyncOptions getTextDocumentSyncOptions() {
		return textDocumentSyncOptions;
	}

	public void setTextDocumentSyncOptions(TextDocumentSyncOptions textDocumentSyncOptions) {
		this.textDocumentSyncOptions = textDocumentSyncOptions;
	}

	public TextDocumentSyncKind getTextDocumentSyncKind() {
		return textDocumentSyncKind;
	}

	public void setTextDocumentSyncKind(TextDocumentSyncKind textDocumentSyncKind) {
		this.textDocumentSyncKind = textDocumentSyncKind;
	}

	public Boolean getHoverProvider() {
		return hoverProvider;
	}

	public void setHoverProvider(Boolean hoverProvider) {
		this.hoverProvider = hoverProvider;
	}

	public Boolean getDocumentSymbolProvider() {
		return documentSymbolProvider;
	}

	public void setDocumentSymbolProvider(Boolean documentSymbolProvider) {
		this.documentSymbolProvider = documentSymbolProvider;
	}

	public CompletionOptions getCompletionProvider() {
		return completionProvider;
	}

	public void setCompletionProvider(CompletionOptions completionProvider) {
		this.completionProvider = completionProvider;
	}

	public Boolean getRenameProvider() {
		return renameProvider;
	}

	public void setRenameProvider(Boolean renameProvider) {
		this.renameProvider = renameProvider;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((completionProvider == null) ? 0 : completionProvider.hashCode());
		result = prime * result + ((documentSymbolProvider == null) ? 0 : documentSymbolProvider.hashCode());
		result = prime * result + ((hoverProvider == null) ? 0 : hoverProvider.hashCode());
		result = prime * result + ((renameProvider == null) ? 0 : renameProvider.hashCode());
		result = prime * result + ((textDocumentSyncKind == null) ? 0 : textDocumentSyncKind.hashCode());
		result = prime * result + ((textDocumentSyncOptions == null) ? 0 : textDocumentSyncOptions.hashCode());
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
		ServerCapabilities other = (ServerCapabilities) obj;
		if (completionProvider == null) {
			if (other.completionProvider != null) {
				return false;
			}
		} else if (!completionProvider.equals(other.completionProvider)) {
			return false;
		}
		if (documentSymbolProvider == null) {
			if (other.documentSymbolProvider != null) {
				return false;
			}
		} else if (!documentSymbolProvider.equals(other.documentSymbolProvider)) {
			return false;
		}
		if (hoverProvider == null) {
			if (other.hoverProvider != null) {
				return false;
			}
		} else if (!hoverProvider.equals(other.hoverProvider)) {
			return false;
		}
		if (renameProvider == null) {
			if (other.renameProvider != null) {
				return false;
			}
		} else if (!renameProvider.equals(other.renameProvider)) {
			return false;
		}
		if (textDocumentSyncKind != other.textDocumentSyncKind) {
			return false;
		}
		if (textDocumentSyncOptions == null) {
			if (other.textDocumentSyncOptions != null) {
				return false;
			}
		} else if (!textDocumentSyncOptions.equals(other.textDocumentSyncOptions)) {
			return false;
		}
		return true;
	}

	/**
	 * Builder interface for {@link ServerCapabilities}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface ServerCapabilitiesBuilder<P> extends DomainBuilder<ServerCapabilities, P> {

		/**
		 * Gets the TextDocumentSyncOptionsBuilder.
		 *
		 * @return the builder for chaining
		 */
		TextDocumentSyncOptionsBuilder<ServerCapabilitiesBuilder<P>> textDocumentSyncOptions();

		/**
		 * Gets the TextDocumentSyncOptionsBuilder.
		 *
		 * @param enabled flag if this feature is actually enabled
		 * @return the builder for chaining
		 */
		TextDocumentSyncOptionsBuilder<ServerCapabilitiesBuilder<P>> textDocumentSyncOptions(boolean enabled);

		/**
		 * Sets the {@link TextDocumentSyncKind}.
		 *
		 * @param textDocumentSyncKind the text document sync kind
		 * @return the builder for chaining
		 */
		ServerCapabilitiesBuilder<P> textDocumentSyncKind(TextDocumentSyncKind textDocumentSyncKind);

		/**
		 * Sets if {@code hoverProvider} is enabled.
		 *
		 * @param hoverProvider the provider enabler flag
		 * @return the builder for chaining
		 */
		ServerCapabilitiesBuilder<P> hoverProvider(Boolean hoverProvider);

		/**
		 * Sets if {@code renameProvider} is enabled.
		 *
		 * @param renameProvider the provider enabled flag
		 * @return the builder for chaining
		 */
		ServerCapabilitiesBuilder<P> renameProvider(Boolean renameProvider);

		/**
		 * Sets if {@code documentSymbolProvider} is enabled.
		 *
		 * @param documentSymbolProvider the provider enabler flag
		 * @return the builder for chaining
		 */
		ServerCapabilitiesBuilder<P> documentSymbolProvider(Boolean documentSymbolProvider);

		/**
		 * Gets a builder for a {@link CompletionOptions}. Same as calling
		 * {@link #completionProvider(boolean)} with {@code true}.
		 *
		 * @return the builder for chaining
		 * @see #completionProvider(boolean)
		 */
		CompletionOptionsBuilder<ServerCapabilitiesBuilder<P>> completionProvider();

		/**
		 * Gets a builder for a {@link CompletionOptions}. Setting {@code enabled} to
		 * {@code true} effectively disables builder.
		 *
		 * @param enabled the flag to disable whole builder
		 * @return the builder for chaining
		 */
		CompletionOptionsBuilder<ServerCapabilitiesBuilder<P>> completionProvider(boolean enabled);
	}

	/**
	 * Gets a builder for {@link ServerCapabilities}
	 *
	 * @return the server capabilities builder
	 */
	public static <P> ServerCapabilitiesBuilder<P> serverCapabilities() {
		return new InternalServerCapabilitiesBuilder<>(null);
	}

	protected static <P> ServerCapabilitiesBuilder<P> serverCapabilities(P parent) {
		return new InternalServerCapabilitiesBuilder<>(parent);
	}

	private static class InternalServerCapabilitiesBuilder<P>
			extends AbstractDomainBuilder<ServerCapabilities, P> implements ServerCapabilitiesBuilder<P> {

		private TextDocumentSyncOptionsBuilder<ServerCapabilitiesBuilder<P>> textDocumentSyncOptions;
		private TextDocumentSyncKind textDocumentSyncKind;
		private Boolean hoverProvider;
		private Boolean renameProvider;
		private Boolean documentSymbolProvider;
		private CompletionOptionsBuilder<ServerCapabilitiesBuilder<P>> completionProvider;

		InternalServerCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentSyncOptionsBuilder<ServerCapabilitiesBuilder<P>> textDocumentSyncOptions() {
			return textDocumentSyncOptions(true);
		}

		@Override
		public TextDocumentSyncOptionsBuilder<ServerCapabilitiesBuilder<P>> textDocumentSyncOptions(boolean enabled) {
			if (enabled) {
				this.textDocumentSyncOptions = TextDocumentSyncOptions.textDocumentSyncOptions(this);
				return textDocumentSyncOptions;
			} else {
				return TextDocumentSyncOptions.textDocumentSyncOptions(this);
			}
		}

		@Override
		public ServerCapabilitiesBuilder<P> textDocumentSyncKind(TextDocumentSyncKind textDocumentSyncKind) {
			this.textDocumentSyncKind = textDocumentSyncKind;
			return this;
		}

		@Override
		public ServerCapabilitiesBuilder<P> hoverProvider(Boolean hoverProvider) {
			this.hoverProvider = hoverProvider;
			return this;
		}

		@Override
		public ServerCapabilitiesBuilder<P> renameProvider(Boolean renameProvider) {
			this.renameProvider = renameProvider;
			return this;
		}

		@Override
		public ServerCapabilitiesBuilder<P> documentSymbolProvider(Boolean documentSymbolProvider) {
			this.documentSymbolProvider = documentSymbolProvider;
			return this;
		}

		@Override
		public CompletionOptionsBuilder<ServerCapabilitiesBuilder<P>> completionProvider() {
			return completionProvider(true);
		}

		@Override
		public CompletionOptionsBuilder<ServerCapabilitiesBuilder<P>> completionProvider(boolean enabled) {
			if (enabled) {
				this.completionProvider = CompletionOptions.completionOptions(this);
				return completionProvider;
			} else {
				return CompletionOptions.completionOptions(this);
			}
		}

		@Override
		public ServerCapabilities build() {
			ServerCapabilities serverCapabilities = new ServerCapabilities();
			if (textDocumentSyncOptions != null) {
				serverCapabilities.setTextDocumentSyncOptions(textDocumentSyncOptions.build());
			} else {
				serverCapabilities.setTextDocumentSyncKind(textDocumentSyncKind);
			}
			serverCapabilities.setHoverProvider(hoverProvider);
			serverCapabilities.setRenameProvider(renameProvider);
			serverCapabilities.setDocumentSymbolProvider(documentSymbolProvider);
			if (completionProvider != null) {
				serverCapabilities.setCompletionProvider(completionProvider.build());
			}
			return serverCapabilities;
		}
	}
}
