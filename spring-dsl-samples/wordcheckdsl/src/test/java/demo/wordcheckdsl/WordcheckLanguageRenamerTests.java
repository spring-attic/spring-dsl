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
package demo.wordcheckdsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.dsl.document.Document;
import org.springframework.dsl.document.TextDocument;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.WorkspaceEdit;
import org.springframework.dsl.model.LanguageId;

/**
 * Tests for {@link WordcheckLanguageRenamer}.
 *
 * @author Janne Valkealahti
 *
 */
public class WordcheckLanguageRenamerTests {

	private final WordcheckLanguageSymbolizer symbolizer = new WordcheckLanguageSymbolizer();

	@Test
	public void test1() {
		WordcheckLanguageRenamer renamer = new WordcheckLanguageRenamer(symbolizer);
		renamer.getProperties().setWords(Arrays.asList("jack", "is", "a", "dull", "boy"));
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, "jack is a dull boy");
		WorkspaceEdit workspaceEdit = renamer.rename(document, Position.from(0, 0), "xxx").block();
		WorkspaceEdit expect = WorkspaceEdit.workspaceEdit()
			.changes("fakeuri")
				.newText("xxx")
				.range()
					.start()
						.line(0)
						.character(0)
						.and()
					.end()
						.line(0)
						.character(4)
						.and()
					.and()
				.and()
			.build();
		assertThat(workspaceEdit).isEqualTo(expect);
	}

	@Test
	public void test2() {
		WordcheckLanguageRenamer renamer = new WordcheckLanguageRenamer(symbolizer);
		renamer.getProperties().setWords(Arrays.asList("jack", "is", "a", "dull", "boy"));
		Document document = new TextDocument("fakeuri", LanguageId.TXT, 0, "jack is a dull boy");
		WorkspaceEdit workspaceEdit = renamer.rename(document, Position.from(0, 6), "xxx").block();
		WorkspaceEdit expect = WorkspaceEdit.workspaceEdit()
			.changes("fakeuri")
				.newText("xxx")
				.range()
					.start()
						.line(0)
						.character(5)
						.and()
					.end()
						.line(0)
						.character(7)
						.and()
					.and()
				.and()
			.build();
		assertThat(workspaceEdit).isEqualTo(expect);
	}
}
