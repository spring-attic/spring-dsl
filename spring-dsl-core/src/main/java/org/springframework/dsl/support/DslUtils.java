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
package org.springframework.dsl.support;

import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;

/**
 * Various utility functions for {@code DSL}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class DslUtils {

	/**
	 * Checks inclusively if position is within range limits.
	 *
	 * @param position the position
	 * @param range the range
	 * @return {@code TRUE} if position is in range
	 */
	public static boolean isPositionInRange(Position position, Range range) {
		if (position == null || range == null || range.getStart() == null || range.getEnd() == null) {
			return false;
		}
		if (position.getLine() < range.getStart().getLine()) {
			return false;
		}
		if (position.getLine() > range.getEnd().getLine()) {
			return false;
		}
		if (position.getCharacter() < range.getStart().getCharacter()) {
			return false;
		}
		if (position.getCharacter() > range.getEnd().getCharacter()) {
			return false;
		}
		return true;
	}
}
