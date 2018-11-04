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

import org.springframework.dsl.domain.Synchronization.SynchronizationBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class TextDocumentClientCapabilities {

	private Synchronization synchronization;

	public TextDocumentClientCapabilities() {
	}

	public Synchronization getSynchronization() {
		return synchronization;
	}

	public void setSynchronization(Synchronization synchronization) {
		this.synchronization = synchronization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((synchronization == null) ? 0 : synchronization.hashCode());
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
		TextDocumentClientCapabilities other = (TextDocumentClientCapabilities) obj;
		if (synchronization == null) {
			if (other.synchronization != null)
				return false;
		} else if (!synchronization.equals(other.synchronization))
			return false;
		return true;
	}

	public interface TextDocumentClientCapabilitiesBuilder<P> extends DomainBuilder<TextDocumentClientCapabilities, P> {

		SynchronizationBuilder<TextDocumentClientCapabilitiesBuilder<P>> synchronization();
	}

	public static <P> TextDocumentClientCapabilitiesBuilder<P> textDocumentClientCapabilities() {
		return new InternalTextDocumentClientCapabilitiesBuilder<>(null);
	}

	protected static <P> TextDocumentClientCapabilitiesBuilder<P> textDocumentClientCapabilities(P parent) {
		return new InternalTextDocumentClientCapabilitiesBuilder<>(parent);
	}

	private static class InternalTextDocumentClientCapabilitiesBuilder<P>
			extends AbstractDomainBuilder<TextDocumentClientCapabilities, P> implements TextDocumentClientCapabilitiesBuilder<P> {

		private SynchronizationBuilder<TextDocumentClientCapabilitiesBuilder<P>> synchronization;

		InternalTextDocumentClientCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public SynchronizationBuilder<TextDocumentClientCapabilitiesBuilder<P>> synchronization() {
			this.synchronization = Synchronization.synchronization(this);
			return synchronization;
		}

		@Override
		public TextDocumentClientCapabilities build() {
			TextDocumentClientCapabilities textDocumentClientCapabilities = new TextDocumentClientCapabilities();
			if (synchronization != null) {
				textDocumentClientCapabilities.setSynchronization(synchronization.build());
			}
			return textDocumentClientCapabilities;
		}
	}
}
