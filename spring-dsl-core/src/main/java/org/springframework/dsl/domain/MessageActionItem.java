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

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code MessageActionItem}.
 *
 * @author Janne Valkealahti
 *
 */
public class MessageActionItem {

	private String title;

	/**
	 * Instantiates a new message action item.
	 */
	public MessageActionItem() {
	}

	/**
	 * Instantiates a new message action item.
	 *
	 * @param title the title
	 */
	public MessageActionItem(String title) {
		this.title = title;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		MessageActionItem other = (MessageActionItem) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link MessageActionItem}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface MessageActionItemBuilder<P> extends DomainBuilder<MessageActionItem, P> {

		/**
		 * Sets a title.
		 *
		 * @param title the title
		 * @return the builder for chaining
		 */
		MessageActionItemBuilder<P> title(String title);
	}

	/**
	 * Gets a builder for {@link MessageActionItem}.
	 *
	 * @return the message action item builder
	 */
	public static <P> MessageActionItemBuilder<P> messageActionItem() {
		return new InternalMessageActionItemBuilder<>(null);
	}

	protected static <P> MessageActionItemBuilder<P> messageActionItem(P parent) {
		return new InternalMessageActionItemBuilder<>(parent);
	}

	private static class InternalMessageActionItemBuilder<P> extends AbstractDomainBuilder<MessageActionItem, P>
			implements MessageActionItemBuilder<P> {

		private String title;

		InternalMessageActionItemBuilder(P parent) {
			super(parent);
		}

		@Override
		public MessageActionItemBuilder<P> title(String title) {
			this.title = title;
			return this;
		}

		@Override
		public MessageActionItem build() {
			return new MessageActionItem(title);
		}
	}
}
