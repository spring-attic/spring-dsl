/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.service;

import java.util.Map;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.support.DefaultDslContextBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Interface which is mostly used to pass dsl related information into
 * various classes and other intefaces.
 *
 * @author Janne Valkealahti
 *
 */
public interface DslContext {

    /**
     * Gets a document for this context.
     *
     * @return the document
     */
    Document getDocument();

    /**
     * Gets an attributes for this context.
     *
     * @return the attributes
     */
    Map<String, Object> getAttributes();

	/**
	 * Return the context attribute value if present.
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
	 * Return the context attribute value or if not present raise an
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
     * Gets a builder for {@link DslContext}.
     *
     * @return the builder
     */
    static Builder builder() {
        return new DefaultDslContextBuilder();
    }

    interface Builder {

        /**
         * Sets a {@link Document} instance for this builder.
         *
         * @param document the document
         * @return builder for chaining
         */
        Builder document(Document document);

        /**
         * Sets an attributes for this builder. This will effectively
         * clear and overrides existing attributes.
         *
         * @param attributes the attributes
         * @return builder for chaining
         */
        Builder attributes(Map<String, Object> attributes);

        /**
         * Set an attribute.
         *
         * @param key the attribute key
         * @param value the attribute valur
         * @return builder for chaining
         */
        Builder attribute(String key, Object value);

        /**
         * Builds a dsl context.
         *
         * @return the built dsl context
         */
        DslContext build();
    }
}
