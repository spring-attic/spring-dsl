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

import org.junit.jupiter.api.Test;

public class DefaultDocumentLineTrackerTests {

	@Test
	public void test1() {
		DefaultDocumentLineTracker tracker = new DefaultDocumentLineTracker();

		tracker.set(new DocumentText(""));
		assertThat(tracker.getNumberOfLines()).isEqualTo(1);
		assertThat(tracker.getLineLength(0)).isEqualTo(0);

		tracker.replace(0, 0, new DocumentText("1"));
		assertThat(tracker.getNumberOfLines()).isEqualTo(1);
		assertThat(tracker.getLineLength(0)).isEqualTo(1);
		assertThat(tracker.getLineDelimiter(0)).isNull();

		tracker.replace(0, 1, new DocumentText("1\n2"));
		assertThat(tracker.getNumberOfLines()).isEqualTo(2);
		assertThat(tracker.getLineLength(0)).isEqualTo(2);
		assertThat(tracker.getLineDelimiter(0)).isNotNull();
		assertThat(tracker.getLineDelimiter(0).toString()).isEqualTo("\n");
		assertThat(tracker.getLineLength(1)).isEqualTo(1);
		assertThat(tracker.getLineDelimiter(1)).isNull();

		tracker.replace(0, 3, new DocumentText("1\n2\n3"));
		assertThat(tracker.getNumberOfLines()).isEqualTo(3);
		assertThat(tracker.getLineLength(0)).isEqualTo(2);
		assertThat(tracker.getLineDelimiter(0).toString()).isEqualTo("\n");
		assertThat(tracker.getLineLength(1)).isEqualTo(2);
		assertThat(tracker.getLineDelimiter(1).toString()).isEqualTo("\n");
		assertThat(tracker.getLineLength(2)).isEqualTo(1);
		assertThat(tracker.getLineDelimiter(2)).isNull();

		tracker.set(new DocumentText(""));
		assertThat(tracker.getNumberOfLines()).isEqualTo(1);
		assertThat(tracker.getLineLength(0)).isEqualTo(0);
	}

}
