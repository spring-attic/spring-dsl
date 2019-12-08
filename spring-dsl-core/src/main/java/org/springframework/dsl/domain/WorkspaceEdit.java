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
import java.util.stream.Collectors;

import org.springframework.dsl.domain.CreateFile.CreateFileBuilder;
import org.springframework.dsl.domain.DeleteFile.DeleteFileBuilder;
import org.springframework.dsl.domain.RenameFile.RenameFileBuilder;
import org.springframework.dsl.domain.TextDocumentEdit.TextDocumentEditBuilder;
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
	private List<TextDocumentEdit> documentChangesTextDocumentEdits;
	private List<CreateFile> documentChangesCreateFiles;
	private List<RenameFile> documentChangesRenameFiles;
	private List<DeleteFile> documentChangesDeleteFiles;

	public WorkspaceEdit() {
	}

	public Map<String, List<TextEdit>> getChanges() {
		return changes;
	}

	public void setChanges(Map<String, List<TextEdit>> changes) {
		this.changes = changes;
	}

	public List<TextDocumentEdit> getDocumentChangesTextDocumentEdits() {
		return documentChangesTextDocumentEdits;
	}

	public void setDocumentChangesTextDocumentEdits(List<TextDocumentEdit> documentChangesTextDocumentEdits) {
		this.documentChangesTextDocumentEdits = documentChangesTextDocumentEdits;
	}

	public List<CreateFile> getDocumentChangesCreateFiles() {
		return documentChangesCreateFiles;
	}

	public void setDocumentChangesCreateFiles(List<CreateFile> documentChangesCreateFiles) {
		this.documentChangesCreateFiles = documentChangesCreateFiles;
	}

	public List<RenameFile> getDocumentChangesRenameFiles() {
		return documentChangesRenameFiles;
	}

	public void setDocumentChangesRenameFiles(List<RenameFile> documentChangesRenameFiles) {
		this.documentChangesRenameFiles = documentChangesRenameFiles;
	}

	public List<DeleteFile> getDocumentChangesDeleteFiles() {
		return documentChangesDeleteFiles;
	}

	public void setDocumentChangesDeleteFiles(List<DeleteFile> documentChangesDeleteFiles) {
		this.documentChangesDeleteFiles = documentChangesDeleteFiles;
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

		/**
		 * Gets a builder for {@code documentChanges} for having {@link TextDocumentEdit} types.
		 *
		 * @return the text document edit builder
		 */
		TextDocumentEditBuilder<WorkspaceEditBuilder<P>> documentChangesTextDocumentEdits();

		/**
		 * Gets a builder for {@code documentChanges} for having {@link CreateFile} types.
		 *
		 * @return the create file builder
		 */
		CreateFileBuilder<WorkspaceEditBuilder<P>> documentChangesCreateFiles();

		/**
		 * Gets a builder for {@code documentChanges} for having {@link RenameFile} types.
		 *
		 * @return the rename file builder
		 */
		RenameFileBuilder<WorkspaceEditBuilder<P>> documentChangesRenameFiles();

		/**
		 * Gets a builder for {@code documentChanges} for having {@link DeleteFile} types.
		 *
		 * @return the delete file builder
		 */
		DeleteFileBuilder<WorkspaceEditBuilder<P>> documentChangesDeleteFiles();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changes == null) ? 0 : changes.hashCode());
		result = prime * result + ((documentChangesCreateFiles == null) ? 0 : documentChangesCreateFiles.hashCode());
		result = prime * result + ((documentChangesDeleteFiles == null) ? 0 : documentChangesDeleteFiles.hashCode());
		result = prime * result + ((documentChangesRenameFiles == null) ? 0 : documentChangesRenameFiles.hashCode());
		result = prime * result
				+ ((documentChangesTextDocumentEdits == null) ? 0 : documentChangesTextDocumentEdits.hashCode());
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
		WorkspaceEdit other = (WorkspaceEdit) obj;
		if (changes == null) {
			if (other.changes != null)
				return false;
		} else if (!changes.equals(other.changes))
			return false;
		if (documentChangesCreateFiles == null) {
			if (other.documentChangesCreateFiles != null)
				return false;
		} else if (!documentChangesCreateFiles.equals(other.documentChangesCreateFiles))
			return false;
		if (documentChangesDeleteFiles == null) {
			if (other.documentChangesDeleteFiles != null)
				return false;
		} else if (!documentChangesDeleteFiles.equals(other.documentChangesDeleteFiles))
			return false;
		if (documentChangesRenameFiles == null) {
			if (other.documentChangesRenameFiles != null)
				return false;
		} else if (!documentChangesRenameFiles.equals(other.documentChangesRenameFiles))
			return false;
		if (documentChangesTextDocumentEdits == null) {
			if (other.documentChangesTextDocumentEdits != null)
				return false;
		} else if (!documentChangesTextDocumentEdits.equals(other.documentChangesTextDocumentEdits))
			return false;
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
		private List<TextDocumentEditBuilder<WorkspaceEditBuilder<P>>> documentChangesTextDocumentEditsBuilders = new ArrayList<>();
		private List<CreateFileBuilder<WorkspaceEditBuilder<P>>> documentChangesCreateFilesBuilders = new ArrayList<>();
		private List<RenameFileBuilder<WorkspaceEditBuilder<P>>> documentChangesRenameFilesBuilders = new ArrayList<>();
		private List<DeleteFileBuilder<WorkspaceEditBuilder<P>>> documentChangesDeleteFilesBuilders = new ArrayList<>();

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
		public TextDocumentEditBuilder<WorkspaceEditBuilder<P>> documentChangesTextDocumentEdits() {
			TextDocumentEditBuilder<WorkspaceEditBuilder<P>> builder = TextDocumentEdit.textDocumentEdit(this);
			documentChangesTextDocumentEditsBuilders.add(builder);
			return builder;
		}

		@Override
		public CreateFileBuilder<WorkspaceEditBuilder<P>> documentChangesCreateFiles() {
			CreateFileBuilder<WorkspaceEditBuilder<P>> builder = CreateFile.createFile(this);
			documentChangesCreateFilesBuilders.add(builder);
			return builder;
		}

		@Override
		public RenameFileBuilder<WorkspaceEditBuilder<P>> documentChangesRenameFiles() {
			RenameFileBuilder<WorkspaceEditBuilder<P>> builder = RenameFile.renameFile(this);
			documentChangesRenameFilesBuilders.add(builder);
			return builder;
		}

		@Override
		public DeleteFileBuilder<WorkspaceEditBuilder<P>> documentChangesDeleteFiles() {
			DeleteFileBuilder<WorkspaceEditBuilder<P>> builder = DeleteFile.deleteFile(this);
			documentChangesDeleteFilesBuilders.add(builder);
			return builder;
		}

		@Override
		public WorkspaceEdit build() {
			WorkspaceEdit workspaceEdit = new WorkspaceEdit();
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

			if (!documentChangesTextDocumentEditsBuilders.isEmpty()) {
				List<TextDocumentEdit> edits = documentChangesTextDocumentEditsBuilders.stream()
					.map(builder -> builder.build()).collect(Collectors.toList());
				workspaceEdit.setDocumentChangesTextDocumentEdits(edits);
			} else if (!documentChangesCreateFilesBuilders.isEmpty()) {
				List<CreateFile> files = documentChangesCreateFilesBuilders.stream()
					.map(builder -> builder.build()).collect(Collectors.toList());
				workspaceEdit.setDocumentChangesCreateFiles(files);
			} else if (!documentChangesRenameFilesBuilders.isEmpty()) {
				List<RenameFile> files = documentChangesRenameFilesBuilders.stream()
					.map(builder -> builder.build()).collect(Collectors.toList());
				workspaceEdit.setDocumentChangesRenameFiles(files);
			} else if (!documentChangesDeleteFilesBuilders.isEmpty()) {
				List<DeleteFile> files = documentChangesDeleteFilesBuilders.stream()
					.map(builder -> builder.build()).collect(Collectors.toList());
				workspaceEdit.setDocumentChangesDeleteFiles(files);
			}

			// ok, we have someting in a map, set it
			if (!map.isEmpty()) {
				workspaceEdit.setChanges(map);
			}
			return workspaceEdit;
		}
	}
}
