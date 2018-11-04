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

import org.springframework.dsl.domain.ServerCapabilities.ServerCapabilitiesBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code InitializeResult}.
 *
 * @author Janne Valkealahti
 *
 */
public class InitializeResult {

	private ServerCapabilities capabilities;

	/**
	 * Instantiates a new initialize result.
	 */
	public InitializeResult() {
	}

	/**
	 * Instantiates a new initialize result.
	 *
	 * @param capabilities the capabilities
	 */
	public InitializeResult(ServerCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	public ServerCapabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(ServerCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capabilities == null) ? 0 : capabilities.hashCode());
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
		InitializeResult other = (InitializeResult) obj;
		if (capabilities == null) {
			if (other.capabilities != null) {
				return false;
			}
		} else if (!capabilities.equals(other.capabilities)) {
			return false;
		}
		return true;
	}

	public interface InitializeResultBuilder<P> extends DomainBuilder<InitializeResult, P> {

		ServerCapabilitiesBuilder<InitializeResultBuilder<P>> capabilities();
	}

	public static <P> InitializeResultBuilder<P> initializeResult() {
		return new InternalInitializeResultBuilder<>(null);
	}

	protected static <P> InitializeResultBuilder<P> initializeResult(P parent) {
		return new InternalInitializeResultBuilder<>(parent);
	}

	private static class InternalInitializeResultBuilder<P>
			extends AbstractDomainBuilder<InitializeResult, P> implements InitializeResultBuilder<P> {

		private ServerCapabilitiesBuilder<InitializeResultBuilder<P>> capabilities;

		InternalInitializeResultBuilder(P parent) {
			super(parent);
		}

		@Override
		public ServerCapabilitiesBuilder<InitializeResultBuilder<P>> capabilities() {
			this.capabilities = ServerCapabilities.serverCapabilities(this);
			return capabilities;
		}

		@Override
		public InitializeResult build() {
			InitializeResult initializeResult = new InitializeResult();
			if (capabilities != null) {
				initializeResult.setCapabilities(capabilities.build());
			}
			return initializeResult;
		}
	}
}
