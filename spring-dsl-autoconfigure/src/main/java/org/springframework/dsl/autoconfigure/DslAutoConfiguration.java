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
package org.springframework.dsl.autoconfigure;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dsl.service.DefaultDslServiceRegistry;
import org.springframework.dsl.service.DslServiceRegistry;

/**
 * {@link EnableAutoConfiguration Auto-configuration} integrating into {@code DSL} features.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
@ConditionalOnClass(DslServiceRegistry.class)
public class DslAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(DslServiceRegistry.class)
	public DslServiceRegistry dslServiceRegistry() {
		return new DefaultDslServiceRegistry();
	}
}
