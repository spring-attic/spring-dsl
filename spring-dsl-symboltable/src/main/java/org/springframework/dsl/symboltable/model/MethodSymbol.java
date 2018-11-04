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
 * A method symbol is a function that lives within an aggregate/class and has a
 * slot number.
 * 
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 * 
 */
public class MethodSymbol extends FunctionSymbol implements MemberSymbol {

	protected int slot = -1;

	public MethodSymbol(String name) {
		super(name);
	}

	@Override
	public int getSlotNumber() {
		return slot;
	}
}
