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

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * Position in a text document expressed as zero-based line and character
 * offset. The offsets are based on a UTF-16 string representation. So a string
 * of the form `a𐐀b` the character offset of the character `a` is 0, the
 * character offset of `𐐀` is 1 and the character offset of b is 3 since `𐐀`
 * is represented using two code units in UTF-16.
 * <p>
 * Positions are line end character agnostic. So you can not specify a position
 * that denotes `\r|\n` or `\n|` where `|` represents the character offset.
 *
 * @author Janne Valkealahti
 *
 */
public class Position {

	private int line;
	private int character;

	public Position() {
	}

	public Position(int line, int character) {
		this.line = line;
		this.character = character;
	}

	public Position(Position other) {
		this.line = other.getLine();
		this.character = other.getCharacter();
	}

	public static Position from(Position other) {
		return new Position(other);
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public static Position from(int line, int character) {
		return new Position(line, character);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + character;
		result = prime * result + line;
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
		Position other = (Position) obj;
		if (character != other.character)
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [line=" + line + ", character=" + character + "]";
	}

	/**
	 * Builder interface for {@link Position}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface PositionBuilder<P> extends DomainBuilder<Position, P>{

		/**
		 * Sets a line.
		 *
		 * @param line the line
		 * @return the builder for chaining
		 */
		PositionBuilder<P> line(int line);

		/**
		 * Sets a character.
		 *
		 * @param character the character
		 * @return the builder for chaining
		 */
		PositionBuilder<P> character(int character);
	}

	/**
	 * Gets a builder for {@link Position}
	 *
	 * @return the position builder
	 */
	public static <P> PositionBuilder<P> position() {
		return new InternalPositionBuilder<>(null);
	}

	protected static <P> PositionBuilder<P> position(P parent) {
		return new InternalPositionBuilder<>(parent);
	}

	private static class InternalPositionBuilder<P> extends AbstractDomainBuilder<Position, P> implements PositionBuilder<P>{

		private int line;
		private int character;

		InternalPositionBuilder(P parent) {
			super(parent);
		}

		@Override
		public PositionBuilder<P> line(int line) {
			this.line = line;
			return this;
		}

		@Override
		public PositionBuilder<P> character(int character) {
			this.character = character;
			return this;
		}

		@Override
		public Position build() {
			return new Position(line, character);
		}
	}
}
