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
package org.springframework.dsl.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link LanguageId}.
 *
 * @author Janne Valkealahti
 *
 */
public class LanguageIdTests {

	@Test
	public void testEquals() {
		assertThat(LanguageId.TXT).isNotEqualTo(LanguageId.BAT);
		assertThat(LanguageId.TXT).isNotEqualTo(LanguageId.ALL);
	}

	@Test
	public void testCompatible() {
		assertThat(LanguageId.TXT.isCompatibleWith(LanguageId.BAT)).isFalse();
		assertThat(LanguageId.TXT.isCompatibleWith(LanguageId.ALL)).isFalse();
		assertThat(LanguageId.ALL.isCompatibleWith(LanguageId.TXT)).isTrue();
		assertThat(LanguageId.PERL.isCompatibleWith(LanguageId.PERL6)).isTrue();
		assertThat(LanguageId.PERL6.isCompatibleWith(LanguageId.PERL)).isTrue();
		assertThat(LanguageId.GIT_COMMIT.isCompatibleWith(LanguageId.GIT_REBASE)).isTrue();
		assertThat(LanguageId.GIT_REBASE.isCompatibleWith(LanguageId.GIT_COMMIT)).isTrue();
	}
}
