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

import org.springframework.dsl.domain.MarkupContent.MarkupContentBuilder;
import org.springframework.dsl.domain.Range.RangeBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code Hover}.
 *
 * @author Janne Valkealahti
 *
 */
public class Hover {

	private MarkupContent contents;
	private Range range;

	/**
	 * Instantiates a new hover.
	 */
	public Hover() {
	}

	/**
	 * Instantiates a new hover.
	 *
	 * @param contents the contents
	 * @param range the range
	 */
	public Hover(MarkupContent contents, Range range) {
		this.contents = contents;
		this.range = range;
	}

	public MarkupContent getContents() {
		return contents;
	}

	public void setContents(MarkupContent contents) {
		this.contents = contents;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
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
		Hover other = (Hover) obj;
		if (contents == null) {
			if (other.contents != null)
				return false;
		} else if (!contents.equals(other.contents))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link Hover}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface HoverBuilder<P> extends DomainBuilder<Hover, P> {

		/**
		 * Gets a builder for a {@link MarkupContent}.
		 *
		 * @return the builder for chaining
		 */
		MarkupContentBuilder<HoverBuilder<P>> contents();

		/**
		 * Gets a builder for a {@link Range}.
		 *
		 * @return the builder for chaining
		 */
		RangeBuilder<HoverBuilder<P>> range();

		/**
		 * Sets a range for a {@code range}. Will take presence of range set from
		 * via @{@link #range()}.
		 *
		 * @return the builder for chaining
		 */
		HoverBuilder<P> range(Range range);
	}

	/**
	 * Gets a builder for {@link Hover}
	 *
	 * @return the position builder
	 */
	public static <P> HoverBuilder<P> hover() {
		return new InternalHoverBuilder<>(null);
	}

	protected static <P> HoverBuilder<P> hover(P parent) {
		return new InternalHoverBuilder<>(parent);
	}

	private static class InternalHoverBuilder<P> extends AbstractDomainBuilder<Hover, P> implements HoverBuilder<P> {

		MarkupContentBuilder<HoverBuilder<P>> markupContent;
		RangeBuilder<HoverBuilder<P>> rangeBuilder;
		Range range;

		InternalHoverBuilder(P parent) {
			super(parent);
		}

		@Override
		public RangeBuilder<HoverBuilder<P>> range() {
			this.rangeBuilder = Range.range(this);
			return rangeBuilder;
		}

		@Override
		public HoverBuilder<P> range(Range range) {
			this.range = range;
			return this;
		}

		@Override
		public MarkupContentBuilder<HoverBuilder<P>> contents() {
			this.markupContent = MarkupContent.markupContent(this);
			return markupContent;
		}

		@Override
		public Hover build() {
			Hover hover = new Hover();
			if (markupContent != null) {
				hover.setContents(markupContent.build());
			}
			if (range != null) {
				hover.setRange(range);
			} else if (rangeBuilder != null) {
				hover.setRange(rangeBuilder.build());
			}
			return hover;
		}
	}
}
