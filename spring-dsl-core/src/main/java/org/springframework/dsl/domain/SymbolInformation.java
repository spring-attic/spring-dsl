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

import org.springframework.dsl.domain.Location.LocationBuilder;
import org.springframework.dsl.support.AbstractDomainBuilder;
import org.springframework.dsl.support.DomainBuilder;

/**
 * {@code LSP} domain object for a specification {@code SymbolInformation}.
 *
 * @author Janne Valkealahti
 *
 */
public class SymbolInformation {

	private String name;
	private SymbolKind kind;
	private Boolean deprecated;
	private Location location;
	private String containerName;

	public SymbolInformation() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SymbolKind getKind() {
		return kind;
	}

	public void setKind(SymbolKind kind) {
		this.kind = kind;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerName == null) ? 0 : containerName.hashCode());
		result = prime * result + ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SymbolInformation other = (SymbolInformation) obj;
		if (containerName == null) {
			if (other.containerName != null) {
				return false;
			}
		} else if (!containerName.equals(other.containerName)) {
			return false;
		}
		if (deprecated == null) {
			if (other.deprecated != null) {
				return false;
			}
		} else if (!deprecated.equals(other.deprecated)) {
			return false;
		}
		if (kind != other.kind) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * Builder interface for {@link SymbolInformation}.
	 *
	 * @param <P> the parent builder type
	 */
	public interface SymbolInformationBuilder<P> extends DomainBuilder<SymbolInformation, P> {

		/**
		 * Sets a name.
		 *
		 * @param name the name
		 * @return the builder for chaining
		 */
		SymbolInformationBuilder<P> name(String name);

		/**
		 * Sets a kind.
		 *
		 * @param kind the kind
		 * @return the builder for chaining
		 */
		SymbolInformationBuilder<P> kind(SymbolKind kind);

		/**
		 * Sets a deprecated.
		 *
		 * @param deprecated the deprecated
		 * @return the builder for chaining
		 */
		SymbolInformationBuilder<P> deprecated(Boolean deprecated);

		/**
		 * Gets a builder for a {@link Location} for {@code location}.
		 *
		 * @return the builder for chaining
		 */
		LocationBuilder<SymbolInformationBuilder<P>> location();

		/**
		 * Sets a containerName.
		 *
		 * @param containerName the containerName
		 * @return the builder for chaining
		 */
		SymbolInformationBuilder<P> containerName(String containerName);
	}

	/**
	 * Gets a builder for {@link SymbolInformation}
	 *
	 * @return the symbol information builder
	 */
	public static <P> SymbolInformationBuilder<P> symbolInformation() {
		return new InternalSymbolInformationBuilder<>(null);
	}

	protected static <P> SymbolInformationBuilder<P> symbolInformation(P parent) {
		return new InternalSymbolInformationBuilder<>(parent);
	}

	private static class InternalSymbolInformationBuilder<P>
		extends AbstractDomainBuilder<SymbolInformation, P> implements SymbolInformationBuilder<P> {

		private String name;
		private SymbolKind kind;
		private Boolean deprecated;
		private LocationBuilder<SymbolInformationBuilder<P>> locationBuilder;
		private String containerName;

		InternalSymbolInformationBuilder(P parent) {
			super(parent);
		}

		@Override
		public SymbolInformationBuilder<P> name(String name) {
			this.name = name;
			return this;
		}

		@Override
		public SymbolInformationBuilder<P> kind(SymbolKind kind) {
			this.kind = kind;
			return this;
		}

		@Override
		public SymbolInformationBuilder<P> deprecated(Boolean deprecated) {
			this.deprecated = deprecated;
			return this;
		}

		@Override
		public LocationBuilder<SymbolInformationBuilder<P>> location() {
			this.locationBuilder = Location.location(this);
			return locationBuilder;
		}

		@Override
		public SymbolInformationBuilder<P> containerName(String containerName) {
			this.containerName = containerName;
			return this;
		}

		@Override
		public SymbolInformation build() {
			SymbolInformation symbolInformation = new SymbolInformation();
			symbolInformation.setName(name);
			symbolInformation.setKind(kind);
			symbolInformation.setDeprecated(deprecated);
			if (locationBuilder != null) {
				symbolInformation.setLocation(locationBuilder.build());
			}
			symbolInformation.setContainerName(containerName);
			return symbolInformation;
		}
	}
}
