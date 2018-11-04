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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dsl.domain.TextEdit.TextEditBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code WorkspaceEdit}.
 *
 * @author Janne Valkealahti
 *
 */
public class WorkspaceEdit {

	private Map<String, List<TextEdit>> changes;
	// TODO: documentChanges
	//       documentChanges?: (TextDocumentEdit[] | (TextDocumentEdit | CreateFile | RenameFile | DeleteFile)[]);

	public WorkspaceEdit() {
	}

	public Map<String, List<TextEdit>> getChanges() {
		return changes;
	}

	public void setChanges(Map<String, List<TextEdit>> changes) {
		this.changes = changes;
	}

	/**
	 * Builder interface for {@link WorkspaceEdit}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface WorkspaceEditBuilder<P> extends DomainBuilder<WorkspaceEdit, P> {

		/**
		 * Gets a builder for {@link TextEdit}.
		 *
		 * @param uri the uri
		 * @return the text edit builder
		 */
		TextEditBuilder<WorkspaceEditBuilder<P>> changes(String uri);

		/**
		 * Sets a list of {@link TextEdit}s for a uri. This effectively overrides use of
		 * {@link #changes(String)}.
		 *
		 * @param uri   the uri
		 * @param edits the text edits
		 * @return the builder for chaining
		 */
		WorkspaceEditBuilder<P> changes(String uri, List<TextEdit> edits);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changes == null) ? 0 : changes.hashCode());
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
		WorkspaceEdit other = (WorkspaceEdit) obj;
		if (changes == null) {
			if (other.changes != null) {
				return false;
			}
		} else if (!changes.equals(other.changes)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets a builder for {@link WorkspaceEdit}.
	 *
	 * @return the text edit builder
	 */
	public static <P> WorkspaceEditBuilder<P> workspaceEdit() {
		return new InternalWorkspaceEditBuilder<>(null);
	}

	protected static <P> WorkspaceEditBuilder<P> workspaceEdit(P parent) {
		return new InternalWorkspaceEditBuilder<>(parent);
	}

	private static class InternalWorkspaceEditBuilder<P>
			extends AbstractDomainBuilder<WorkspaceEdit, P> implements WorkspaceEditBuilder<P> {

		private Map<String, List<TextEdit>> changes = new HashMap<>();
		private Map<String, List<TextEditBuilder<WorkspaceEditBuilder<P>>>> changesBuilders = new HashMap<>();

		InternalWorkspaceEditBuilder(P parent) {
			super(parent);
		}

		@Override
		public TextEditBuilder<WorkspaceEditBuilder<P>> changes(String uri) {
			List<TextEditBuilder<WorkspaceEditBuilder<P>>> list = changesBuilders.getOrDefault(uri, new ArrayList<>());
			TextEditBuilder<WorkspaceEditBuilder<P>> textEditBuilder = TextEdit.textEdit(this);
			list.add(textEditBuilder);
			changesBuilders.put(uri, list);
			return textEditBuilder;
		}

		@Override
		public WorkspaceEditBuilder<P> changes(String uri, List<TextEdit> edits) {
			changes.put(uri, edits);
			return this;
		}

		@Override
		public WorkspaceEdit build() {
			WorkspaceEdit workspaceEdit = new WorkspaceEdit();
			if (changes.isEmpty() && changesBuilders.isEmpty()) {
				return workspaceEdit;
			}
			Map<String, List<TextEdit>> map = new HashMap<String, List<TextEdit>>();
			if (!changes.isEmpty()) {
				for (Entry<String, List<TextEdit>> e : changes.entrySet()) {
					map.put(e.getKey(), e.getValue());
				}
			} else if (!changesBuilders.isEmpty()) {
				for (Entry<String, List<TextEditBuilder<WorkspaceEditBuilder<P>>>> e : changesBuilders.entrySet()) {
					List<TextEdit> list = new ArrayList<>();
					for (TextEditBuilder<WorkspaceEditBuilder<P>> b : e.getValue()) {
						list.add(b.build());
					}
					map.put(e.getKey(), list);
				}
			}
			// ok, we have someting in a map, set it
			workspaceEdit.setChanges(map);
			return workspaceEdit;
		}
	}
}
