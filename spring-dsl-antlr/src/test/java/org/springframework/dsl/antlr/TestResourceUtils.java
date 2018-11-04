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
package org.springframework.dsl.antlr;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public abstract class TestResourceUtils {

	public static String resourceAsString(Class<?> baseClass, String name) throws IOException {
		return StreamUtils.copyToString(TestResourceUtils.qualifiedResource(baseClass, name).getInputStream(),
				Charset.defaultCharset());
	}

	public static ClassPathResource qualifiedResource(Class<?> clazz, String resourceSuffix) {
		return new ClassPathResource(String.format("%s-%s", clazz.getSimpleName(), resourceSuffix), clazz);
	}

}
