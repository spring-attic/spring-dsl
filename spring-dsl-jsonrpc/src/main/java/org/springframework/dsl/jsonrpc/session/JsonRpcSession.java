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
package org.springframework.dsl.jsonrpc.session;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * Main contract for using a server-side session that provides access to session
 * attributes across {@code JSONRPC} requests.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcSession {

	/**
	 * Return a unique session identifier.
	 */
	String getId();

	/**
	 * Return a map that holds session attributes.
	 *
	 * @return the attributes map
	 */
	Map<String, Object> getAttributes();

	/**
	 * Return the session attribute value if present.
	 *
	 * @param name the attribute name
	 * @param <T> the attribute type
	 * @return the attribute value
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	default <T> T getAttribute(String name) {
		return (T) getAttributes().get(name);
	}

	/**
	 * Return the session attribute value or if not present raise an
	 * {@link IllegalArgumentException}.
	 *
	 * @param name the attribute name
	 * @param <T> the attribute type
	 * @return the attribute value
	 */
	default <T> T getRequiredAttribute(String name) {
		T value = getAttribute(name);
		Assert.notNull(value, "Required attribute '" + name + "' is missing.");
		return value;
	}

	/**
	 * Force the creation of a session causing the session id to be sent when
	 * {@link #save()} is called.
	 */
	void start();

	/**
	 * Save the session persisting attributes (e.g. if stored remotely) and also
	 * sending the session id to the client if the session is new.
	 * <p>
	 * Note that a session must be started explicitly via {@link #start()} or
	 * implicitly by adding attributes or otherwise this method has no effect.
	 *
	 * @return {@code Mono} to indicate completion with success or error
	 *         <p>
	 *         Typically this method should be automatically invoked just before the
	 *         response is committed so applications don't have to by default.
	 */
	Mono<Void> save();

	/**
	 * Whether a session with the client has been started explicitly via
	 * {@link #start()} or implicitly by adding session attributes. If "false" then
	 * the session id is not sent to the client and the {@link #save()} method is
	 * essentially a no-op.
	 *
	 * @return if session is started
	 */
	boolean isStarted();

	/**
	 * Return {@code true} if the session expired after {@link #getMaxIdleTime()
	 * maxIdleTime} elapsed.
	 * <p>
	 * Typically expiration checks should be automatically made when a session is
	 * accessed, a new {@code WebSession} instance created if necessary, at the
	 * start of request processing so that applications don't have to worry about
	 * expired session by default.
	 *
	 * @return if session is expired
	 */
	boolean isExpired();

	/**
	 * Invalidate the current session and clear session storage.
	 *
	 * @return completion notification (success or error)
	 */
	Mono<Void> invalidate();

	/**
	 * Return the time when the session was created.
	 */
	Instant getCreationTime();

	/**
	 * Return the last time of session access as a result of user activity such
	 * as an HTTP request. Together with {@link #getMaxIdleTime()
	 * maxIdleTimeInSeconds} this helps to determine when a session is
	 * {@link #isExpired() expired}.
	 */
	Instant getLastAccessTime();

	/**
	 * Configure the max amount of time that may elapse after the
	 * {@link #getLastAccessTime() lastAccessTime} before a session is considered
	 * expired. A negative value indicates the session should not expire.
	 */
	void setMaxIdleTime(Duration maxIdleTime);

	/**
	 * Return the maximum time after the {@link #getLastAccessTime()
	 * lastAccessTime} before a session expires. A negative time indicates the
	 * session doesn't expire.
	 */
	Duration getMaxIdleTime();

	/**
	 * Callback for customizing a given session. Designed for use with a lambda
	 * expression or method reference.
	 */
	@FunctionalInterface
	public interface JsonRpcSessionCustomizer {

		/**
		 * Customize the given session.
		 *
		 * @param jsonRpcSession the session
		 */
		void customize(JsonRpcSession jsonRpcSession);
	}
}
