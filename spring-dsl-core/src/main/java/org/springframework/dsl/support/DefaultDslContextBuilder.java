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
package org.springframework.dsl.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.service.DslContext;
import org.springframework.dsl.service.DslContext.Builder;

/**
 * Default implementation of a {@link DslContext.Builder}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultDslContextBuilder implements DslContext.Builder {

    private Document document;
    private Map<String, Object> attributes = new HashMap<>();

    @Override
    public DslContext.Builder document(Document document) {
        this.document = document;
        return this;
    }

    @Override
    public Builder attributes(Map<String, Object> attributes) {
        this.attributes = attributes != null ? attributes : new HashMap<>();
        return this;
    }

    @Override
    public Builder attribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    @Override
    public DslContext build() {
        return new DefaultDslContext(document, attributes);
    }

    private static class DefaultDslContext implements DslContext {

        private final Document document;
        private final Map<String, Object> attributes;

        DefaultDslContext(Document document, Map<String, Object> attributes) {
            this.document = document;
            this.attributes = attributes;
        }

        @Override
        public Document getDocument() {
            return document;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }
    }
}
