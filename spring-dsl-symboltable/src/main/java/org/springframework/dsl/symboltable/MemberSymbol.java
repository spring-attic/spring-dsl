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
package org.springframework.dsl.symboltable;

/**
 * A {@link Symbol} within an aggregate like a {@code class} or {@code struct}.
 * Each {@link Symbol} in an aggregate knows its slot number so we can order
 * elements in memory, for example, or keep overridden method slots in sync for
 * vtables.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public interface MemberSymbol extends Symbol {

	/**
	 * Gets the slot number.
	 * <p>
	 * index giving order in the aggregate (from 0).
	 *
	 * @return the slot number
	 */
	int getSlotNumber();
}
