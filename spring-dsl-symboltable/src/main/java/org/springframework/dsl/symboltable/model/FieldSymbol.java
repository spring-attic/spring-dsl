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
package org.springframework.dsl.symboltable.model;

import org.springframework.dsl.symboltable.MemberSymbol;

/**
 * A {@code FieldSymbol} is just a {@link VariableSymbol} that lives inside an
 * aggregate like a {@code class} or {@code struct}.
 *
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 *
 */
public class FieldSymbol extends VariableSymbol implements MemberSymbol {

	private int slotNumber;

	/**
	 * Instantiates a new field symbol.
	 *
	 * @param name the name
	 */
	public FieldSymbol(String name) {
		super(name);
	}

	@Override
	public int getSlotNumber() {
		return slotNumber;
	}

	/**
	 * Sets the slot number.
	 *
	 * @param slotNumber the new slot number
	 */
	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
}
