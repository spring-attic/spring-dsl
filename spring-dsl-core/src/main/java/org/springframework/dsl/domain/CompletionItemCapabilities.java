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

public class CompletionItemCapabilities {

	private Boolean snippetSupport;
	private Boolean commitCharactersSupport;
	private List<MarkupKind> documentationFormat;
	private Boolean deprecatedSupport;
	private Boolean preselectSupport;

	public CompletionItemCapabilities() {

	}

	public CompletionItemCapabilities(Boolean snippetSupport, Boolean commitCharactersSupport,
			List<MarkupKind> documentationFormat, Boolean deprecatedSupport, Boolean preselectSupport) {
		this.snippetSupport = snippetSupport;
		this.commitCharactersSupport = commitCharactersSupport;
		this.documentationFormat = documentationFormat;
		this.deprecatedSupport = deprecatedSupport;
		this.preselectSupport = preselectSupport;
	}

	public Boolean getSnippetSupport() {
		return snippetSupport;
	}

	public void setSnippetSupport(Boolean snippetSupport) {
		this.snippetSupport = snippetSupport;
	}

	public Boolean getCommitCharactersSupport() {
		return commitCharactersSupport;
	}

	public void setCommitCharactersSupport(Boolean commitCharactersSupport) {
		this.commitCharactersSupport = commitCharactersSupport;
	}

	public List<MarkupKind> getDocumentationFormat() {
		return documentationFormat;
	}

	public void setDocumentationFormat(List<MarkupKind> documentationFormat) {
		this.documentationFormat = documentationFormat;
	}

	public Boolean getDeprecatedSupport() {
		return deprecatedSupport;
	}

	public void setDeprecatedSupport(Boolean deprecatedSupport) {
		this.deprecatedSupport = deprecatedSupport;
	}

	public Boolean getPreselectSupport() {
		return preselectSupport;
	}

	public void setPreselectSupport(Boolean preselectSupport) {
		this.preselectSupport = preselectSupport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitCharactersSupport == null) ? 0 : commitCharactersSupport.hashCode());
		result = prime * result + ((deprecatedSupport == null) ? 0 : deprecatedSupport.hashCode());
		result = prime * result + ((documentationFormat == null) ? 0 : documentationFormat.hashCode());
		result = prime * result + ((preselectSupport == null) ? 0 : preselectSupport.hashCode());
		result = prime * result + ((snippetSupport == null) ? 0 : snippetSupport.hashCode());
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
		CompletionItemCapabilities other = (CompletionItemCapabilities) obj;
		if (commitCharactersSupport == null) {
			if (other.commitCharactersSupport != null)
				return false;
		} else if (!commitCharactersSupport.equals(other.commitCharactersSupport))
			return false;
		if (deprecatedSupport == null) {
			if (other.deprecatedSupport != null)
				return false;
		} else if (!deprecatedSupport.equals(other.deprecatedSupport))
			return false;
		if (documentationFormat == null) {
			if (other.documentationFormat != null)
				return false;
		} else if (!documentationFormat.equals(other.documentationFormat))
			return false;
		if (preselectSupport == null) {
			if (other.preselectSupport != null)
				return false;
		} else if (!preselectSupport.equals(other.preselectSupport))
			return false;
		if (snippetSupport == null) {
			if (other.snippetSupport != null)
				return false;
		} else if (!snippetSupport.equals(other.snippetSupport))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompletionItemCapabilities [commitCharactersSupport=" + commitCharactersSupport + ", deprecatedSupport="
				+ deprecatedSupport + ", documentationFormat=" + documentationFormat + ", preselectSupport="
				+ preselectSupport + ", snippetSupport=" + snippetSupport + "]";
	}

	public interface CompletionItemCapabilitiesBuilder<P> extends DomainBuilder<CompletionItemCapabilities, P> {

		/**
		 * Sets a snippetSupport.
		 *
		 * @param snippetSupport the snippetSupport
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> snippetSupport(Boolean snippetSupport);

		/**
		 * Sets a commitCharactersSupport.
		 *
		 * @param commitCharactersSupport the commitCharactersSupport
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> commitCharactersSupport(Boolean commitCharactersSupport);


		/**
		 * Adds a new {@link MarkupKind} to a {@code documentationFormat}.
		 *
		 * @param kind the completion item kind
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> documentationFormat(MarkupKind kind);

		/**
		 * Sets a {@link List} of {@MarkupKind}'s' for {@code documentationFormat}.
		 *
		 * @param kinds the list of completion item kinds
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> documentationFormat(List<MarkupKind> kinds);

		/**
		 * Sets a deprecatedSupport.
		 *
		 * @param snippetSupport the deprecatedSupport
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> deprecatedSupport(Boolean deprecatedSupport);

		/**
		 * Sets a preselectSupport.
		 *
		 * @param snippetSupport the preselectSupport
		 * @return the builder for chaining
		 */
		CompletionItemCapabilitiesBuilder<P> preselectSupport(Boolean preselectSupport);

	}

	/**
	 * Gets a builder for {@link CompletionItemCapabilities}
	 *
	 * @return the completion item capabilities builder
	 */
	public static <P> CompletionItemCapabilitiesBuilder<P> completionItemKindCapabilities() {
		return new InternalCompletionItemCapabilitiesBuilder<>(null);
	}

	protected static <P> CompletionItemCapabilitiesBuilder<P> completionItemKindCapabilities(P parent) {
		return new InternalCompletionItemCapabilitiesBuilder<>(parent);
	}

	private static class InternalCompletionItemCapabilitiesBuilder<P>
			extends AbstractDomainBuilder<CompletionItemCapabilities, P> implements CompletionItemCapabilitiesBuilder<P> {

		private Boolean snippetSupport;
		private Boolean commitCharactersSupport;
		private List<MarkupKind> documentationFormat = new ArrayList<>();
		private Boolean deprecatedSupport;
		private Boolean preselectSupport;

		InternalCompletionItemCapabilitiesBuilder(P parent) {
			super(parent);
		}

		@Override
		public CompletionItemCapabilitiesBuilder<P> snippetSupport(Boolean snippetSupport) {
			this.snippetSupport = snippetSupport;
			return this;
		}

		@Override
		public CompletionItemCapabilitiesBuilder<P> commitCharactersSupport(Boolean commitCharactersSupport) {
			this.commitCharactersSupport = commitCharactersSupport;
			return this;
		}


		@Override
		public CompletionItemCapabilitiesBuilder<P> documentationFormat(MarkupKind kind) {
			documentationFormat.add(kind);
			return this;
		}

		@Override
		public CompletionItemCapabilitiesBuilder<P> documentationFormat(List<MarkupKind> kinds) {
			this.documentationFormat.clear();
			this.documentationFormat.addAll(kinds);
			return this;
		}

		@Override
		public CompletionItemCapabilitiesBuilder<P> deprecatedSupport(Boolean deprecatedSupport) {
			this.deprecatedSupport = deprecatedSupport;
			return this;
		}

		@Override
		public CompletionItemCapabilitiesBuilder<P> preselectSupport(Boolean preselectSupport) {
			this.preselectSupport = preselectSupport;
			return this;
		}

		@Override
		public CompletionItemCapabilities build() {
			return new CompletionItemCapabilities(snippetSupport, commitCharactersSupport, documentationFormat,
					deprecatedSupport, preselectSupport);
		}
	}
}
