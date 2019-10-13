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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class CompletionItemKindCapabilities {

	private List<CompletionItemKind> valueSet;

	public CompletionItemKindCapabilities() {
	}

	public CompletionItemKindCapabilities(List<CompletionItemKind> valueSet) {
		this.valueSet = valueSet;
	}

	public List<CompletionItemKind> getValueSet() {
		return valueSet;
	}

	public void setValueSet(List<CompletionItemKind> valueSet) {
		this.valueSet = valueSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((valueSet == null) ? 0 : valueSet.hashCode());
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
		CompletionItemKindCapabilities other = (CompletionItemKindCapabilities) obj;
		if (valueSet == null) {
			if (other.valueSet != null)
				return false;
		} else if (!valueSet.equals(other.valueSet))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompletionItemKindCapabilities [valueSet=" + valueSet + "]";
	}

	public interface CompletionItemKindCapabilitiesBuilder<P> extends DomainBuilder<CompletionItemKindCapabilities, P> {

		/**
		 * Adds a new {@link CompletionItemKind} to a {@code valueSet}.
		 *
		 * @param kind the completion item kind
		 * @return the builder for chaining
		 */
		CompletionItemKindCapabilitiesBuilder<P> valueSet(CompletionItemKind kind);

		/**
		 * Sets a {@link List} of {@CompletionItemKind}'s' for {@code valueSet}.
		 *
		 * @param kinds the list of completion item kinds
		 * @return the builder for chaining
		 */
		CompletionItemKindCapabilitiesBuilder<P> valueSet(List<CompletionItemKind> kinds);
	}

	/**
	 * Gets a builder for {@link CompletionItemKindCapabilities}
	 *
	 * @return the completion item kind capabilities builder
	 */
	public static <P> CompletionItemKindCapabilitiesBuilder<P> completionItemKindCapabilities() {
		return new InternalCompletionItemKindCapabilitiesBuilder<>(null);
	}

	protected static <P> CompletionItemKindCapabilitiesBuilder<P> completionItemKindCapabilities(P parent) {
		return new InternalCompletionItemKindCapabilitiesBuilder<>(parent);
	}

	private static class InternalCompletionItemKindCapabilitiesBuilder<P>
			extends AbstractDomainBuilder<CompletionItemKindCapabilities, P> implements CompletionItemKindCapabilitiesBuilder<P> {

		private List<CompletionItemKind> kinds = new ArrayList<>();

		InternalCompletionItemKindCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public CompletionItemKindCapabilitiesBuilder<P> valueSet(CompletionItemKind kind) {
			kinds.add(kind);
			return this;
		}

		@Override
		public CompletionItemKindCapabilitiesBuilder<P> valueSet(List<CompletionItemKind> kinds) {
			this.kinds.clear();
			this.kinds.addAll(kinds);
			return this;
		}

		@Override
		public CompletionItemKindCapabilities build() {
			return new CompletionItemKindCapabilities(kinds);
		}
	}
}
