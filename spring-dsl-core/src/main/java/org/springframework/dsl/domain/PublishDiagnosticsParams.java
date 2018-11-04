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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.domain.Diagnostic.DiagnosticBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code PublishDiagnosticsParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class PublishDiagnosticsParams {

	private String uri;
	private List<Diagnostic> diagnostics;

	/**
	 * Instantiates a new publish diagnostics params.
	 */
	public PublishDiagnosticsParams() {
		this.diagnostics = new ArrayList<>();
	}

	/**
	 * Instantiates a new publish diagnostics params.
	 *
	 * @param uri the uri
	 */
	public PublishDiagnosticsParams(String uri) {
		this.uri = uri;
		this.diagnostics = new ArrayList<>();
	}

	/**
	 * Instantiates a new publish diagnostics params.
	 *
	 * @param uri the uri
	 * @param diagnostics the diagnostics
	 */
	public PublishDiagnosticsParams(String uri, List<Diagnostic> diagnostics) {
		this.uri = uri;
		this.diagnostics = diagnostics;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<Diagnostic> getDiagnostics() {
		return diagnostics;
	}

	public void setDiagnostics(List<Diagnostic> diagnostics) {
		this.diagnostics = diagnostics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagnostics == null) ? 0 : diagnostics.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
		PublishDiagnosticsParams other = (PublishDiagnosticsParams) obj;
		if (diagnostics == null) {
			if (other.diagnostics != null)
				return false;
		} else if (!diagnostics.equals(other.diagnostics))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link PublishDiagnosticsParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface PublishDiagnosticsParamsBuilder<P> extends DomainBuilder<PublishDiagnosticsParams, P> {

		/**
		 * Sets an uri.
		 *
		 * @param uri the uri
		 * @return the builder for chaining
		 */
		PublishDiagnosticsParamsBuilder<P> uri(String uri);

		/**
		 * Gets a builder for diagnostics.
		 *
		 * @return the builder for chaining
		 */
		DiagnosticBuilder<PublishDiagnosticsParamsBuilder<P>> diagnostic();
	}

	/**
	 * Gets a builder for {@link PublishDiagnosticsParams}.
	 *
	 * @return the initialize params builder
	 */
	public static <P> PublishDiagnosticsParamsBuilder<P> publishDiagnosticsParams() {
		return new InternalPublishDiagnosticsParamsBuilder<>(null);
	}

	protected static <P> PublishDiagnosticsParamsBuilder<P> publishDiagnosticsParams(P parent) {
		return new InternalPublishDiagnosticsParamsBuilder<>(parent);
	}

	private static class InternalPublishDiagnosticsParamsBuilder<P>
			extends AbstractDomainBuilder<PublishDiagnosticsParams, P> implements PublishDiagnosticsParamsBuilder<P> {

		private String uri;
		private List<DiagnosticBuilder<PublishDiagnosticsParamsBuilder<P>>> diagnostics = new ArrayList<>();

		InternalPublishDiagnosticsParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public DiagnosticBuilder<PublishDiagnosticsParamsBuilder<P>> diagnostic() {
			DiagnosticBuilder<PublishDiagnosticsParamsBuilder<P>> diagnosticBuilder = Diagnostic.diagnostic(this);
			this.diagnostics.add(diagnosticBuilder);
			return diagnosticBuilder;
		}

		@Override
		public PublishDiagnosticsParamsBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public PublishDiagnosticsParams build() {
			PublishDiagnosticsParams publishDiagnosticsParams = new PublishDiagnosticsParams();
			if (!diagnostics.isEmpty()) {
				List<Diagnostic> diagnostic = new ArrayList<>();
				for (DiagnosticBuilder<PublishDiagnosticsParamsBuilder<P>> d : diagnostics) {
					diagnostic.add(d.build());
				}
				publishDiagnosticsParams.setDiagnostics(diagnostic);
			}
			publishDiagnosticsParams.setUri(uri);
			return publishDiagnosticsParams;
		}
	}
}
