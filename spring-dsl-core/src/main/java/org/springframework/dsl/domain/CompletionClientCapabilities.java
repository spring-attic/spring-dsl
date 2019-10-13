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

import org.springframework.dsl.domain.CompletionItemCapabilities.CompletionItemCapabilitiesBuilder;
import org.springframework.dsl.domain.CompletionItemKindCapabilities.CompletionItemKindCapabilitiesBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class CompletionClientCapabilities {

	private Boolean dynamicRegistration;
	private CompletionItemCapabilities completionItem;
	private CompletionItemKindCapabilities completionItemKind;
	private Boolean contextSupport;

	public CompletionClientCapabilities() {

	}

	public CompletionClientCapabilities(Boolean dynamicRegistration, CompletionItemCapabilities completionItem,
			CompletionItemKindCapabilities completionItemKind, Boolean contextSupport) {
		this.dynamicRegistration = dynamicRegistration;
		this.completionItem = completionItem;
		this.completionItemKind = completionItemKind;
		this.contextSupport = contextSupport;
	}

	public Boolean getDynamicRegistration() {
		return dynamicRegistration;
	}

	public void setDynamicRegistration(Boolean dynamicRegistration) {
		this.dynamicRegistration = dynamicRegistration;
	}

	public CompletionItemCapabilities getCompletionItem() {
		return completionItem;
	}

	public void setCompletionItem(CompletionItemCapabilities completionItem) {
		this.completionItem = completionItem;
	}

	public CompletionItemKindCapabilities getCompletionItemKind() {
		return completionItemKind;
	}

	public void setCompletionItemKind(CompletionItemKindCapabilities completionItemKind) {
		this.completionItemKind = completionItemKind;
	}

	public Boolean getContextSupport() {
		return contextSupport;
	}

	public void setContextSupport(Boolean contextSupport) {
		this.contextSupport = contextSupport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((completionItem == null) ? 0 : completionItem.hashCode());
		result = prime * result + ((completionItemKind == null) ? 0 : completionItemKind.hashCode());
		result = prime * result + ((contextSupport == null) ? 0 : contextSupport.hashCode());
		result = prime * result + ((dynamicRegistration == null) ? 0 : dynamicRegistration.hashCode());
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
		CompletionClientCapabilities other = (CompletionClientCapabilities) obj;
		if (completionItem == null) {
			if (other.completionItem != null)
				return false;
		} else if (!completionItem.equals(other.completionItem))
			return false;
		if (completionItemKind == null) {
			if (other.completionItemKind != null)
				return false;
		} else if (!completionItemKind.equals(other.completionItemKind))
			return false;
		if (contextSupport == null) {
			if (other.contextSupport != null)
				return false;
		} else if (!contextSupport.equals(other.contextSupport))
			return false;
		if (dynamicRegistration == null) {
			if (other.dynamicRegistration != null)
				return false;
		} else if (!dynamicRegistration.equals(other.dynamicRegistration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompletionClientCapabilities [completionItem=" + completionItem + ", completionItemKind="
				+ completionItemKind + ", contextSupport=" + contextSupport + ", dynamicRegistration="
				+ dynamicRegistration + "]";
	}

	public interface CompletionClientCapabilitiesBuilder<P> extends DomainBuilder<CompletionClientCapabilities, P> {

		/**
		 * Sets a dynamicRegistration.
		 *
		 * @param dynamicRegistration the dynamicRegistration
		 * @return the builder for chaining
		 */
		CompletionClientCapabilitiesBuilder<P> dynamicRegistration(Boolean dynamicRegistration);

		/**
		 * Gets a builder for {@link CompletionItemCapabilities}.
		 *
		 * @return the completion item capabilities builder
		 */
		CompletionItemCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItem();

		/**
		 * Gets a builder for {@link CompletionClientCapabilities}.
		 *
		 * @return the completion item kind capabilities builder
		 */
		CompletionItemKindCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItemKind();

		/**
		 * Sets a contextSupport.
		 *
		 * @param contextSupport the contextSupport
		 * @return the builder for chaining
		 */
		CompletionClientCapabilitiesBuilder<P> contextSupport(Boolean contextSupport);
	}

	/**
	 * Gets a builder for {@link CompletionClientCapabilities}
	 *
	 * @return the completion client capabilities builder
	 */
	public static <P> CompletionClientCapabilitiesBuilder<P> completionClientCapabilities() {
		return new InternalCompletionClientCapabilitiesBuilder<>(null);
	}

	protected static <P> CompletionClientCapabilitiesBuilder<P> completionClientCapabilities(P parent) {
		return new InternalCompletionClientCapabilitiesBuilder<>(parent);
	}

	private static class InternalCompletionClientCapabilitiesBuilder<P>
			extends AbstractDomainBuilder<CompletionClientCapabilities, P> implements CompletionClientCapabilitiesBuilder<P> {

		private Boolean dynamicRegistration;
		private CompletionItemCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItemBuilder;
		private CompletionItemKindCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItemKindBuilder;
		private Boolean contextSupport;

		InternalCompletionClientCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public CompletionClientCapabilitiesBuilder<P> dynamicRegistration(Boolean dynamicRegistration) {
			this.dynamicRegistration = dynamicRegistration;
			return this;
		}

		@Override
		public CompletionItemCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItem() {
			this.completionItemBuilder = CompletionItemCapabilities.completionItemKindCapabilities(this);
			return completionItemBuilder;
		}

		@Override
		public CompletionItemKindCapabilitiesBuilder<CompletionClientCapabilitiesBuilder<P>> completionItemKind() {
			this.completionItemKindBuilder = CompletionItemKindCapabilities.completionItemKindCapabilities(this);
			return completionItemKindBuilder;
		}

		@Override
		public CompletionClientCapabilitiesBuilder<P> contextSupport(Boolean contextSupport) {
			this.contextSupport = contextSupport;
			return this;
		}

		@Override
		public CompletionClientCapabilities build() {
			CompletionClientCapabilities completionClientCapabilities = new CompletionClientCapabilities();
			completionClientCapabilities.setDynamicRegistration(dynamicRegistration);
			if (completionItemBuilder != null) {
				completionClientCapabilities.setCompletionItem(completionItemBuilder.build());
			}
			if (completionItemKindBuilder != null) {
				completionClientCapabilities.setCompletionItemKind(completionItemKindBuilder.build());
			}
			completionClientCapabilities.setContextSupport(contextSupport);
			return completionClientCapabilities;
		}
	}
}
