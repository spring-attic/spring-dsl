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
 * {@code LSP} domain object for a specification {@code MessageParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class MessageParams {

	private MessageType type;
	private String message;

	/**
	 * Instantiates a new message params.
	 */
	public MessageParams() {
	}

	/**
	 * Instantiates a new message params.
	 *
	 * @param type the type
	 * @param message the message
	 */
	public MessageParams(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MessageParams other = (MessageParams) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link MessageParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface MessageParamsBuilder<P> extends DomainBuilder<MessageParams, P> {

		/**
		 * Sets a message.
		 *
		 * @param message the message
		 * @return the builder for chaining
		 */
		MessageParamsBuilder<P> message(String message);

		/**
		 * Sets a type.
		 *
		 * @param type the type
		 * @return the builder for chaining
		 */
		MessageParamsBuilder<P> type(MessageType type);
	}

	/**
	 * Gets a builder for {@link MessageParams}.
	 *
	 * @return the message params builder
	 */
	public static <P> MessageParamsBuilder<P> messageParams() {
		return new InternalMessageParamsBuilder<>(null);
	}

	protected static <P> MessageParamsBuilder<P> messageParams(P parent) {
		return new InternalMessageParamsBuilder<>(parent);
	}

	private static class InternalMessageParamsBuilder<P> extends AbstractDomainBuilder<MessageParams, P>
			implements MessageParamsBuilder<P> {

		private MessageType type;
		private String message;

		InternalMessageParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public MessageParamsBuilder<P> message(String message) {
			this.message = message;
			return this;
		}

		@Override
		public MessageParamsBuilder<P> type(MessageType type) {
			this.type = type;
			return this;
		}

		@Override
		public MessageParams build() {
			return new MessageParams(type, message);
		}
	}
}
