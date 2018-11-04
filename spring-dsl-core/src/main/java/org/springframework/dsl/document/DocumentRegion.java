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
package org.springframework.dsl.document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dsl.document.linetracker.Region;
import org.springframework.dsl.domain.Range;
import org.springframework.util.Assert;

/**
 * Represents a region of text in a document.
 * <p>
 * Caution: assumes the underlying document is not mutated during the lifetime
 * of the region object (otherwise start/end positions may no longer be valid).
 * <p>
 * Implements {@link CharSequence} for convenience (e.g you can use
 * {@link DocumentRegion} as input to a {@link Pattern} and other standard JRE
 * functions which expect a {@link CharSequence}.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class DocumentRegion implements CharSequence, Region {

	final Document document;
	final int start;
	final int end;

	/**
	 * Instantiates a new document region.
	 *
	 * @param document the document
	 * @param region the region
	 */
	public DocumentRegion(Document document, Region region) {
		this(document, region.getOffset(), region.getOffset() + region.getLength());
	}

	/**
	 * Constructs a {@link DocumentRegion} on a given document. Tries its best to
	 * behave sensibly when passed 'strange' coordinates by adjusting them logically
	 * rather than throw an Exception.
	 * <p>
	 * A position before the start of the document is moved to be the start of the
	 * document.
	 * <p>
	 * A position after the end of the document is moved to the end of the document.
	 * <p>
	 * If 'end' position is before the start position it is moved be exactly at the
	 * start position (this avoids region with negative length).
	 */
	public DocumentRegion(Document document, int start, int end) {
		this.document = document;
		this.start = limitRange(start, 0, document.length());
		this.end = limitRange(end, start, document.length());
	}

	public DocumentRegion(Document doc) {
		this(doc, 0, doc.length());
	}

	public DocumentRegion(Document document, Range range) {
		this.document = document;
		this.start = document.toOffset(range.getStart());
		this.end = document.toOffset(range.getEnd());
	}

	@Override
	public int length() {
		return end - start;
	}

	/**
	 * Gets character from the region, offset from the start of the region
	 *
	 * @return the character from the document (char)0 if the offset is outside the
	 *         region.
	 */
	@Override
	public char charAt(int offset) {
		if (offset < 0 || offset >= length()) {
			throw new IndexOutOfBoundsException("" + offset);
		}
		try {
			return document.charAt(start + offset);
		} catch (BadLocationException e) {
			throw new IndexOutOfBoundsException("" + offset);
		}
	}

	@Override
	public DocumentRegion subSequence(int start, int end) {
		int len = length();
		Assert.isTrue(start >= 0, "'start' cannot be negative");
		Assert.isTrue(end <= len, "'end' cannot be higher than current length");
		if (start == 0 && end == len) {
			return this;
		}
		return new DocumentRegion(document, this.start + start, this.start + end);
	}

	public Range toRange() {
		return document.toRange(start, end - start);
	}

	private int limitRange(int offset, int min, int max) {
		if (offset < min) {
			return min;
		}
		if (offset > max) {
			return max;
		}
		return offset;
	}

	public DocumentRegion trim() {
		return trimEnd().trimStart();
	}

	public DocumentRegion trimStart() {
		int howMany = 0;
		int len = length();
		while (howMany < len && Character.isWhitespace(charAt(howMany))) {
			howMany++;
		}
		return subSequence(howMany, len);
	}

	public DocumentRegion trimEnd() {
		int howMany = 0; // how many chars to remove from the end
		int len = length();
		int lastChar = len - 1;
		while (howMany < len && Character.isWhitespace(charAt(lastChar - howMany))) {
			howMany++;
		}
		if (howMany > 0) {
			return subSequence(0, len - howMany);
		}
		return this;
	}

	/**
	 * Determines whether this range contains a given (absolute) offset.
	 * <p>
	 * Note that even a empty region will be treated as containing at least the
	 * starting offset.
	 * <p>
	 * In other words, the 'end' position is considered as being contained in the
	 * region.
	 */
	public boolean containsOffset(int absoluteOffset) {
		return absoluteOffset >= start && absoluteOffset <= end;
	}

	public boolean isEmpty() {
		return length() == 0;
	}

	public DocumentRegion subSequence(int start) {
		return subSequence(start, length());
	}

	public int indexOf(char ch, int fromIndex) {
		while (fromIndex < length()) {
			if (charAt(fromIndex) == ch) {
				return fromIndex;
			}
			fromIndex++;
		}
		return -1;
	}

	public int indexOf(char c) {
		return indexOf(c, 0);
	}

	public DocumentRegion[] split(char c) {
		List<DocumentRegion> pieces = new ArrayList<>();
		int start = 0;
		int end;
		while ((end = indexOf(c, start)) >= 0) {
			pieces.add(subSequence(start, end));
			start = end + 1;
		}
		// Do not forget the last piece!
		pieces.add(subSequence(start, length()));
		return pieces.toArray(new DocumentRegion[pieces.size()]);
	}

	public DocumentRegion[] split(Pattern delimiter) {
		List<DocumentRegion> pieces = new ArrayList<>();
		int start = 0;
		Matcher matcher = delimiter.matcher(this);
		while (matcher.find(start)) {
			int end = matcher.start();
			pieces.add(subSequence(start, end));
			start = matcher.end();
		}
		// Do not forget the last piece!
		pieces.add(subSequence(start, length()));
		return pieces.toArray(new DocumentRegion[pieces.size()]);
	}

	/**
	 * Removes a single occurrence of pat from the start of this region.
	 */
	public DocumentRegion trimStart(Pattern pat) {
		pat = Pattern.compile("^(" + pat.pattern() + ")");
		Matcher matcher = pat.matcher(this);
		if (matcher.find()) {
			return subSequence(matcher.end());
		}
		return this;
	}

	/**
	 * Removes a single occurrence of pat from the end of this region.
	 */
	public DocumentRegion trimEnd(Pattern pat) {
		pat = Pattern.compile("(" + pat.pattern() + ")$");
		Matcher matcher = pat.matcher(this);
		if (matcher.find()) {
			return subSequence(0, matcher.start());
		}
		return this;
	}

	/**
	 * Get the region after this one with a given lenght.
	 * <p>
	 * If the document is too short to provide the requested lenght then the region
	 * is truncated to end of the document.
	 */
	public DocumentRegion textAfter(int len) {
		Assert.isTrue(len >= 0, "length cannot be negative");
		return new DocumentRegion(document, end, end + len);
	}

	/**
	 * Get the region before this one with a given lenght.
	 * <p>
	 * If the requested region extends before the start of the document, then the
	 * region is shortened so its start coincides with document start.
	 */
	public DocumentRegion textBefore(int len) {
		Assert.isTrue(len >= 0, "length cannot be negative");
		return new DocumentRegion(document, start - len, start);
	}

	/**
	 * Get the start of this region in 'absolute' terms (i.e. relative to the
	 * document).
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Get the end of this region in 'absolute' terms (i.e. relative to the
	 * document).
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Convert the given document offset into an offset relative to this region.
	 */
	public int toRelative(int offset) {
		return offset - start;
	}

	@Override
	public int getLength() {
		return length();
	}

	public boolean endsWith(CharSequence string) {
		int myLen = length();
		int strLen = string.length();
		if (myLen >= strLen) {
			for (int i = 0; i < strLen; i++) {
				if (charAt(myLen - strLen + i) != string.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean startsWith(CharSequence string) {
		int myLen = length();
		int strLen = string.length();
		if (myLen >= strLen) {
			for (int i = 0; i < strLen; i++) {
				if (charAt(i) != string.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Take documentRegion at the end of
	 */
	public DocumentRegion textAtEnd(int numChars) {
		numChars = Math.min(getLength(), numChars);
		return new DocumentRegion(document, end - numChars, end);
	}

	public DocumentRegion leadingWhitespace() {
		int howMany = 0;
		int len = length();
		while (howMany < len && Character.isWhitespace(charAt(howMany))) {
			howMany++;
		}
		return subSequence(0, howMany);
	}

	@Override
	public int getOffset() {
		return getStart();
	}

	@Override
	public String toString() {
		return textBetween(document, start, end);
	}

	public static String textBetween(Document doc, int start, int end) {
		if (start >= doc.length()) {
			return "";
		}
		if (start < 0) {
			start = 0;
		}
		if (end > doc.length()) {
			end = doc.length();
		}
		if (end < start) {
			end = start;
		}
		try {
			return doc.content(start, end - start);
		} catch (BadLocationException e) {
			throw new IllegalStateException("Bug!", e);
		}
	}

}