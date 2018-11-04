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
package org.springframework.dsl.lsp.client;

import java.util.function.BiFunction;

import org.reactivestreams.Processor;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * A non-blocking, reactive client for performing {@code LSP} related
 * {@code JSONRCP} {@code requests} and {@code notification} with Reactive
 * Streams back pressure.
 *
 * @author Janne Valkealahti
 *
 */
public interface LspClient {

	/**
	 * Starts the lsp client. Calling this method on an already started client has
	 * no effect. Some client implementations may discard this method altogether if
	 * there is nothing to start.
	 */
	void start();

	/**
	 * Stops the lsp client. Calling this method on an already stopped client has no
	 * effect. Some client implementations may discard this method altogether if
	 * there is nothing to stop.
	 */
	void stop();

	/**
	 * Prepare a {@code JSONRCP} request.
	 *
	 * @return a {@link RequestSpec} for specifying the request
	 */
	RequestSpec request();

	/**
	 * Prepare a {@code JSONRCP} notification.
	 *
	 * @return a {@link NotificationSpec} for specifying the notification
	 */
	NotificationSpec notification();

	/**
	 * Gets a builder for instances of a {@link LspClient}s.
	 *
	 * @return the builder for building lsp clients
	 */
	static Builder builder() {
		return new DefaultLspClientBuilder();
	}

	/**
	 * Interface for building {@link LspClient}.
	 */
	public interface Builder {

		/**
		 * Sets a host address where client should connect to.
		 *
		 * @param host the host
		 * @return the builder
		 */
		Builder host(String host);

		/**
		 * Sets a port where client should connect to.
		 *
		 * @param port the port
		 * @return the builder
		 */
		Builder port(Integer port);

		Builder function(BiFunction<NettyInbound, NettyOutbound, Mono<Void>> function);
		Builder processor(Processor<ByteBuf, LspClientResponse> processor);


		/**
		 * Builds the {@link LspClient}.
		 *
		 * @return the lsp client
		 */
		LspClient build();
	}

	/**
	 * Defines a contract for a {@code JSONRPC request}.
	 */
	public interface RequestSpec {

		/**
		 * Sets the message id.
		 *
		 * @param id the id
		 * @return the request spec
		 */
		RequestSpec id(String id);

		/**
		 * Sets the message method.
		 *
		 * @param method the method
		 * @return the request spec
		 */
		RequestSpec method(String method);

		/**
		 * Sets the message params.
		 *
		 * @param params the params
		 * @return the request spec
		 */
		RequestSpec params(Object params);

		/**
		 * Gets the exchange.
		 * <p>
		 * Calling this method doesn't cause any exchange to happen until returned
		 * {@link Mono} is consumed.
		 *
		 * @return the mono of a response
		 */
		Mono<LspClientResponse> exchange();
	}

	/**
	 * Defines a contract for a {@code JSONRPC notification}.
	 */
	interface NotificationSpec {

		/**
		 * Sets the message method.
		 *
		 * @param method the method
		 * @return the request spec
		 */
		NotificationSpec method(String method);

		/**
		 * Sets the message params.
		 *
		 * @param params the params
		 * @return the request spec
		 */
		NotificationSpec params(Object params);

		/**
		 * Gets the exchange.
		 * <p>
		 * Calling this method doesn't cause any exchange to happen until returned
		 * {@link Mono} is consumed.
		 *
		 * @return the mono of a completion for a notification
		 */
		Mono<Void> exchange();
	}
}
