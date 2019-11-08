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

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code FoldingRange}.
 *
 * @author Janne Valkealahti
 *
 */
public class FoldingRange {

	private Integer startLine;
	private Integer startCharacter;
	private Integer endLine;
	private Integer endCharacter;
	private FoldingRangeKind kind;

	/**
	 * Instantiates a new folding range.
	 */
	public FoldingRange() {
	}

	/**
	 * Instantiates a new folding range.
	 *
	 * @param startLine the start line
	 * @param startCharacter the start character
	 * @param endLine the end line
	 * @param endCharacter the end character
	 * @param kind the folding range kind
	 */
	public FoldingRange(Integer startLine, Integer startCharacter, Integer endLine, Integer endCharacter,
			FoldingRangeKind kind) {
		this.startLine = startLine;
		this.startCharacter = startCharacter;
		this.endLine = endLine;
		this.endCharacter = endCharacter;
		this.kind = kind;
	}

	public Integer getStartLine() {
		return startLine;
	}

	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}

	public Integer getStartCharacter() {
		return startCharacter;
	}

	public void setStartCharacter(Integer startCharacter) {
		this.startCharacter = startCharacter;
	}

	public Integer getEndLine() {
		return endLine;
	}

	public void setEndLine(Integer endLine) {
		this.endLine = endLine;
	}

	public Integer getEndCharacter() {
		return endCharacter;
	}

	public void setEndCharacter(Integer endCharacter) {
		this.endCharacter = endCharacter;
	}

	public FoldingRangeKind getKind() {
		return kind;
	}

	public void setKind(FoldingRangeKind kind) {
		this.kind = kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endCharacter == null) ? 0 : endCharacter.hashCode());
		result = prime * result + ((endLine == null) ? 0 : endLine.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((startCharacter == null) ? 0 : startCharacter.hashCode());
		result = prime * result + ((startLine == null) ? 0 : startLine.hashCode());
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
		FoldingRange other = (FoldingRange) obj;
		if (endCharacter == null) {
			if (other.endCharacter != null)
				return false;
		} else if (!endCharacter.equals(other.endCharacter))
			return false;
		if (endLine == null) {
			if (other.endLine != null)
				return false;
		} else if (!endLine.equals(other.endLine))
			return false;
		if (kind != other.kind)
			return false;
		if (startCharacter == null) {
			if (other.startCharacter != null)
				return false;
		} else if (!startCharacter.equals(other.startCharacter))
			return false;
		if (startLine == null) {
			if (other.startLine != null)
				return false;
		} else if (!startLine.equals(other.startLine))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FoldingRange [endCharacter=" + endCharacter + ", endLine=" + endLine + ", kind=" + kind
				+ ", startCharacter=" + startCharacter + ", startLine=" + startLine + "]";
	}

	/**
	 * Builder interface for {@link FoldingRange}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface FoldingRangeBuilder<P> extends DomainBuilder<FoldingRange, P>{

		/**
		 * Sets a start line.
		 *
		 * @param startLine the start line
		 * @return the builder for chaining
		 */
		FoldingRangeBuilder<P> startLine(Integer startLine);

		/**
		 * Sets a start character.
		 *
		 * @param startCharacter the start character
		 * @return the builder for chaining
		 */
		FoldingRangeBuilder<P> startCharacter(Integer startCharacter);

		/**
		 * Sets a end line.
		 *
		 * @param endLine the end line
		 * @return the builder for chaining
		 */
		FoldingRangeBuilder<P> endLine(Integer endLine);

		/**
		 * Sets a end character.
		 *
		 * @param endCharacter the end character
		 * @return the builder for chaining
		 */
		FoldingRangeBuilder<P> endCharacter(Integer endCharacter);

		/**
		 * Sets a folding range kind.
		 *
		 * @param kind the folding range kind
		 * @return the builder for chaining
		 */
		FoldingRangeBuilder<P> kind(FoldingRangeKind kind);
	}

	/**
	 * Gets a builder for {@link FoldingRange}
	 *
	 * @return the folding range builder
	 */
	public static <P> FoldingRangeBuilder<P> foldingRange() {
		return new InternalFoldingRangeBuilder<>(null);
	}

	protected static <P> FoldingRangeBuilder<P> foldingRange(P parent) {
		return new InternalFoldingRangeBuilder<>(parent);
	}

	private static class InternalFoldingRangeBuilder<P> extends AbstractDomainBuilder<FoldingRange, P>
			implements FoldingRangeBuilder<P> {

		private Integer startLine;
		private Integer startCharacter;
		private Integer endLine;
		private Integer endCharacter;
		private FoldingRangeKind kind;

		InternalFoldingRangeBuilder(P parent) {
			super(parent);
		}

		@Override
		public FoldingRangeBuilder<P> startLine(Integer startLine) {
			this.startLine = startLine;
			return this;
		}

		@Override
		public FoldingRangeBuilder<P> startCharacter(Integer startCharacter) {
			this.startCharacter = startCharacter;
			return this;
		}

		@Override
		public FoldingRangeBuilder<P> endLine(Integer endLine) {
			this.endLine = endLine;
			return this;
		}

		@Override
		public FoldingRangeBuilder<P> endCharacter(Integer endCharacter) {
			this.endCharacter = endCharacter;
			return this;
		}

		@Override
		public FoldingRangeBuilder<P> kind(FoldingRangeKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public FoldingRange build() {
			return new FoldingRange(startLine, startCharacter, endLine, endCharacter, kind);
		}
	}
}
