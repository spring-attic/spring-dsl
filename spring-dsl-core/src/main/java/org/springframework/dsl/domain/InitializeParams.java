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

import org.springframework.dsl.domain.ClientCapabilities.ClientCapabilitiesBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code InitializeParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class InitializeParams {

	private Integer processId;
	private String rootPath;
	private String rootUri;
	private Object initializationOptions;
	private ClientCapabilities capabilities;
	private String trace;

	/**
	 * Instantiates a new initialize params.
	 */
	public InitializeParams() {
	}

	/**
	 * Instantiates a new initialize params.
	 *
	 * @param processId the process id
	 * @param rootUri the root uri
	 * @param initializationOptions the initialization options
	 * @param trace the trace
	 */
	public InitializeParams(Integer processId, String rootUri, Object initializationOptions, String trace) {
		this.processId = processId;
		this.rootUri = rootUri;
		this.initializationOptions = initializationOptions;
		this.trace = trace;
	}

	/**
	 * Instantiates a new initialize params.
	 *
	 * @param processId the process id
	 * @param rootUri the root uri
	 * @param initializationOptions the initialization options
	 * @param capabilities the capabilities
	 * @param trace the trace
	 */
	public InitializeParams(Integer processId, String rootUri, Object initializationOptions,
			ClientCapabilities capabilities, String trace) {
		this.processId = processId;
		this.rootUri = rootUri;
		this.initializationOptions = initializationOptions;
		this.capabilities = capabilities;
		this.trace = trace;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getRootUri() {
		return rootUri;
	}

	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}

	public Object getInitializationOptions() {
		return initializationOptions;
	}

	public void setInitializationOptions(Object initializationOptions) {
		this.initializationOptions = initializationOptions;
	}

	public ClientCapabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(ClientCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capabilities == null) ? 0 : capabilities.hashCode());
		result = prime * result + ((initializationOptions == null) ? 0 : initializationOptions.hashCode());
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
		result = prime * result + ((rootPath == null) ? 0 : rootPath.hashCode());
		result = prime * result + ((rootUri == null) ? 0 : rootUri.hashCode());
		result = prime * result + ((trace == null) ? 0 : trace.hashCode());
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
		InitializeParams other = (InitializeParams) obj;
		if (capabilities == null) {
			if (other.capabilities != null)
				return false;
		} else if (!capabilities.equals(other.capabilities))
			return false;
		if (initializationOptions == null) {
			if (other.initializationOptions != null)
				return false;
		} else if (!initializationOptions.equals(other.initializationOptions))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		if (rootPath == null) {
			if (other.rootPath != null)
				return false;
		} else if (!rootPath.equals(other.rootPath))
			return false;
		if (rootUri == null) {
			if (other.rootUri != null)
				return false;
		} else if (!rootUri.equals(other.rootUri))
			return false;
		if (trace == null) {
			if (other.trace != null)
				return false;
		} else if (!trace.equals(other.trace))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link InitializeParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface InitializeParamsBuilder<P> extends DomainBuilder<InitializeParams, P> {

		/**
		 * Sets a process id.
		 *
		 * @param processId the process id
		 * @return the builder for chaining
		 */
		InitializeParamsBuilder<P> processId(Integer processId);

		/**
		 * Sets a root path.
		 *
		 * @param rootPath the root uri
		 * @return the builder for chaining
		 */
		InitializeParamsBuilder<P> rootPath(String rootPath);

		/**
		 * Sets a root uri.
		 *
		 * @param rootUri the root uri
		 * @return the builder for chaining
		 */
		InitializeParamsBuilder<P> rootUri(String rootUri);

		/**
		 * Sets a initialization options
		 *
		 * @param initializationOptions the initialization options
		 * @return the builder for chaining
		 */
		InitializeParamsBuilder<P> initializationOptions(Object initializationOptions);

		/**
		 * Sets a trace.
		 *
		 * @param trace the trace
		 * @return the builder for chaining
		 */
		InitializeParamsBuilder<P> trace(String trace);

		/**
		 * Gets a builder for capabilities.
		 *
		 * @return the builder for chaining
		 */
		ClientCapabilitiesBuilder<InitializeParamsBuilder<P>> capabilities();
	}

	/**
	 * Gets a builder for {@link InitializeParams}.
	 *
	 * @return the initialize params builder
	 */
	public static <P> InitializeParamsBuilder<P> initializeParams() {
		return new InternalInitializeParamsBuilder<>(null);
	}

	protected static <P> InitializeParamsBuilder<P> initializeParams(P parent) {
		return new InternalInitializeParamsBuilder<>(parent);
	}

	private static class InternalInitializeParamsBuilder<P> extends AbstractDomainBuilder<InitializeParams, P>
			implements InitializeParamsBuilder<P> {

		private Integer processId;
		private String rootPath;
		private String rootUri;
		private Object initializationOptions;
		private ClientCapabilitiesBuilder<InitializeParamsBuilder<P>> capabilities;
		private String trace;

		InternalInitializeParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public InitializeParamsBuilder<P> processId(Integer processId) {
			this.processId = processId;
			return this;
		}

		@Override
		public InitializeParamsBuilder<P> rootPath(String rootPath) {
			this.rootPath = rootPath;
			return this;
		}

		@Override
		public InitializeParamsBuilder<P> rootUri(String rootUri) {
			this.rootUri = rootUri;
			return this;
		}

		@Override
		public InitializeParamsBuilder<P> initializationOptions(Object initializationOptions) {
			this.initializationOptions = initializationOptions;
			return this;
		}

		@Override
		public InitializeParamsBuilder<P> trace(String trace) {
			this.trace = trace;
			return this;
		}

		@Override
		public ClientCapabilitiesBuilder<InitializeParamsBuilder<P>> capabilities() {
			this.capabilities = ClientCapabilities.clientCapabilities(this);
			return capabilities;
		}

		@Override
		public InitializeParams build() {
			InitializeParams initializeParams = new InitializeParams(processId, rootUri, initializationOptions, trace);
			initializeParams.setRootPath(rootPath);
			if (capabilities != null) {
				initializeParams.setCapabilities(capabilities.build());
			}
			return initializeParams;
		}
	}
}
