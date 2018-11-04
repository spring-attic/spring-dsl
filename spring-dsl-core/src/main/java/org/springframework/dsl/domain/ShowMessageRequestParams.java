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

import org.springframework.dsl.domain.MessageActionItem.MessageActionItemBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code ShowMessageRequestParams}.
 *
 * @author Janne Valkealahti
 *
 */
public class ShowMessageRequestParams extends MessageParams {

	private List<MessageActionItem> actions;

	/**
	 * Instantiates a new show message request params.
	 */
	public ShowMessageRequestParams() {
		this.actions = new ArrayList<>();
	}

	/**
	 * Instantiates a new show message request params.
	 *
	 * @param actions the actions
	 */
	public ShowMessageRequestParams(List<MessageActionItem> actions) {
		this.actions = actions;
	}

	/**
	 * Gets the actions.
	 *
	 * @return the actions
	 */
	public List<MessageActionItem> getActions() {
		return actions;
	}

	/**
	 * Sets the actions.
	 *
	 * @param actions the new actions
	 */
	public void setActions(List<MessageActionItem> actions) {
		this.actions = actions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShowMessageRequestParams other = (ShowMessageRequestParams) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		return true;
	}

	/**
	 * Builder interface for {@link ShowMessageRequestParams}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface ShowMessageRequestParamsBuilder<P> extends DomainBuilder<ShowMessageRequestParams, P> {

		/**
		 * Gets a builder for {@link MessageActionItem}.
		 *
		 * @return the builder for chaining
		 */
		MessageActionItemBuilder<ShowMessageRequestParamsBuilder<P>> action();

		/**
		 * Sets a message.
		 *
		 * @param message the message
		 * @return the builder for chaining
		 */
		ShowMessageRequestParamsBuilder<P> message(String message);

		/**
		 * Sets a type.
		 *
		 * @param type the type
		 * @return the builder for chaining
		 */
		ShowMessageRequestParamsBuilder<P> type(MessageType type);
	}

	/**
	 * Gets a builder for {@link ShowMessageRequestParams}.
	 *
	 * @return the show message request params builder
	 */
	public static <P> ShowMessageRequestParamsBuilder<P> showMessageRequestParams() {
		return new InternalShowMessageRequestParamsBuilder<>(null);
	}

	protected static <P> ShowMessageRequestParamsBuilder<P> showMessageRequestParams(P parent) {
		return new InternalShowMessageRequestParamsBuilder<>(parent);
	}

	private static class InternalShowMessageRequestParamsBuilder<P>
			extends AbstractDomainBuilder<ShowMessageRequestParams, P> implements ShowMessageRequestParamsBuilder<P> {

		private MessageType type;
		private String message;
		private List<MessageActionItemBuilder<ShowMessageRequestParamsBuilder<P>>> actions = new ArrayList<>();

		InternalShowMessageRequestParamsBuilder(P parent) {
			super(parent);
		}

		@Override
		public ShowMessageRequestParamsBuilder<P> message(String message) {
			this.message = message;
			return this;
		}

		@Override
		public ShowMessageRequestParamsBuilder<P> type(MessageType type) {
			this.type = type;
			return this;
		}

		@Override
		public MessageActionItemBuilder<ShowMessageRequestParamsBuilder<P>> action() {
			MessageActionItemBuilder<ShowMessageRequestParamsBuilder<P>> actionBuilder = MessageActionItem.messageActionItem(this);
			this.actions.add(actionBuilder);
			return actionBuilder;
		}

		@Override
		public ShowMessageRequestParams build() {
			ShowMessageRequestParams showMessageRequestParams = new ShowMessageRequestParams();
			showMessageRequestParams.setType(type);
			showMessageRequestParams.setMessage(message);
			if (!actions.isEmpty()) {
				List<MessageActionItem> messageActionItems = new ArrayList<>();
				for (MessageActionItemBuilder<ShowMessageRequestParamsBuilder<P>> d : actions) {
					messageActionItems.add(d.build());
				}
				showMessageRequestParams.setActions(messageActionItems);
			}
			return showMessageRequestParams;
		}
	}
}
