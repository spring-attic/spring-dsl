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
 * Abstract implementation of <code>ILineTracker</code>. It lets the definition of line
 * delimiters to subclasses. Assuming that '\n' is the only line delimiter, this abstract
 * implementation defines the following line scheme:
 * <ul>
 * <li> "" - [0,0]
 * <li> "a" - [0,1]
 * <li> "\n" - [0,1], [1,0]
 * <li> "a\n" - [0,2], [2,0]
 * <li> "a\nb" - [0,2], [2,1]
 * <li> "a\nbc\n" - [0,2], [2,3], [5,0]
 * </ul>
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractLineTracker implements LineTracker/*, LineTrackerExtension*/ {

	/**
	 * Combines the information of the occurrence of a line delimiter. <code>delimiterIndex</code>
	 * is the index where a line delimiter starts, whereas <code>delimiterLength</code>,
	 * indicates the length of the delimiter.
	 */
	protected static class DelimiterInfo {

		public int delimiterIndex;
		public int delimiterLength;
		public String delimiter;
	}

	/**
	 * Representation of replace and set requests.
	 */
	protected static class Request {

		public final int offset;
		public final int length;
		public final String text;

		/**
		 * Instantiates a new request.
		 *
		 * @param offset the offset
		 * @param length the length
		 * @param text the text
		 */
		public Request(int offset, int length, String text) {
			this.offset= offset;
			this.length= length;
			this.text= text;
		}

		/**
		 * Instantiates a new request.
		 *
		 * @param text the text
		 */
		public Request(String text) {
			this.offset= -1;
			this.length= -1;
			this.text= text;
		}

		/**
		 * Checks if is replace request.
		 *
		 * @return true, if is replace request
		 */
		public boolean isReplaceRequest() {
			return this.offset > -1 && this.length > -1;
		}
	}

	/**
	 * The implementation that this tracker delegates to.
	 */
	private LineTracker delegate= new ListLineTracker() {
		@Override
		public String[] getLegalLineDelimiters() {
			return AbstractLineTracker.this.getLegalLineDelimiters();
		}

		@Override
		protected DelimiterInfo nextDelimiterInfo(String text, int offset) {
			return AbstractLineTracker.this.nextDelimiterInfo(text, offset);
		}
	};

	/**
	 * Whether the delegate needs conversion when the line structure is modified.
	 */
	private boolean needsConversion= true;

	/**
	 * Creates a new line tracker.
	 */
	protected AbstractLineTracker() {
	}

	@Override
	public int computeNumberOfLines(String text) {
		return delegate.computeNumberOfLines(text);
	}

	@Override
	public String getLineDelimiter(int line) throws BadLocationException {
		return delegate.getLineDelimiter(line);
	}

	@Override
	public Region getLineInformation(int line) {
		return delegate.getLineInformation(line);
	}

//	@Override
//	public Range getLineInformationRange(int line) {
//		return delegate.getLineInformationRange(line);
//	}

	@Override
	public Region getLineInformationOfOffset(int offset) throws BadLocationException {
		return delegate.getLineInformationOfOffset(offset);
	}

	@Override
	public int getLineLength(int line) throws BadLocationException {
		return delegate.getLineLength(line);
	}

	@Override
	public int getLineNumberOfOffset(int offset) throws BadLocationException {
		return delegate.getLineNumberOfOffset(offset);
	}

	@Override
	public int getLineOffset(int line) throws BadLocationException {
		return delegate.getLineOffset(line);
	}

	@Override
	public int getNumberOfLines() {
		return delegate.getNumberOfLines();
	}

	@Override
	public int getNumberOfLines(int offset, int length) throws BadLocationException {
		return delegate.getNumberOfLines(offset, length);
	}

	@Override
	public void set(String text) {
		delegate.set(text);
	}

	@Override
	public void replace(int offset, int length, String text) throws BadLocationException {
		checkImplementation();
		delegate.replace(offset, length, text);
	}

	/**
	 * Converts the implementation to be a {@link TreeLineTracker} if it isn't yet.
	 */
	private void checkImplementation() {
		if (needsConversion) {
			needsConversion= false;
			delegate= new TreeLineTracker((ListLineTracker) delegate) {
				@Override
				protected DelimiterInfo nextDelimiterInfo(String text, int offset) {
					return AbstractLineTracker.this.nextDelimiterInfo(text, offset);
				}

				@Override
				public String[] getLegalLineDelimiters() {
					return AbstractLineTracker.this.getLegalLineDelimiters();
				}
			};
		}
	}

	/**
	 * Returns the information about the first delimiter found in the given text starting at the
	 * given offset.
	 *
	 * @param text the text to be searched
	 * @param offset the offset in the given text
	 * @return the information of the first found delimiter or <code>null</code>
	 */
	protected abstract DelimiterInfo nextDelimiterInfo(String text, int offset);
}
