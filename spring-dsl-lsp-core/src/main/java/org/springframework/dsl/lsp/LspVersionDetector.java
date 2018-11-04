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
package org.springframework.dsl.lsp;

import org.springframework.dsl.domain.InitializeParams;
import org.springframework.dsl.domain.Synchronization;

/**
 * {@code LspVersionDetector} detects lsp version as {@link LspVersion} from
 * {@link InitializeParams} when client initialises its connection.
 *
 * @author Janne Valkealahti
 *
 */
public class LspVersionDetector {

	private final static LspVersion DEFAULT = LspVersion.VERSION_3_0;
	private final static LspVersionDetector INSTANCE = new LspVersionDetector();

	/**
	 * Detect lsp version using default static instance.
	 *
	 * @param params the params
	 * @return the lsp version
	 */
	public static LspVersion detect(InitializeParams params) {
		return INSTANCE.detectVersion(params);
	}

	/**
	 * Detect lsp version.
	 *
	 * @param params the params
	 * @return the lsp version
	 */
	public LspVersion detectVersion(InitializeParams params) {
		LspVersion version = detectVersion2x(params);
		if (version != null) {
			return version;
		}
		return DEFAULT;
	}

	private LspVersion detectVersion2x(InitializeParams params) {
		try {
			Synchronization synchronization = params.getCapabilities().getTextDocument().getSynchronization();
			if (synchronization.getDidSave() != null || synchronization.getWillSave() != null
					|| synchronization.getWillSaveWaitUntil() != null) {
				// willSave, willSaveWaitUntil or didSave exists. we're at least 3.x
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return LspVersion.VERSION_2;
	}

	public enum LspVersion {

		VERSION_2(2, null, null),
		VERSION_3_0(3, 0, null),
		VERSION_3_1_0(3, 1, 0),
		VERSION_3_2_0(3, 2, 0),
		VERSION_3_3_0(3, 3, 0),
		VERSION_3_4_0(3, 4, 0),
		VERSION_3_5_0(3, 5, 0),
		VERSION_3_6_0(3, 6, 0),
		VERSION_3_7_0(3, 7, 0),
		VERSION_3_8_0(3, 8, 0),
		VERSION_3_9_0(3, 9, 0),
		VERSION_3_10_0(3, 10, 0),
		VERSION_3_11_0(3, 11, 0),
		VERSION_3_12_0(3, 12, 0),
		VERSION_3_13_0(3, 13, 0);

		private final Integer major;
		private final Integer minor;
		private final Integer patch;

		LspVersion(Integer major, Integer minor, Integer patch) {
			this.major = major;
			this.minor = minor;
			this.patch = patch;
		}

		public Integer getMajor() {
			return major;
		}

		public Integer getMinor() {
			return minor;
		}

		public Integer getPatch() {
			return patch;
		}

		public boolean is2x() {
			return major != null && major == 2;
		}

		public boolean is3x() {
			return major != null && major == 3;
		}
	}
}
