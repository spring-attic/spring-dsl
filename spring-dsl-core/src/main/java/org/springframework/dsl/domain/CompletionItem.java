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

import org.springframework.dsl.domain.Command.CommandBuilder;
import org.springframework.dsl.domain.MarkupContent.MarkupContentBuilder;
import org.springframework.dsl.domain.TextEdit.TextEditBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code CompletionItem}.
 *
 * @author Janne Valkealahti
 *
 */
public class CompletionItem {

	private String label;
	private CompletionItemKind kind;
	private String detail;
	private MarkupContent documentation;
	private String sortText;
	private String filterText;
	private String insertText;
	private InsertTextFormat insertTextFormat;
	private TextEdit textEdit;
	private List<TextEdit> additionalTextEdits;
	private List<String> commitCharacters;
	private Command command;
	private Object data;

	public CompletionItem() {
	}

	public CompletionItem(String label, TextEdit textEdit) {
		this.label = label;
		this.textEdit = textEdit;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public CompletionItemKind getKind() {
		return kind;
	}

	public void setKind(CompletionItemKind kind) {
		this.kind = kind;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public MarkupContent getDocumentation() {
		return documentation;
	}

	public void setDocumentation(MarkupContent documentation) {
		this.documentation = documentation;
	}

	public String getSortText() {
		return sortText;
	}

	public void setSortText(String sortText) {
		this.sortText = sortText;
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	public String getInsertText() {
		return insertText;
	}

	public void setInsertText(String insertText) {
		this.insertText = insertText;
	}

	public InsertTextFormat getInsertTextFormat() {
		return insertTextFormat;
	}

	public void setInsertTextFormat(InsertTextFormat insertTextFormat) {
		this.insertTextFormat = insertTextFormat;
	}

	public TextEdit getTextEdit() {
		return textEdit;
	}

	public void setTextEdit(TextEdit textEdit) {
		this.textEdit = textEdit;
	}

	public List<TextEdit> getAdditionalTextEdits() {
		return additionalTextEdits;
	}

	public void setAdditionalTextEdits(List<TextEdit> additionalTextEdits) {
		this.additionalTextEdits = additionalTextEdits;
	}

	public List<String> getCommitCharacters() {
		return commitCharacters;
	}

	public void setCommitCharacters(List<String> commitCharacters) {
		this.commitCharacters = commitCharacters;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalTextEdits == null) ? 0 : additionalTextEdits.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((commitCharacters == null) ? 0 : commitCharacters.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + ((documentation == null) ? 0 : documentation.hashCode());
		result = prime * result + ((filterText == null) ? 0 : filterText.hashCode());
		result = prime * result + ((insertText == null) ? 0 : insertText.hashCode());
		result = prime * result + ((insertTextFormat == null) ? 0 : insertTextFormat.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((sortText == null) ? 0 : sortText.hashCode());
		result = prime * result + ((textEdit == null) ? 0 : textEdit.hashCode());
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
		CompletionItem other = (CompletionItem) obj;
		if (additionalTextEdits == null) {
			if (other.additionalTextEdits != null)
				return false;
		} else if (!additionalTextEdits.equals(other.additionalTextEdits))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (commitCharacters == null) {
			if (other.commitCharacters != null)
				return false;
		} else if (!commitCharacters.equals(other.commitCharacters))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (documentation == null) {
			if (other.documentation != null)
				return false;
		} else if (!documentation.equals(other.documentation))
			return false;
		if (filterText == null) {
			if (other.filterText != null)
				return false;
		} else if (!filterText.equals(other.filterText))
			return false;
		if (insertText == null) {
			if (other.insertText != null)
				return false;
		} else if (!insertText.equals(other.insertText))
			return false;
		if (insertTextFormat != other.insertTextFormat)
			return false;
		if (kind != other.kind)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (sortText == null) {
			if (other.sortText != null)
				return false;
		} else if (!sortText.equals(other.sortText))
			return false;
		if (textEdit == null) {
			if (other.textEdit != null)
				return false;
		} else if (!textEdit.equals(other.textEdit))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link CompletionItem}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CompletionItemBuilder<P> extends DomainBuilder<CompletionItem, P>{

		/**
		 * Sets a label.
		 *
		 * @param label the label
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> label(String label);

		/**
		 * Sets a completion item kind
		 *
		 * @param kind the kind
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> kind(CompletionItemKind kind);

		/**
		 * Sets a detail.
		 *
		 * @param detail the detail
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> detail(String detail);

		/**
		 * Gets a builder for {@link MarkupContent}.
		 *
		 * @return the builder for chaining
		 */
		MarkupContentBuilder<CompletionItemBuilder<P>> documentation();

		/**
		 * Sets a sort text.
		 *
		 * @param sortText the sortText
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> sortText(String sortText);

		/**
		 * Sets a filter text.
		 *
		 * @param filterText the filterText
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> filterText(String filterText);

		/**
		 * Sets an insert text.
		 *
		 * @param insertText the insertText
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> insertText(String insertText);

		/**
		 * Sets an insert text format.
		 *
		 * @param insertTextFormat the insertTextFormat
		 * @return the builder for chaining
		 */
		CompletionItemBuilder<P> insertTextFormat(InsertTextFormat insertTextFormat);

		/**
		 * Gets a builder for {@link TextEdit}.
		 *
		 * @return the builder for chaining
		 */
		TextEditBuilder<CompletionItemBuilder<P>> textEdit();

		/**
		 * Gets a builder for {@link Command}.
		 *
		 * @return the builder for chaining
		 */
		CommandBuilder<CompletionItemBuilder<P>> command();
	}

	/**
	 * Gets a builder for {@link CompletionItem}
	 *
	 * @return the completion list builder
	 */
	public static <P> CompletionItemBuilder<P> completionItem() {
		return new InternalCompletionItemBuilder<>(null);
	}

	protected static <P> CompletionItemBuilder<P> completionItem(P parent) {
		return new InternalCompletionItemBuilder<>(parent);
	}

	private static class InternalCompletionItemBuilder<P>
			extends AbstractDomainBuilder<CompletionItem, P> implements CompletionItemBuilder<P> {

		private String label;
		private CompletionItemKind kind;
		private String detail;
		private MarkupContentBuilder<CompletionItemBuilder<P>> documentation;
		private String sortText;
		private String filterText;
		private String insertText;
		private InsertTextFormat insertTextFormat;
		private TextEditBuilder<CompletionItemBuilder<P>> textEdit;
		// TODO: support vvv
//		private List<TextEdit> additionalTextEdits;
//		private List<String> commitCharacters;
//		private Object data;
		private CommandBuilder<CompletionItemBuilder<P>> command;

		InternalCompletionItemBuilder(P parent) {
			super(parent);
		}

		@Override
		public CompletionItemBuilder<P> label(String label) {
			this.label = label;
			return this;
		}

		@Override
		public CompletionItemBuilder<P> kind(CompletionItemKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public CompletionItemBuilder<P> detail(String detail) {
			this.detail = detail;
			return this;
		}

		@Override
		public MarkupContentBuilder<CompletionItemBuilder<P>> documentation() {
			this.documentation = MarkupContent.markupContent(this);
			return documentation;
		}

		@Override
		public CompletionItemBuilder<P> sortText(String sortText) {
			this.sortText = sortText;
			return this;
		}

		@Override
		public CompletionItemBuilder<P> filterText(String filterText) {
			this.filterText = filterText;
			return this;
		}

		@Override
		public CompletionItemBuilder<P> insertText(String insertText) {
			this.insertText = insertText;
			return this;
		}

		@Override
		public CompletionItemBuilder<P> insertTextFormat(InsertTextFormat insertTextFormat) {
			this.insertTextFormat = insertTextFormat;
			return this;
		}

		@Override
		public TextEditBuilder<CompletionItemBuilder<P>> textEdit() {
			this.textEdit = TextEdit.textEdit(this);
			return textEdit;
		}

		@Override
		public CommandBuilder<CompletionItemBuilder<P>> command() {
			this.command = Command.command(this);
			return this.command;
		}

		@Override
		public CompletionItem build() {
			CompletionItem completionItem = new CompletionItem();
			completionItem.setLabel(label);
			completionItem.setKind(kind);
			completionItem.setDetail(detail);
			if (documentation != null) {
				completionItem.setDocumentation(documentation.build());
			}
			if (textEdit != null) {
				completionItem.setTextEdit(textEdit.build());
			}
			completionItem.setSortText(sortText);
			completionItem.setFilterText(filterText);
			completionItem.setInsertText(insertText);
			completionItem.setInsertTextFormat(insertTextFormat);
			if (command != null) {
				completionItem.setCommand(command.build());
			}
			return completionItem;
		}
	}
}
