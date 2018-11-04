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
package org.springframework.dsl.symboltable.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A unique set of strings mapped to a monotonically increasing index. These
 * indexes often useful to bytecode interpreters that have instructions
 * referring to strings by unique integer. Indexing is from 0.
 *
 * We can also get them back out in original order.
 *
 * Yes, I know that this is similar to {@link String#intern()} but in this case,
 * I need the index out not just to make these strings unique.
 * 
 * @author Original ANTLR Authors
 * @author Janne Valkealahti
 * 
 */
public class StringTable {

	protected LinkedHashMap<String, Integer> table = new LinkedHashMap<String, Integer>();
	protected int index = -1; // index we have just written
	protected List<String> strings = new ArrayList<>();

	public int add(String s) {
		Integer I = table.get(s);
		if (I != null)
			return I;
		index++;
		table.put(s, index);
		strings.add(s);
		return index;
	}

	/** Get the ith string or null if out of range */
	public String get(int i) {
		if (i < size() && i >= 0) {
			return strings.get(i);
		}
		return null;
	}

	public int size() {
		return table.size();
	}

	/**
	 * Return an array, possibly of length zero, with all strings sitting at their
	 * appropriate index within the array.
	 */
	public String[] toArray() {
		return strings.toArray(new String[strings.size()]);
	}

	/**
	 * Return a List, possibly of length zero, with all strings sitting at their
	 * appropriate index within the array.
	 */
	public List<String> toList() {
		return strings;
	}

	public int getNumberOfStrings() {
		return index + 1;
	}

	@Override
	public String toString() {
		return table.toString();
	}
}
