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
package org.springframework.dsl.domain;

public class CompletionContext {

	private CompletionTriggerKind triggerKind;

	private String triggerCharacter;

	public CompletionContext() {
	}

	public CompletionContext(CompletionTriggerKind triggerKind, String triggerCharacter) {
		this.triggerKind = triggerKind;
		this.triggerCharacter = triggerCharacter;
	}

	public CompletionTriggerKind getTriggerKind() {
		return triggerKind;
	}

	public void setTriggerKind(CompletionTriggerKind triggerKind) {
		this.triggerKind = triggerKind;
	}

	public String getTriggerCharacter() {
		return triggerCharacter;
	}

	public void setTriggerCharacter(String triggerCharacter) {
		this.triggerCharacter = triggerCharacter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((triggerCharacter == null) ? 0 : triggerCharacter.hashCode());
		result = prime * result + ((triggerKind == null) ? 0 : triggerKind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompletionContext other = (CompletionContext) obj;
		if (triggerCharacter == null) {
			if (other.triggerCharacter != null)
				return false;
		} else if (!triggerCharacter.equals(other.triggerCharacter))
			return false;
		if (triggerKind != other.triggerKind)
			return false;
		return true;
	}
}
