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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.model.LanguageId;

import demo.simpledsl.SimpleLanguage.Line;
import demo.simpledsl.SimpleLanguage.Token;
import demo.simpledsl.SimpleLanguage.TokenType;

public class SimpleLanguageTests {

	public final static String content1 = "int=0\nlong=0\ndouble=0.0\nstring=hi";
	public final static String content2 = "int=0\rlong=0\rdouble=0.0\rstring=hi";
	public final static String content3 = "int=0\r\nlong=0\r\ndouble=0.0\r\nstring=hi";
	public final static String content4 = "int=0";
	public final static String content5 = "int=";
	public final static String content6 = "int";
	public final static String content7 = "int=hi";
	public final static String content8 = "ddd=xxx";
	public final static String content9 = "long=hi";
	public final static String content10 = "double=hi";

	@Test
	public void testParse() {
		Document document = new TextDocument("", LanguageId.TXT, 0, content1);
		List<Line> lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(4);

		document = new TextDocument("", LanguageId.TXT, 0, content2);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(4);

		document = new TextDocument("", LanguageId.TXT, 0, content3);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(4);

		document = new TextDocument("", LanguageId.TXT, 0, content4);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(1);

		document = new TextDocument("", LanguageId.TXT, 0, content5);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(1);

		document = new TextDocument("", LanguageId.TXT, 0, content6);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(1);

		document = new TextDocument("", LanguageId.TXT, 0, content7);
		lines = SimpleLanguage.build(document).getLines();
		assertThat(lines.size()).isEqualTo(1);
	}

	@Test
	public void testTokenFromPositions() {
		Document document = new TextDocument("", LanguageId.TXT, 0, content4);
		List<Line> lines = SimpleLanguage.build(document).getLines();
		SimpleLanguage language = new SimpleLanguage(document, lines);
		Token token = language.getToken(Position.position().line(0).character(0).build());
		assertThat(token).isNotNull();
		assertThat(token.getType()).isEqualTo(TokenType.INT);

		document = new TextDocument("", LanguageId.TXT, 0, content5);
		lines = SimpleLanguage.build(document).getLines();
		language = new SimpleLanguage(document, lines);
		token = language.getToken(Position.position().line(0).character(3).build());
		assertThat(token).isNotNull();
		assertThat(token.getType()).isEqualTo(TokenType.INT);
		token = language.getToken(Position.position().line(0).character(4).build());
		assertThat(token).isNull();
	}
}
