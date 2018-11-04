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

import java.util.ArrayList;
import java.util.List;

import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code Command}.
 *
 * @author Janne Valkealahti
 *
 */
public class Command {

	private String title;
	private String command;
	private List<Object> arguments;

	/**
	 * Instantiates a new command.
	 */
	public Command() {
	}

	/**
	 * Instantiates a new command.
	 *
	 * @param title the title
	 * @param command the command
	 */
	public Command(String title, String command) {
		this(title, command, null);
	}

	/**
	 * Instantiates a new command.
	 *
	 * @param title the title
	 * @param command the command
	 * @param arguments the arguments
	 */
	public Command(String title, String command, List<Object> arguments) {
		super();
		this.title = title;
		this.command = command;
		this.arguments = arguments;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<Object> getArguments() {
		return arguments;
	}

	public void setArguments(List<Object> arguments) {
		this.arguments = arguments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
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
		Command other = (Command) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link Command}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CommandBuilder<P> extends DomainBuilder<Command, P> {

		/**
		 * Sets a title.
		 *
		 * @param title the title
		 * @return the builder for chaining
		 */
		CommandBuilder<P> title(String title);

		/**
		 * Sets a command.
		 *
		 * @param command the command
		 * @return the builder for chaining
		 */
		CommandBuilder<P> command(String command);

		/**
		 * Adds an argument.
		 *
		 * @param argument the argument
		 * @return the builder for chaining
		 */
		CommandBuilder<P> argument(Object argument);

		/**
		 * Sets an arguments.
		 *
		 * @param arguments the arguments
		 * @return the builder for chaining
		 */
		CommandBuilder<P> arguments(List<Object> arguments);
	}

	/**
	 * Gets a builder for {@link Command}
	 *
	 * @return the command builder
	 */
	public static <P> CommandBuilder<P> command() {
		return new InternalCommandBuilder<>(null);
	}

	protected static <P> CommandBuilder<P> command(P parent) {
		return new InternalCommandBuilder<>(parent);
	}

	private static class InternalCommandBuilder<P>
			extends AbstractDomainBuilder<Command, P> implements CommandBuilder<P> {

		private String title;
		private String command;
		private List<Object> arguments;

		InternalCommandBuilder(P parent) {
			super(parent);
		}

		@Override
		public CommandBuilder<P> title(String title) {
			this.title = title;
			return this;
		}

		@Override
		public CommandBuilder<P> command(String command) {
			this.command = command;
			return this;
		}

		@Override
		public CommandBuilder<P> argument(Object argument) {
			if (arguments == null) {
				arguments = new ArrayList<>();
			}
			arguments.add(argument);
			return this;
		}

		@Override
		public CommandBuilder<P> arguments(List<Object> arguments) {
			this.arguments = arguments;
			return this;
		}

		@Override
		public Command build() {
			return new Command(title, command, arguments);
		}
	}
}
