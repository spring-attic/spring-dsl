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

import org.springframework.dsl.domain.Range.RangeBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code DocumentSymbol}.
 *
 * @author Janne Valkealahti
 *
 */
public class DocumentSymbol {

	private String name;
	private String detail;
	private SymbolKind kind;
	private Boolean deprecated;
	private Range range;
	private Range selectionRange;
	private List<DocumentSymbol> children;

	/**
	 * Instantiates a new document symbol.
	 */
	public DocumentSymbol() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public SymbolKind getKind() {
		return kind;
	}

	public void setKind(SymbolKind kind) {
		this.kind = kind;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public Range getSelectionRange() {
		return selectionRange;
	}

	public void setSelectionRange(Range selectionRange) {
		this.selectionRange = selectionRange;
	}

	public List<DocumentSymbol> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentSymbol> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((selectionRange == null) ? 0 : selectionRange.hashCode());
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
		DocumentSymbol other = (DocumentSymbol) obj;
		if (children == null) {
			if (other.children != null) {
				return false;
			}
		} else if (!children.equals(other.children)) {
			return false;
		}
		if (deprecated == null) {
			if (other.deprecated != null) {
				return false;
			}
		} else if (!deprecated.equals(other.deprecated)) {
			return false;
		}
		if (detail == null) {
			if (other.detail != null) {
				return false;
			}
		} else if (!detail.equals(other.detail)) {
			return false;
		}
		if (kind != other.kind) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (range == null) {
			if (other.range != null) {
				return false;
			}
		} else if (!range.equals(other.range)) {
			return false;
		}
		if (selectionRange == null) {
			if (other.selectionRange != null) {
				return false;
			}
		} else if (!selectionRange.equals(other.selectionRange)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets a builder for {@link DocumentSymbol}
	 *
	 * @return the document symbol builder
	 */
	public static <P> DocumentSymbolBuilder<P> documentSymbol() {
		return new InternalDocumentSymbolBuilder<>(null);
	}

	protected static <P> DocumentSymbolBuilder<P> documentSymbol(P parent) {
		return new InternalDocumentSymbolBuilder<>(parent);
	}

	/**
	 * Builder interface for {@link DocumentSymbol}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface DocumentSymbolBuilder<P> extends DomainBuilder<DocumentSymbol, P> {

		/**
		 * Sets a name.
		 *
		 * @param name the name
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> name(String name);

		/**
		 * Sets a detail.
		 *
		 * @param detail the detail
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> detail(String detail);

		/**
		 * Sets a kind.
		 *
		 * @param kind the kind
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> kind(SymbolKind kind);

		/**
		 * Sets a deprecated.
		 *
		 * @param deprecated the deprecated
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> deprecated(Boolean deprecated);

		/**
		 * Gets a builder for a {@link Range} for {@code range}. Use
		 * of @{@link #range(Range)} will take presence of this.
		 *
		 * @return the builder for chaining
		 */
		RangeBuilder<DocumentSymbolBuilder<P>> range();

		/**
		 * Sets a range for a {@code range}. Will take presence of range set from
		 * via @{@link #range()}.
		 *
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> range(Range range);

		/**
		 * Gets a builder for a {@link Range} for {@code selectionRange}.
		 *
		 * @return the builder for chaining
		 */
		RangeBuilder<DocumentSymbolBuilder<P>> selectionRange();

		/**
		 * Sets a range for a {@code selectionRange}. Will take presence of range set from
		 * via @{@link #selectionRange()}.
		 *
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> selectionRange(Range range);

		/**
		 * Gets a builder for a {@link DocumentSymbol} for adding a child.
		 *
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<DocumentSymbolBuilder<P>> child();

		/**
		 * Adds one {@link DocumentSymbol} to children.
		 *
		 * @param child the child
		 * @return the builder for chaining
		 */
		DocumentSymbolBuilder<P> child(DocumentSymbol child);
	}

	private static class InternalDocumentSymbolBuilder<P>
		extends AbstractDomainBuilder<DocumentSymbol, P> implements DocumentSymbolBuilder<P> {

		private String name;
		private String detail;
		private SymbolKind kind;
		private Boolean deprecated;
		private RangeBuilder<DocumentSymbolBuilder<P>> rangeBuilder;
		private Range range;
		private RangeBuilder<DocumentSymbolBuilder<P>> selectionRangeBuilder;
		private Range selectionRange;
		private List<DocumentSymbolBuilder<DocumentSymbolBuilder<P>>> childrenBuilders = new ArrayList<>();
		private List<DocumentSymbol> children = new ArrayList<>();

		InternalDocumentSymbolBuilder(P parent) {
			super(parent);
		}

		@Override
		public DocumentSymbolBuilder<P> name(String name) {
			this.name = name;
			return this;
		}

		@Override
		public DocumentSymbolBuilder<P> detail(String detail) {
			this.detail = detail;
			return this;
		}

		@Override
		public DocumentSymbolBuilder<P> kind(SymbolKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public DocumentSymbolBuilder<P> deprecated(Boolean deprecated) {
			this.deprecated = deprecated;
			return this;
		}

		@Override
		public RangeBuilder<DocumentSymbolBuilder<P>> range() {
			this.rangeBuilder = Range.range(this);
			return rangeBuilder;
		}

		@Override
		public DocumentSymbolBuilder<P> range(Range range) {
			this.range = range;
			return this;
		}

		@Override
		public RangeBuilder<DocumentSymbolBuilder<P>> selectionRange() {
			this.selectionRangeBuilder = Range.range(this);
			return selectionRangeBuilder;
		}

		@Override
		public DocumentSymbolBuilder<P> selectionRange(Range selectionRange) {
			this.selectionRange = selectionRange;
			return this;
		}

		@Override
		public DocumentSymbolBuilder<DocumentSymbolBuilder<P>> child() {
			DocumentSymbolBuilder<DocumentSymbolBuilder<P>> builder = documentSymbol(this);
			this.childrenBuilders.add(builder);
			return builder;
		}

		@Override
		public DocumentSymbolBuilder<P> child(DocumentSymbol child) {
			this.children.add(child);
			return this;
		}

		@Override
		public DocumentSymbol build() {
			DocumentSymbol documentSymbol = new DocumentSymbol();
			documentSymbol.setName(name);
			documentSymbol.setDetail(detail);
			documentSymbol.setKind(kind);
			documentSymbol.setDeprecated(deprecated);
			if (range != null) {
				documentSymbol.setRange(range);
			} else if (rangeBuilder != null) {
				documentSymbol.setRange(rangeBuilder.build());
			}
			if (selectionRange != null) {
				documentSymbol.setSelectionRange(selectionRange);
			} else if (selectionRangeBuilder != null) {
				documentSymbol.setSelectionRange(selectionRangeBuilder.build());
			}
			if (!childrenBuilders.isEmpty() || !children.isEmpty()) {
				ArrayList<DocumentSymbol> c = new ArrayList<>();
				if (!childrenBuilders.isEmpty()) {
					for (DocumentSymbolBuilder<DocumentSymbolBuilder<P>> b : childrenBuilders) {
						c.add(b.build());
					}
				}
				if (!children.isEmpty()) {
					c.addAll(children);
				}
				documentSymbol.setChildren(c);
			}
			return documentSymbol;
		}
	}
}
