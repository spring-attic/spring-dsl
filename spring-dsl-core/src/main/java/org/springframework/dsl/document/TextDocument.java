/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.document;

import org.springframework.dsl.document.Region;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.domain.TextDocumentIdentifier;
import org.springframework.dsl.model.LanguageId;

/**
 * {@link Document} implementation having a textual content and understands
 * normal line delimiters.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class TextDocument implements Document {

	private final String uri;
	private final LanguageId languageId;
	private int version;
	private DocumentText text = new DocumentText("");
	private DocumentLineTracker lineTracker = new DefaultDocumentLineTracker();

	public TextDocument(String content) {
		this(null, null, 0, content);
	}

	public TextDocument(String uri, LanguageId languageId) {
		this(uri, languageId, 0, "");
	}

	public TextDocument(String uri, LanguageId languageId, int version, String text) {
		this.uri = uri;
		this.languageId = languageId;
		this.version = version;
		setText(text);
	}

	private TextDocument(TextDocument other) {
		this.uri = other.uri;
		this.languageId = other.languageId();
		this.text = other.text;
		this.lineTracker.set(text);
		this.version = other.version;
	}

	@Override
	public String uri() {
		return uri;
	}

	@Override
	public LanguageId languageId() {
		return languageId;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public DocumentText content() {
		return text;
	}

	@Override
	public int caret(Position position) {
		return lineTracker.getLineOffset(position.getLine()) + position.getCharacter();
	}

	@Override
	public Position toPosition(int offset) {
		int line = lineNumber(offset);
		int startOfLine = startOfLine(line);
		int column = offset - startOfLine;
		Position pos = new Position();
		pos.setCharacter(column);
		pos.setLine(line);
		return pos;
	}

	public synchronized void setText(String content) {
		this.text = new DocumentText(content);
		this.lineTracker.set(this.text);
	}

	/**
	 * Convert a simple offset+length pair into a {@link Range}. This is a method on
	 * TextDocument because it requires splitting document into lines to determine
	 * line numbers from offsets.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @return the range
	 * @throws BadLocationException the bad location exception
	 */
	@Override
	public Range toRange(int offset, int length) {
		int end = Math.min(offset + length, length());
		Range range = new Range();
		range.setStart(toPosition(offset));
		range.setEnd(toPosition(end));
		return range;
	}

	/**
	 * Determine the line-number a given offset (i.e. what line is the offset inside of?)
	 *
	 * @param offset the offset
	 * @return the int
	 * @throws BadLocationException the bad location exception
	 */
	private int lineNumber(int offset) {
		return lineTracker.getLineNumberOfOffset(offset);
	}

	@Override
	public int length() {
		return text.length();
	}

	@Override
	public DocumentText content(int start, int length) {
		return text.subtext(start, start + length);
	}

	@Override
	public DocumentText content(Range range) {
		int startOffset = toOffset(range.getStart());
		int endOffset = toOffset(range.getEnd());
		return content(startOffset, endOffset - startOffset);
	}

	@Override
	public int lineCount() {
		return lineTracker.getNumberOfLines();
	}

	@Override
	public char charAt(int offset) {
		if (offset >= 0 && offset < text.length()) {
			return text.charAt(offset);
		}
		throw new BadLocationException("Offset location not in bounds, offset=" + offset + " text length=" + text.length());
	}

	@Override
	public char charAtPosition(Position position) {
		return charAt(caret(position));
	}

	@Override
	public boolean positionInBounds(Position position) {
		int offset = caret(position);
		return offset >= 0 && offset < text.length();
	}

	@Override
	public int toOffset(Position position) {
		Region region = lineTracker.getLineInformation(position.getLine());
		int lineStart = region.getOffset();
		return lineStart + position.getCharacter();
	}

	@Override
	public Position validatePosition(Position position) {
		int adjustedLineCount = Math.min(position.getLine(), lineTracker.getNumberOfLines());
		int adjustedCharacter = Math.min(position.getCharacter(), lineTracker.getLineLength(adjustedLineCount) - 1);
		return Position.from(adjustedLineCount < 0 ? 0 : adjustedLineCount,
				adjustedCharacter < 0 ? 0 : adjustedCharacter);
	}

	public int getLineOfOffset(int offset) {
		return lineTracker.getLineNumberOfOffset(offset);
	}

	public Region getLineInformation(int line) {
		try {
			return lineTracker.getLineInformation(line);
		} catch (BadLocationException e) {
			//line doesn't exist
		}
		return null;
	}

	public Range getLineRange(int line) {
		Region range = getLineInformation(line);
		return toRange(range.getOffset(), range.getLength());
	}

	public int getLineOffset(int line) {
		return lineTracker.getLineOffset(line);
	}

	public synchronized void replace(int start, int len, String ins) {
		int end = start+len;
		text = text
			.delete(start, end)
			.insert(start, new DocumentText(ins));
		lineTracker.replace(start, len, new DocumentText(ins));
	}

	public synchronized TextDocument copy() {
		return new TextDocument(this);
	}

	public DocumentText textBetween(int start, int end) {
		return content(start, end-start);
	}

	public TextDocumentIdentifier getId() {
		if (uri!=null) {
			return new TextDocumentIdentifier(uri);
		}
		return null;
	}

	private int startOfLine(int line) {
		Region region = lineTracker.getLineInformation(line);
		return region.getOffset();
	}

	public Region getLineInformationOfOffset(int offset) {
		try {
			if (offset<=length()) {
				int line = lineNumber(offset);
				return getLineInformation(line);
			}
		} catch (BadLocationException e) {
			//outside document.
		}
		return null;
	}

	@Override
	public String toString() {
		return "TextDocument(uri="+uri+"["+version+"],\n"+this.text+"\n)";
	}
}
