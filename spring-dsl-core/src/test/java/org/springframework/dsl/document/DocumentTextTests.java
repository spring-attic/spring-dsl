/*
 * Copyright 2019 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class DocumentTextTests {

	public final static String content1 = "int=0\nlong=0\ndouble=0.0\nstring=hi";
	public final static String content2 = "int=0";
	private final static Pattern REGEX = Pattern.compile("[\\r\\n]+");

	@Test
	public void testRegexDoesntError() {
		Document document = new TextDocument(content1);
		DocumentText dt = document.content();
		Matcher matcher = REGEX.matcher(dt);
		while (matcher.find()) {
			matcher.end();
		}
	}

	@Test
	public void testSplitFirst() {
		Document document = new TextDocument(content2);
		DocumentText dt = document.content();
		DocumentText[] split = dt.splitFirst('=');
		assertThat(split).hasSize(2);
		assertThat(split[0].toString()).isEqualTo("int");
		assertThat(split[1].toString()).isEqualTo("0");

		split = dt.splitFirst('|');
		assertThat(split).hasSize(1);
		assertThat(split[0].toString()).isEqualTo("int=0");

		split = dt.splitFirst('i');
		assertThat(split).hasSize(2);
		assertThat(split[0].toString()).isEqualTo("");
		assertThat(split[1].toString()).isEqualTo("nt=0");

		split = dt.splitFirst('0');
		assertThat(split).hasSize(2);
		assertThat(split[0].toString()).isEqualTo("int=");
		assertThat(split[1].toString()).isEqualTo("");
	}
}
