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

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import reactor.core.publisher.Mono;

/**
 * Simple in-memory map based storage for a {@link JsonRpcSessionStore}.
 *
 * @author Janne Valkealahti
 *
 */
public class InMemoryJsonRpcSessionStore implements JsonRpcSessionStore {

	private static final Duration EXPIRATION_CHECK_PERIOD = Duration.ofSeconds(60);
	private final ConcurrentMap<String, InMemoryJsonRpcSession> sessions = new ConcurrentHashMap<>();
	private Clock clock = Clock.system(ZoneId.of("GMT"));
	private volatile Instant nextExpirationCheckTime = Instant.now(this.clock).plus(EXPIRATION_CHECK_PERIOD);
	private final ReentrantLock expirationCheckLock = new ReentrantLock();

	@Override
	public Mono<JsonRpcSession> createSession(String sessionId) {
		return Mono.fromSupplier(() -> new InMemoryJsonRpcSession(sessionId));
	}

	@Override
	public Mono<JsonRpcSession> retrieveSession(String sessionId) {
		Instant currentTime = Instant.now(this.clock);
		if (!this.sessions.isEmpty() && !currentTime.isBefore(this.nextExpirationCheckTime)) {
			checkExpiredSessions(currentTime);
		}

		InMemoryJsonRpcSession session = this.sessions.get(sessionId);
		if (session == null) {
			return Mono.empty();
		} else if (session.isExpired(currentTime)) {
			this.sessions.remove(sessionId);
			return Mono.empty();
		} else {
			session.updateLastAccessTime(currentTime);
			return Mono.just(session);
		}
	}

	@Override
	public Mono<Void> removeSession(String sessionId) {
		this.sessions.remove(sessionId);
		return Mono.empty();
	}

	@Override
	public Mono<JsonRpcSession> updateLastAccessTime(JsonRpcSession webSession) {
		return Mono.just(webSession);
	}

	private Clock getClock() {
		return this.clock;
	}

	private void checkExpiredSessions(Instant currentTime) {
		if (this.expirationCheckLock.tryLock()) {
			try {
				Iterator<InMemoryJsonRpcSession> iterator = this.sessions.values().iterator();
				while (iterator.hasNext()) {
					InMemoryJsonRpcSession session = iterator.next();
					if (session.isExpired(currentTime)) {
						iterator.remove();
						session.invalidate();
					}
				}
			}
			finally {
				this.nextExpirationCheckTime = currentTime.plus(EXPIRATION_CHECK_PERIOD);
				this.expirationCheckLock.unlock();
			}
		}
	}

	private class InMemoryJsonRpcSession implements JsonRpcSession {

		private String id;

		private final Map<String, Object> attributes = new ConcurrentHashMap<>();

		private final Instant creationTime;

		private volatile Instant lastAccessTime;

		private volatile Duration maxIdleTime = Duration.ofMinutes(30);

		private final AtomicReference<State> state = new AtomicReference<>(State.NEW);

		public InMemoryJsonRpcSession(String id) {
			this.id = id;
			this.creationTime = Instant.now(getClock());
			this.lastAccessTime = this.creationTime;
		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public Map<String, Object> getAttributes() {
			return this.attributes;
		}

		@Override
		public Instant getCreationTime() {
			return this.creationTime;
		}

		@Override
		public Instant getLastAccessTime() {
			return this.lastAccessTime;
		}

		@Override
		public void setMaxIdleTime(Duration maxIdleTime) {
			this.maxIdleTime = maxIdleTime;
		}

		@Override
		public Duration getMaxIdleTime() {
			return this.maxIdleTime;
		}

		@Override
		public void start() {
			this.state.compareAndSet(State.NEW, State.STARTED);
		}

		@Override
		public boolean isStarted() {
			return this.state.get().equals(State.STARTED) || !getAttributes().isEmpty();
		}

		@Override
		public Mono<Void> invalidate() {
			this.state.set(State.EXPIRED);
			getAttributes().clear();
			InMemoryJsonRpcSessionStore.this.sessions.remove(this.id);
			return Mono.empty();
		}

		@Override
		public Mono<Void> save() {
			if (!getAttributes().isEmpty()) {
				this.state.compareAndSet(State.NEW, State.STARTED);
			}
			InMemoryJsonRpcSessionStore.this.sessions.put(this.getId(), this);
			return Mono.empty();
		}

		@Override
		public boolean isExpired() {
			return isExpired(Instant.now(getClock()));
		}

		private boolean isExpired(Instant currentTime) {
			if (this.state.get().equals(State.EXPIRED)) {
				return true;
			}
			if (checkExpired(currentTime)) {
				this.state.set(State.EXPIRED);
				return true;
			}
			return false;
		}

		private boolean checkExpired(Instant currentTime) {
			return isStarted() && !this.maxIdleTime.isNegative() &&
					currentTime.minus(this.maxIdleTime).isAfter(this.lastAccessTime);
		}

		private void updateLastAccessTime(Instant currentTime) {
			this.lastAccessTime = currentTime;
		}
	}

	private enum State { NEW, STARTED, EXPIRED }
}
