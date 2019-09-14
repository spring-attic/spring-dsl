/*
 * Copyright 2019 the original author or authors.
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

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 *
 * @author Janne Valkealahti
 *
 */
public class WorkspaceSymbolParams {

	private String query;

	/**
	 * Instantiates a new workspace symbol params.
	 */
	public WorkspaceSymbolParams() {
	}

	/**
	 * Instantiates a new workspace symbol params.
	 *
	 * @param query the query
	 */
	public WorkspaceSymbolParams(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((query == null) ? 0 : query.hashCode());
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
			WorkspaceSymbolParams other = (WorkspaceSymbolParams) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkspaceSymbolParams [query=" + query + "]";
	}

	/**
	 * Builder interface for {@link WorkspaceSymbolParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface WorkspaceSymbolParamsBuilder<P> extends DomainBuilder<WorkspaceSymbolParams, P> {

		/**
		 * Sets a query.
		 *
		 * @param query the query
		 * @return the builder for chaining
		 */
		WorkspaceSymbolParamsBuilder<P> query(String query);
	}

	/**
	 * Gets a builder for {@link WorkspaceSymbolParams}
	 *
	 * @return the workspace symbol params builder
	 */
	public static <P> WorkspaceSymbolParamsBuilder<P> workspaceSymbolParams() {
		return new InternalWorkspaceSymbolParamsBuilder<>(null);
	}

	protected static <P> WorkspaceSymbolParamsBuilder<P> workspaceSymbolParams(P parent) {
		return new InternalWorkspaceSymbolParamsBuilder<>(parent);
	}

	private static class InternalWorkspaceSymbolParamsBuilder<P>
			extends AbstractDomainBuilder<WorkspaceSymbolParams, P> implements WorkspaceSymbolParamsBuilder<P> {

		private String query;

		InternalWorkspaceSymbolParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public WorkspaceSymbolParamsBuilder<P> query(String query) {
			this.query = query;
			return this;
		}

		@Override
		public WorkspaceSymbolParams build() {
			return new WorkspaceSymbolParams(query);
		}
	}
}
