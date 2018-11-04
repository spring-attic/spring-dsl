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

import org.springframework.dsl.domain.TextDocumentClientCapabilities.TextDocumentClientCapabilitiesBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class ClientCapabilities {

	private TextDocumentClientCapabilities textDocument;
	private Object experimental;

	/**
	 * Instantiates a new client capabilities.
	 */
	public ClientCapabilities() {
	}

	/**
	 * Instantiates a new client capabilities.
	 *
	 * @param experimental the experimental
	 */
	public ClientCapabilities(Object experimental) {
		this.experimental = experimental;
	}

	public TextDocumentClientCapabilities getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(TextDocumentClientCapabilities textDocument) {
		this.textDocument = textDocument;
	}

	public Object getExperimental() {
		return experimental;
	}

	public void setExperimental(Object experimental) {
		this.experimental = experimental;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimental == null) ? 0 : experimental.hashCode());
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
		ClientCapabilities other = (ClientCapabilities) obj;
		if (experimental == null) {
			if (other.experimental != null)
				return false;
		} else if (!experimental.equals(other.experimental))
			return false;
		if (textDocument == null) {
			if (other.textDocument != null)
				return false;
		} else if (!textDocument.equals(other.textDocument))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link ClientCapabilities}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface ClientCapabilitiesBuilder<P> extends DomainBuilder<ClientCapabilities, P> {

		/**
		 * Sets a experimental.
		 *
		 * @param experimental the experimental
		 * @return the builder for chaining
		 */
		ClientCapabilitiesBuilder<P> experimental(Object experimental);

		TextDocumentClientCapabilitiesBuilder<ClientCapabilitiesBuilder<P>> textDocument();
	}

	/**
	 * Gets a builder for {@link ClientCapabilities}
	 *
	 * @return the client capabilities builder
	 */
	public static <P> ClientCapabilitiesBuilder<P> clientCapabilities() {
		return new InternalClientCapabilitiesBuilder<>(null);
	}

	protected static <P> ClientCapabilitiesBuilder<P> clientCapabilities(P parent) {
		return new InternalClientCapabilitiesBuilder<>(parent);
	}

	private static class InternalClientCapabilitiesBuilder<P> extends AbstractDomainBuilder<ClientCapabilities, P>
			implements ClientCapabilitiesBuilder<P> {

		private TextDocumentClientCapabilitiesBuilder<ClientCapabilitiesBuilder<P>> textDocument;
		private Object experimental;

		InternalClientCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextDocumentClientCapabilitiesBuilder<ClientCapabilitiesBuilder<P>> textDocument() {
			this.textDocument = TextDocumentClientCapabilities.textDocumentClientCapabilities(this);
			return textDocument;
		}

		@Override
		public ClientCapabilitiesBuilder<P> experimental(Object experimental) {
			this.experimental = experimental;
			return this;
		}

		@Override
		public ClientCapabilities build() {
			ClientCapabilities clientCapabilities = new ClientCapabilities(experimental);
			if (textDocument != null) {
				clientCapabilities.setTextDocument(textDocument.build());
			}
			return clientCapabilities;
		}
	}
}
