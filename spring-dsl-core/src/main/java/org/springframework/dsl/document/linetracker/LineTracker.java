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

import org.springframework.dsl.document.BadLocationException;

/**
 * A line tracker maps character positions to line numbers and vice versa.
 * Initially the line tracker is informed about its underlying text in order to
 * initialize the mapping information. After that, the line tracker is informed
 * about all changes of the underlying text allowing for incremental updates of
 * the mapping information. It is the client's responsibility to actively inform
 * the line tacker about text changes. For example, when using a line tracker in
 * combination with a document the document controls the line tracker.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public interface LineTracker {

	/**
	 * Returns the strings this tracker considers as legal line delimiters.
	 *
	 * @return the legal line delimiters
	 */
	String[] getLegalLineDelimiters();

	/**
	 * Returns the line delimiter of the specified line. Returns <code>null</code> if the
	 * line is not closed with a line delimiter.
	 *
	 * @param line the line whose line delimiter is queried
	 * @return the line's delimiter or <code>null</code> if line does not have a delimiter
	 * @exception BadLocationException if the line number is invalid in this tracker's line structure
	 */
	String getLineDelimiter(int line);

	/**
	 * Computes the number of lines in the given text.
	 *
	 * @param text the text whose number of lines should be computed
	 * @return the number of lines in the given text
	 */
	int computeNumberOfLines(String text);

	/**
	 * Returns the number of lines.
	 * <p>
	 * Note that a document always has at least one line.
	 * </p>
	 *
	 * @return the number of lines in this tracker's line structure
	 */
	int getNumberOfLines();

	/**
	 * Returns the number of lines which are occupied by a given text range.
	 *
	 * @param offset the offset of the specified text range
	 * @param length the length of the specified text range
	 * @return the number of lines occupied by the specified range
	 */
	int getNumberOfLines(int offset, int length);

	/**
	 * Returns the position of the first character of the specified line.
	 *
	 * @param line the line of interest
	 * @return offset of the first character of the line
	 */
	int getLineOffset(int line);

	/**
	 * Returns length of the specified line including the line's delimiter.
	 *
	 * @param line the line of interest
	 * @return the length of the line
	 */
	int getLineLength(int line);

	/**
	 * Returns the line number the character at the given offset belongs to.
	 *
	 * @param offset the offset whose line number to be determined
	 * @return the number of the line the offset is on
	 */
	int getLineNumberOfOffset(int offset);

	/**
	 * Returns a line description of the line at the given offset.
	 * The description contains the start offset and the length of the line
	 * excluding the line's delimiter.
	 *
	 * @param offset the offset whose line should be described
	 * @return a region describing the line
	 */
	Region getLineInformationOfOffset(int offset);

	/**
	 * Returns a line description of the given line. The description
	 * contains the start offset and the length of the line excluding the line's
	 * delimiter.
	 *
	 * @param line the line that should be described
	 * @return a region describing the line
	 */
	Region getLineInformation(int line);

	/**
	 * Informs the line tracker about the specified change in the tracked text.
	 *
	 * @param offset the offset of the replaced text
	 * @param length the length of the replaced text
	 * @param text the substitution text
	 */
	void replace(int offset, int length, String text);

	/**
	 * Sets the tracked text to the specified text.
	 *
	 * @param text the new tracked text
	 */
	void set(String text);
}
