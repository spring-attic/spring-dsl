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

import org.springframework.dsl.domain.Command.CommandBuilder;
import org.springframework.dsl.domain.Range.RangeBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code CodeLens}.
 *
 * @author Janne Valkealahti
 *
 */
public class CodeLens {

    private Range range;
    private Command command;
    private Object data;

	/**
	 * Instantiates a new text edit.
	 */
	public CodeLens() {
	}

	/**
	 * Instantiates a new text edit.
	 *
	 * @param range the range
	 * @param command the command
     * @param data the data
	 */
	public CodeLens(Range range, Command command, Object data) {
		this.range = range;
        this.command = command;
        this.data = data;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		CodeLens other = (CodeLens) obj;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link CodeLens}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface CodeLensBuilder<P> extends DomainBuilder<CodeLens, P> {

		/**
		 * Gets a builder for {@link Range}.
		 *
		 * @return the range builder
		 */
		RangeBuilder<CodeLensBuilder<P>> range();

		/**
		 * Sets a range for a {@code range}. Will take presence of range set from
		 * via @{@link #range()}.
		 *
		 * @return the builder for chaining
		 */
		CodeLensBuilder<P> range(Range range);

		/**
		 * Gets a builder for {@link Command}.
		 *
		 * @return the command builder
		 */
		CommandBuilder<CodeLensBuilder<P>> command();

		/**
		 * Sets a command for a {@code command}. Will take presence of command set from
		 * via @{@link #command()}.
		 *
		 * @return the builder for chaining
		 */
		CodeLensBuilder<P> command(Command command);

		/**
		 * Sets a data.
		 *
		 * @param data the data
		 * @return the builder for chaining
		 */
		CodeLensBuilder<P> data(Object data);
	}

	/**
	 * Gets a builder for {@link CodeLens}.
	 *
	 * @return the text edit builder
	 */
	public static <P> CodeLensBuilder<P> codeLens() {
		return new InternalCodeLensBuilder<>(null);
	}

	protected static <P> CodeLensBuilder<P> codeLens(P parent) {
		return new InternalCodeLensBuilder<>(parent);
	}

	private static class InternalCodeLensBuilder<P>
			extends AbstractDomainBuilder<CodeLens, P> implements CodeLensBuilder<P> {

		Object data;
		RangeBuilder<CodeLensBuilder<P>> rangeBuilder;
        Range range;
        Command command;
		CommandBuilder<CodeLensBuilder<P>> commandBuilder;


		InternalCodeLensBuilder(P parent) {
			super(parent);
		}

		@Override
		public RangeBuilder<CodeLensBuilder<P>> range() {
			this.rangeBuilder = Range.range(this);
			return rangeBuilder;
		}

		@Override
		public CodeLensBuilder<P> range(Range range) {
			this.range = range;
			return this;
		}

		@Override
		public CommandBuilder<CodeLensBuilder<P>> command() {
			this.commandBuilder = Command.command(this);
			return commandBuilder;
		}

		@Override
		public CodeLensBuilder<P> command(Command command) {
			this.command = command;
			return this;
		}

		@Override
		public CodeLensBuilder<P> data(Object data) {
			this.data = data;
			return this;
		}

		@Override
		public CodeLens build() {
			CodeLens codeLens = new CodeLens();
			codeLens.setData(data);
			if (range != null) {
				codeLens.setRange(range);
			} else if (rangeBuilder != null) {
				codeLens.setRange(rangeBuilder.build());
			}
			if (command != null) {
				codeLens.setCommand(command);
			} else if (commandBuilder != null) {
				codeLens.setCommand(commandBuilder.build());
			}
			return codeLens;
		}
	}
}
