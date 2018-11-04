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
package demo.simpledsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.model.LanguageId;
import org.springframework.dsl.support.DslUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * {@code simple} language representation containing parser to tokenize a dsl
 * which can be used to give various answers to {@code LSP} requests.
 * <p>
 * This language parser implementation is to showcase a raw ways to provide
 * concepts of ideas how {@code LSP} can be hooked into various services. In
 * a real world, you'd probably be better off with some real language parser line
 * {@code ANTRL} or similar. But having said that, this gives ideas how things
 * work together without introducing additional logic using external libraries.
 * <p>
 * Grammar of a {@code simple} language is:
 * <ul>
 * <li>Every line entry is a key/value pair separated with character {@code =}.</li>
 * <li>Keys can be of type; int, long, double or string.</li>
 * <li>Values needs to be parsable into types defined by a key.</li>
 * <li>Key can only exist once, meaning there can be max of 4 valid lines.</li>
 * <li>Line can be commented out using character {@code #}.</li>
 * <li>There can be empty lines.</li>
 * </ul>
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class SimpleLanguage {

	public final static LanguageId LANGUAGEID = LanguageId.languageId("simple", "Simple Language");
	private final static Pattern REGEX = Pattern.compile("[\\r\\n]+");
	private final Document document;
	private final List<Line> lines;

	/**
	 * Instantiates a new simple language.
	 *
	 * @param document the document
	 * @param lines the lines
	 */
	public SimpleLanguage(Document document, List<Line> lines) {
		this.document = document;
		this.lines = lines;
	}

	/**
	 * Gets the document known to this language.
	 *
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Gets the {@link Line}s known to this language.
	 *
	 * @return the lines
	 */
	public List<Line> getLines() {
		return lines;
	}

	/**
	 * Gets the token for this language for this particular {@link Position}. If
	 * there are no resolvable token in a specific position {@code NULL} is
	 * returned.
	 *
	 * @param position the position
	 * @return the token
	 */
	public Token getToken(Position position) {
		for (Line line : getLines()) {
			if (DslUtils.isPositionInRange(position, line.getKeyToken().getRange())) {
				return line.getKeyToken();
			}
		}
		return null;
	}

	public Collection<TokenType> resolveLegalTokens(Position position) {
		// filter out tokens which already exist in lines,
		// then from a given position check if we're expecting
		// key or value.
		Line line = getLine(position);
		return Arrays.stream(TokenType.values())
			.filter(tokenType -> {
				return !lines.stream().anyMatch(l -> {
					return l.getKeyToken().getType() == tokenType;
				});
			})
			.filter(tokenType -> {
				if (line == null) {
					return true;
				} else {
					if (!tokenType.toString().toLowerCase().startsWith(line.getKeyToken().value)) {
						return false;
					} else {
						return DslUtils.isPositionInRange(position, line.getKeyToken().getRange());
					}
				}
			})
			.collect(Collectors.toList());
	}

	private Line getLine(Position position) {
		return lines.stream()
			.filter(line -> line.getLine() == position.getLine())
			.findFirst()
			.orElse(null);
	}

	/**
	 * Parse a document and return a tokenized list of lines.
	 *
	 * @param document the document to parse
	 * @return the simple language structure
	 */
	public static SimpleLanguage build(Document document) {
		ArrayList<Line> lines = new ArrayList<>();

		String content = document.content();
		Matcher matcher = REGEX.matcher(content);

		int position = 0;
		int lineIndex = 0;
		boolean found = false;

		while (matcher.find()) {
			found = true;
			String line = content.substring(position, matcher.start());
			processLine(lines, line, lineIndex);
			position = matcher.end();
			lineIndex++;
		}

		if (!found) {
			processLine(lines, content, lineIndex);
		} else {
			processLine(lines, content.substring(position, content.length()), lineIndex);
		}

		return new SimpleLanguage(document, lines);
	}

	private static void processLine(ArrayList<Line> lines, String line, int lineIndex) {
		if (!StringUtils.hasText(line)) {
			return;
		}
		String[] split = line.split("=");
		// content before '=' with surrounding white spaces stripped is a key,
		// same for value after '='.
		if (split.length == 1) {
			Range range = Range.range()
					.start()
						.line(lineIndex)
						.character(0)
						.and()
					.end()
						.line(lineIndex)
						.character(split[0].length())
						.and()
					.build();
			KeyToken keyToken = new KeyToken(split[0], range, resolveType(split[0]));
			lines.add(new Line(lineIndex, keyToken, null));
		} else {
			Range range1 = Range.range()
					.start()
						.line(lineIndex)
						.character(0)
						.and()
					.end()
						.line(lineIndex)
						.character(split[0].length())
						.and()
					.build();
			Range range2 = Range.range()
					.start()
						.line(lineIndex)
						.character(split[0].length() + 1)
						.and()
					.end()
						.line(lineIndex)
						.character(line.length())
						.and()
					.build();
			KeyToken keyToken = new KeyToken(split[0], range1, resolveType(split[0]));
			ValueToken valueToken = new ValueToken(split[1], range2);
			lines.add(new Line(lineIndex, keyToken, valueToken));
		}
	}

	private static TokenType resolveType(String content) {
		TokenType tokenType = null;
		content = content.trim();
		TokenType[] keys = EnumSet.range(TokenType.INT, TokenType.STRING).toArray(new TokenType[0]);
		if (ObjectUtils.containsConstant(keys, content)) {
			tokenType = ObjectUtils.caseInsensitiveValueOf(keys, content);
		}
		return tokenType;
	}

	/**
	 * Represents a {@code line} in this document having a line index, and
	 * optionally existing {@link KeyToken} and {@link ValueToken}.
	 */
	public static class Line {

		private final int line;
		private final Token keyToken;
		private final Token valueToken;

		public Line(int line, Token keyToken, Token valueToken) {
			this.line = line;
			this.keyToken = keyToken;
			this.valueToken = valueToken;
		}

		public int getLine() {
			return line;
		}

		public Token getKeyToken() {
			return keyToken;
		}

		public Token getValueToken() {
			return valueToken;
		}
	}

	/**
	 * Token type shared with both keys and values.
	 */
	public enum TokenType {
		INT,
		LONG,
		DOUBLE,
		STRING,
		VALUE;
	}

	/**
	 * {@code Token} representing a {@code start} and {@code end} position in a
	 * hosting {@link Line} with its hosting {@link TokenType} and actual value.
	 */
	public static class Token {

		private Range range;
		private final TokenType type;
		private final String value;

		public Token(TokenType type, String value, Range range) {
			this.type = type;
			this.value = value;
			this.range = range;
		}

		public Range getRange() {
			return range;
		}

		public String getValue() {
			return value;
		}

		public TokenType getType() {
			return type;
		}

		public boolean isKey() {
			return !(type == TokenType.VALUE);
		}
	}

	private static class KeyToken extends Token {

		public KeyToken(String key, Range range, TokenType type) {
			super(type, key, range);
		}
	}

	private static class ValueToken extends Token {

		public ValueToken(String key, Range range) {
			super(TokenType.VALUE, key, range);
		}
	}
}
//end::snippet1[]
