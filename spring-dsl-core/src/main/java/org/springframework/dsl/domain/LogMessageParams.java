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
 * {@code LSP} domain object for a specification {@code LogMessageParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class LogMessageParams {

	private MessageType type;
	private String message;

	/**
	 * Instantiates a new log message params.
	 */
	public LogMessageParams() {
	}

	/**
	 * Instantiates a new log message params.
	 *
	 * @param type the type
	 * @param message the message
	 */
	public LogMessageParams(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * Get {@link LogMessageParams} from a {@code message} with a
	 * {@link MessageType}}.
	 *
	 * @param type    the type
	 * @param message the message
	 * @return the log message params
	 */
	public static LogMessageParams from(MessageType type, String message) {
		return new LogMessageParams(type, message);
	}

	/**
	 * Get {@link LogMessageParams} from a {@code message} with a
	 * {@link MessageType#Log}}
	 *
	 * @param message the message
	 * @return the log message params
	 */
	public static LogMessageParams from(String message) {
		return new LogMessageParams(MessageType.Log, message);
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

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
		LogMessageParams other = (LogMessageParams) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LogMessageParams [type=" + type + ", message=" + message + "]";
	}

	/**
	 * Builder interface for {@link LogMessageParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface LogMessageParamsBuilder<P> extends DomainBuilder<LogMessageParams, P>{

		/**
		 * Sets a type.
		 *
		 * @param type the type
		 * @return the builder for chaining
		 */
		LogMessageParamsBuilder<P> type(MessageType type);

		/**
		 * Sets a message.
		 *
		 * @param message the message
		 * @return the builder for chaining
		 */
		LogMessageParamsBuilder<P> message(String message);
	}

	/**
	 * Gets a builder for {@link LogMessageParams}
	 *
	 * @return the LogMessageParams builder
	 */
	public static <P> LogMessageParamsBuilder<P> logMessageParams() {
		return new InternalLogMessageParamsBuilder<>(null);
	}

	protected static <P> LogMessageParamsBuilder<P> logMessageParams(P parent) {
		return new InternalLogMessageParamsBuilder<>(parent);
	}

	private static class InternalLogMessageParamsBuilder<P> extends AbstractDomainBuilder<LogMessageParams, P> implements LogMessageParamsBuilder<P>{

		private MessageType type;
		private String message;

		InternalLogMessageParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public LogMessageParamsBuilder<P> type(MessageType type) {
			this.type = type;
			return this;
		}

		@Override
		public LogMessageParamsBuilder<P> message(String message) {
			this.message = message;
			return this;
		}

		@Override
		public LogMessageParams build() {
			return new LogMessageParams(type, message);
		}
	}
}
