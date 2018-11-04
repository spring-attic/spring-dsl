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

import java.util.List;

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code CompletionOptions}.
 *
 * @author Janne Valkealahti
 *
 */
public class CompletionOptions {

	private Boolean resolveProvider;
	private List<String> triggerCharacters;

	public Boolean getResolveProvider() {
		return resolveProvider;
	}

	public void setResolveProvider(Boolean resolveProvider) {
		this.resolveProvider = resolveProvider;
	}

	public List<String> getTriggerCharacters() {
		return triggerCharacters;
	}

	public void setTriggerCharacters(List<String> triggerCharacters) {
		this.triggerCharacters = triggerCharacters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resolveProvider == null) ? 0 : resolveProvider.hashCode());
		result = prime * result + ((triggerCharacters == null) ? 0 : triggerCharacters.hashCode());
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
		CompletionOptions other = (CompletionOptions) obj;
		if (resolveProvider == null) {
			if (other.resolveProvider != null)
				return false;
		} else if (!resolveProvider.equals(other.resolveProvider))
			return false;
		if (triggerCharacters == null) {
			if (other.triggerCharacters != null)
				return false;
		} else if (!triggerCharacters.equals(other.triggerCharacters))
			return false;
		return true;
	}
	
	/**
	 * Builder interface for {@link CompletionOptions}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CompletionOptionsBuilder<P> extends DomainBuilder<CompletionOptions, P> {

		/**
		 * Sets if resolve provider is enabled.
		 *
		 * @param resolveProvider {@code true} if resolve provider is supported
		 * @return the builder for chaining
		 */		
		CompletionOptionsBuilder<P> resolveProvider(Boolean resolveProvider);

		/**
		 * Sets a trigger characters.
		 *
		 * @param triggerCharacters the trigger characters
		 * @return the builder for chaining
		 */		
		CompletionOptionsBuilder<P> triggerCharacters(List<String> triggerCharacters);		
	}

	/**
	 * Gets a builder for {@link CompletionOptions}.
	 *
	 * @return the completion options builder
	 */
	public static <P> CompletionOptionsBuilder<P> completionOptions() {
		return new InternalCompletionOptionsBuilder<>(null);
	}

	protected static <P> CompletionOptionsBuilder<P> completionOptions(P parent) {
		return new InternalCompletionOptionsBuilder<>(parent);
	}
	
	private static class InternalCompletionOptionsBuilder<P> extends AbstractDomainBuilder<CompletionOptions, P>
			implements CompletionOptionsBuilder<P> {

		private Boolean resolveProvider;
		private List<String> triggerCharacters;
		
		InternalCompletionOptionsBuilder(P parent) {
			super(parent);
		}

		@Override
		public CompletionOptionsBuilder<P> resolveProvider(Boolean resolveProvider) {
			this.resolveProvider = resolveProvider;
			return this;
		}

		@Override
		public CompletionOptionsBuilder<P> triggerCharacters(List<String> triggerCharacters) {
			this.triggerCharacters = triggerCharacters;
			return this;
		}

		@Override
		public CompletionOptions build() {
			CompletionOptions completionOptions = new CompletionOptions();
			completionOptions.setResolveProvider(resolveProvider);
			completionOptions.setTriggerCharacters(triggerCharacters);
			return completionOptions;
		}
	}
}
