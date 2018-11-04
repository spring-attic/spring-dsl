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

import java.util.Arrays;

/**
 * Default implementation of {@link LineTracker}.
 * <p>
 * The line tracker considers the three common line delimiters which are '\n',
 * '\r', '\r\n'.
 *
 * @author Kris De Volder
 * @author Janne Valkealahti
 *
 */
public class DefaultLineTracker extends AbstractLineTracker {

	/** The predefined delimiters of this tracker */
	public final static String[] DELIMITERS= { "\r", "\n", "\r\n" };
	/** A predefined delimiter information which is always reused as return value */
	private DelimiterInfo fDelimiterInfo= new DelimiterInfo();


	/**
	 * Creates a standard line tracker.
	 */
	public DefaultLineTracker() {
	}

	@Override
	public String[] getLegalLineDelimiters() {
		return Arrays.copyOf(DELIMITERS, DELIMITERS.length);
	}

	@Override
	protected DelimiterInfo nextDelimiterInfo(String text, int offset) {

		char ch;
		int length= text.length();
		for (int i= offset; i < length; i++) {

			ch= text.charAt(i);
			if (ch == '\r') {

				if (i + 1 < length) {
					if (text.charAt(i + 1) == '\n') {
						fDelimiterInfo.delimiter= DELIMITERS[2];
						fDelimiterInfo.delimiterIndex= i;
						fDelimiterInfo.delimiterLength= 2;
						return fDelimiterInfo;
					}
				}

				fDelimiterInfo.delimiter= DELIMITERS[0];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;

			} else if (ch == '\n') {

				fDelimiterInfo.delimiter= DELIMITERS[1];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;
			}
		}

		return null;
	}
}
