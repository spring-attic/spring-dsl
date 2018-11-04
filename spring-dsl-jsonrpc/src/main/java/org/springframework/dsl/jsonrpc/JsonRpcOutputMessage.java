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
package org.springframework.dsl.jsonrpc;

import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

import reactor.core.publisher.Mono;

/**
 * A reactive JSONRPC output message that accepts output as a {@link Publisher}.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcOutputMessage {

	/**
	 * Return a {@link DataBufferFactory} that can be used to create the body.
	 *
	 * @return the data buffer factory
	 */
	DataBufferFactory bufferFactory();

	/**
	 * Use the given {@link Publisher} to write the body of the message to the
	 * underlying JSONRPC layer.
	 *
	 * @param body the body content publisher
	 * @return a {@link Mono} that indicates completion or error
	 */
	Mono<Void> writeWith(Publisher<? extends DataBuffer> body);

	/**
	 * Use the given {@link Publisher} of {@code Publishers} to write the body of
	 * the HttpOutputMessage to the underlying JSONRPC layer, flushing after each
	 * {@code Publisher<DataBuffer>}.
	 *
	 * @param body the body content publisher
	 * @return a {@link Mono} that indicates completion or error
	 */
	Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body);

	/**
	 * Indicate that message handling is complete, allowing for any cleanup or
	 * end-of-processing tasks to be performed.
	 * <p>
	 * This method should be automatically invoked at the end of message processing
	 * so typically applications should not have to invoke it. If invoked multiple
	 * times it should have no side effects.
	 *
	 * @return a {@link Mono} that indicates completion or error
	 */
	Mono<Void> setComplete();

	/**
	 * Checks if message has been committed.
	 *
	 * @return true, if message has been committed
	 */
	boolean isCommitted();

	/**
	 * Register an action to apply just before the HttpOutputMessage is committed.
	 *
	 * @param action the action to apply
	 */
	void beforeCommit(Supplier<? extends Mono<Void>> action);
}
