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

import org.springframework.dsl.domain.Range.RangeBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code Location}.
 *
 * @author Janne Valkealahti
 *
 */
public class Location {

	private String uri;
	private Range range;

	/**
	 * Instantiates a new location.
	 */
	public Location() {
	}

	/**
	 * Instantiates a new location.
	 *
	 * @param uri the uri
	 * @param range the range
	 */
	public Location(String uri, Range range) {
		this.uri = uri;
		this.range = range;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Location other = (Location) obj;
		if (range == null) {
			if (other.range != null) {
				return false;
			}
		} else if (!range.equals(other.range)) {
			return false;
		}
		if (uri == null) {
			if (other.uri != null) {
				return false;
			}
		} else if (!uri.equals(other.uri)) {
			return false;
		}
		return true;
	}

	/**
	 * Builder interface for {@link Location}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface LocationBuilder<P> extends DomainBuilder<Location, P> {

		/**
		 * Sets an uri.
		 *
		 * @return the builder for chaining
		 */
		LocationBuilder<P> uri(String uri);

		/**
		 * Gets a builder for a {@link Range}.
		 *
		 * @return the builder for chaining
		 */
		RangeBuilder<LocationBuilder<P>> range();
	}

	/**
	 * Gets a builder for {@link Location}
	 *
	 * @return the location builder
	 */
	public static <P> LocationBuilder<P> location() {
		return new InternalLocationBuilder<>(null);
	}

	protected static <P> LocationBuilder<P> location(P parent) {
		return new InternalLocationBuilder<>(parent);
	}

	private static class InternalLocationBuilder<P>
		extends AbstractDomainBuilder<Location, P> implements LocationBuilder<P> {

		private String uri;
		private RangeBuilder<LocationBuilder<P>> range;

		InternalLocationBuilder(P parent) {
			super(parent);
		}

		@Override
		public LocationBuilder<P> uri(String uri) {
			this.uri = uri;
			return this;
		}

		@Override
		public RangeBuilder<LocationBuilder<P>> range() {
			this.range = Range.range(this);
			return range;
		}

		@Override
		public Location build() {
			Location location = new Location();
			location.setUri(uri);
			if (range != null) {
				location.setRange(range.build());
			}
			return location;
		}
	}
}
