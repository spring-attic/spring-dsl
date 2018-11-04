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
package demo.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * Configuration used is samples where we need to add a custom filter order to
 * redirect from root to index.html. This is needed until
 * https://github.com/spring-projects/spring-boot/issues/9785 is fixed.
 *
 * @author Janne Valkealahti
 *
 */
@Configuration
public class SampleRedirectConfiguration {

	@Component
	public class CustomWebFilter implements WebFilter {
		@Override
		public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
			if (exchange.getRequest().getURI().getPath().equals("/")) {
				return chain.filter(
						exchange
							.mutate()
							.request(exchange.getRequest().mutate().path("/index.html").build())
							.build());
			}
			return chain.filter(exchange);
		}
	}
}
