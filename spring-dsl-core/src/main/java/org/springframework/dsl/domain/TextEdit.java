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

import org.springframework.dsl.domain.Range.RangeBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code TextEdit}.
 *
 * @author Janne Valkealahti
 *
 */
public class TextEdit {

	private Range range;
	private String newText;

	/**
	 * Instantiates a new text edit.
	 */
	public TextEdit() {
	}

	/**
	 * Instantiates a new text edit.
	 *
	 * @param range the range
	 * @param newText the new text
	 */
	public TextEdit(Range range, String newText) {
		this.range = range;
		this.newText = newText;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public String getNewText() {
		return newText;
	}

	public void setNewText(String newText) {
		this.newText = newText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newText == null) ? 0 : newText.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
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
		TextEdit other = (TextEdit) obj;
		if (newText == null) {
			if (other.newText != null)
				return false;
		} else if (!newText.equals(other.newText))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link TextEdit}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface TextEditBuilder<P> extends DomainBuilder<TextEdit, P> {

		/**
		 * Gets a builder for {@link Range}.
		 *
		 * @return the range builder
		 */
		RangeBuilder<TextEditBuilder<P>> range();

		/**
		 * Sets a range for a {@code range}. Will take presence of range set from
		 * via @{@link #range()}.
		 *
		 * @return the builder for chaining
		 */
		TextEditBuilder<P> range(Range range);

		/**
		 * Sets a new text.
		 *
		 * @param newText the newText
		 * @return the builder for chaining
		 */
		TextEditBuilder<P> newText(String newText);
	}

	/**
	 * Gets a builder for {@link TextEdit}.
	 *
	 * @return the text edit builder
	 */
	public static <P> TextEditBuilder<P> textEdit() {
		return new InternalTextEditBuilder<>(null);
	}

	protected static <P> TextEditBuilder<P> textEdit(P parent) {
		return new InternalTextEditBuilder<>(parent);
	}

	private static class InternalTextEditBuilder<P>
			extends AbstractDomainBuilder<TextEdit, P> implements TextEditBuilder<P> {

		String newText;
		RangeBuilder<TextEditBuilder<P>> rangeBuilder;
		Range range;

		InternalTextEditBuilder(P parent) {
			super(parent);
		}

		@Override
		public RangeBuilder<TextEditBuilder<P>> range() {
			this.rangeBuilder = Range.range(this);
			return rangeBuilder;
		}

		@Override
		public TextEditBuilder<P> range(Range range) {
			this.range = range;
			return this;
		}

		@Override
		public TextEditBuilder<P> newText(String newText) {
			this.newText = newText;
			return this;
		}

		@Override
		public TextEdit build() {
			TextEdit textEdit = new TextEdit();
			textEdit.setNewText(newText);
			if (range != null) {
				textEdit.setRange(range);
			} else if (rangeBuilder != null) {
				textEdit.setRange(rangeBuilder.build());
			}
			return textEdit;
		}
	}
}
