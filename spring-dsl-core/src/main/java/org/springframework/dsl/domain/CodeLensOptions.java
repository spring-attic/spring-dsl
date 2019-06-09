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

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

public class CodeLensOptions {

	private Boolean resolveProvider;

    public Boolean getResolveProvider() {
        return resolveProvider;
    }

    public void setResolveProvider(Boolean resolveProvider) {
        this.resolveProvider = resolveProvider;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resolveProvider == null) ? 0 : resolveProvider.hashCode());
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
            CodeLensOptions other = (CodeLensOptions) obj;
		if (resolveProvider == null) {
			if (other.resolveProvider != null)
				return false;
		} else if (!resolveProvider.equals(other.resolveProvider))
			return false;
		return true;
	}

	/**
	 * Gets a builder for {@link CodeLensOptions}.
	 *
	 * @return the code lens options builder
	 */
	public static <P> CodeLensOptionsBuilder<P> codeLensOptions() {
		return new InternalCodeLensOptionsBuilder<>(null);
	}

	protected static <P> CodeLensOptionsBuilder<P> codeLensOptions(P parent) {
		return new InternalCodeLensOptionsBuilder<>(parent);
	}

	/**
	 * Builder interface for {@link CodeLensOptions}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CodeLensOptionsBuilder<P> extends DomainBuilder<CodeLensOptions, P> {

		/**
		 * Sets if resolveProvider is enabled.
		 *
		 * @param resolveProvider {@code true} if resolveProvider is supported
		 * @return the builder for chaining
		 */
		CodeLensOptionsBuilder<P> resolveProvider(Boolean resolveProvider);
	}

	private static class InternalCodeLensOptionsBuilder<P> extends AbstractDomainBuilder<CodeLensOptions, P>
			implements CodeLensOptionsBuilder<P> {

		private Boolean resolveProvider;

		InternalCodeLensOptionsBuilder(P parent) {
			super(parent);
		}

		@Override
		public CodeLensOptionsBuilder<P> resolveProvider(Boolean resolveProvider) {
			this.resolveProvider = resolveProvider;
			return this;
		}

		@Override
		public CodeLensOptions build() {
            CodeLensOptions codeLensOptions = new CodeLensOptions();
            codeLensOptions.setResolveProvider(resolveProvider);
			return codeLensOptions;
		}
	}
}
