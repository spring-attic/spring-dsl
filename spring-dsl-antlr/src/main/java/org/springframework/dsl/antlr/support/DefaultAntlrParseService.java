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
package org.springframework.dsl.antlr.support;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.dsl.antlr.AntlrParseResult;
import org.springframework.dsl.antlr.AntlrParseService;
import org.springframework.dsl.document.Document;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

/**
 * Default implementation of a {@link AntlrParseService}.
 *
 * @author Janne Valkealahti
 *
 * @param <T> the type of a result in {@link AntlrParseResult}
 */
public class DefaultAntlrParseService<T> implements AntlrParseService<T> {

	private final Cache<CacheKey, Signal<? extends AntlrParseResult<T>>> cache;

	/**
	 * Instantiates a new default antlr parse service.
	 */
	public DefaultAntlrParseService() {
		this.cache = Caffeine.newBuilder().build();
	}

	@Override
	public Mono<AntlrParseResult<T>> parse(Document document,
			Function<Document, ? extends Mono<? extends AntlrParseResult<T>>> function) {
		return CacheMono
				.lookup(reader(cache), new CacheKey(document))
				.onCacheMissResume(Mono.defer(() -> function.apply(document)))
				.andWriteWith(writer(cache));
	}

	private static <K, V> Function<K, Mono<Signal<? extends AntlrParseResult<V>>>> reader(
			Cache<K, ? extends Signal<? extends AntlrParseResult<V>>> cache) {
		return key -> Mono.justOrEmpty(cache.getIfPresent(key));
	}

	private static <K, V> BiFunction<K, Signal<? extends AntlrParseResult<V>>, Mono<Void>> writer(
			Cache<K, ? super Signal<? extends AntlrParseResult<V>>> cache) {
		return (key, value) -> Mono.fromRunnable(() -> cache.put(key, value));
	}

	private static class CacheKey {
		private String uri;
		private int version;

		public CacheKey(Document document) {
			this.uri = document.uri();
			this.version = document.getVersion();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			result = prime * result + version;
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
			CacheKey other = (CacheKey) obj;
			if (uri == null) {
				if (other.uri != null)
					return false;
			} else if (!uri.equals(other.uri))
				return false;
			if (version != other.version)
				return false;
			return true;
		}


	}

}
