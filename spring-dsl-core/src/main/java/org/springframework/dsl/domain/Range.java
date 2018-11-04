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

import org.springframework.dsl.domain.Position.PositionBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * A range in a text document expressed as (zero-based) start and end positions.
 * <p>
 * If you want to specify a range that contains a line including the line ending
 * character(s) then use an end position denoting the start of the next line.
 * <p>
 * For example:
 *
 * <pre>
 * {
 *   start: { line: 5, character: 23 }
 *   end : { line 6, character : 0 }
 * }
 * </pre>
 *
 * @author Janne Valkealahti
 *
 */
public class Range {

	private Position start;
	private Position end;

	public Range() {
	}

	public Range(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	public Range(int startLine, int startCharacter, int endLine, int endCharacter) {
		this.start = new Position(startLine, startCharacter);
		this.end = new Position(endLine, endCharacter);
	}

	public Range(int line, int startCharacter, int endCharacter) {
		this.start = new Position(line, startCharacter);
		this.end = new Position(line, endCharacter);
	}

	public static Range from(int startLine, int startCharacter, int endLine, int endCharacter) {
		return new Range(startLine, startCharacter, endLine, endCharacter);
	}

	public static Range from(Position start, Position end) {
		return new Range(Position.from(start), Position.from(end));
	}

	public Position getStart() {
		return start;
	}

	public void setStart(Position start) {
		this.start = start;
	}

	public Position getEnd() {
		return end;
	}

	public void setEnd(Position end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Range other = (Range) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + "]";
	}

	/**
	 * Builder interface for {@link Range}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface RangeBuilder<P> extends DomainBuilder<Range, P> {

		/**
		 * Gets a builder for start {@link Position}.
		 *
		 * @return the position builder
		 */
		PositionBuilder<RangeBuilder<P>> start();

		/**
		 * Gets a builder for end {@link Position}.
		 *
		 * @return the position builder
		 */
		PositionBuilder<RangeBuilder<P>> end();
	}

	/**
	 * Gets a builder for {@link Range}
	 *
	 * @return the range builder
	 */
	public static <P> RangeBuilder<P> range() {
		return new InternalRangeBuilder<>(null);
	}

	protected static <P> RangeBuilder<P> range(P parent) {
		return new InternalRangeBuilder<>(parent);
	}

	private static class InternalRangeBuilder<P>
			extends AbstractDomainBuilder<Range, P> implements RangeBuilder<P> {

		PositionBuilder<RangeBuilder<P>> start;
		PositionBuilder<RangeBuilder<P>> end;

		InternalRangeBuilder(P parent) {
			super(parent);
		}

		@Override
		public PositionBuilder<RangeBuilder<P>> start() {
			this.start = Position.position(this);
			return start;
		}

		@Override
		public PositionBuilder<RangeBuilder<P>> end() {
			this.end = Position.position(this);
			return end;
		}

		@Override
		public Range build() {
			Range range = new Range();
			if (start != null) {
				range.setStart(start.build());
			}
			if (end != null) {
				range.setEnd(end.build());
			}
			return range;
		}
	}
}
