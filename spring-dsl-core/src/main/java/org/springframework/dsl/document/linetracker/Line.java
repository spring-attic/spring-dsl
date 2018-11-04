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
package org.springframework.dsl.document.linetracker;

/**
 * Describes a line as a particular number of characters beginning at a
 * particular offset, consisting of a particular number of characters, and being
 * closed with a particular line delimiter.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
final class Line implements Region {

	public final String delimiter;
	public int offset;
	public int length;

	/**
	 * Creates a new Line.
	 *
	 * @param offset the offset of the line
	 * @param end the last including character offset of the line
	 * @param delimiter the line's delimiter
	 */
	public Line(int offset, int end, String delimiter) {
		this.offset = offset;
		this.length = (end - offset) + 1;
		this.delimiter = delimiter;
	}

	/**
	 * Creates a new Line.
	 *
	 * @param offset the offset of the line
	 * @param length the length of the line
	 */
	public Line(int offset, int length) {
		this.offset = offset;
		this.length = length;
		this.delimiter = null;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getLength() {
		return length;
	}
}
