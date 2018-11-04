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
package org.springframework.dsl.jsonrpc.docs;

import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ControllerDocs {

// tag::snippet1[]
	@JsonRpcController
	private static class Test1JsonRpcController {

		@JsonRpcRequestMapping(method = "hi")
		@JsonRpcResponseResult
		public String hi() {
			return "hi";
		}
	}
// end::snippet1[]

// tag::snippet2[]
	@JsonRpcController
	@JsonRpcRequestMapping(method = "prefix/")
	private static class Test2JsonRpcController {

		@JsonRpcRequestMapping(method = "hi")
		@JsonRpcResponseResult
		public String hi() {
			return "hi";
		}
	}
// end::snippet2[]

// tag::snippet3[]
	@JsonRpcController
	private static class Test3JsonRpcController {

		@JsonRpcRequestMapping(method = "himono")
		@JsonRpcResponseResult
		public Mono<String> himono() {
			return Mono.just("himono");
		}

		@JsonRpcRequestMapping(method = "hiflux")
		@JsonRpcResponseResult
		public Flux<String> hiflux() {
			return Flux.just("hiflux");
		}
	}
// end::snippet3[]

// tag::snippet4[]
	@JsonRpcController
	private static class Test4JsonRpcController {

		@JsonRpcRequestMapping(method = "himono")
		@JsonRpcNotification
		public Mono<Void> himono() {
			return Mono.empty();
		}
	}
// end::snippet4[]

// tag::snippet5[]
	@JsonRpcController
	private static class Test5JsonRpcController {

		@JsonRpcRequestMapping(method = "hiflux")
		@JsonRpcNotification(method = "hinotifications")
		public Flux<String> hiflux() {
			return Flux.just("hiflux");
		}
	}
// end::snippet5[]

// tag::snippet6[]
	@JsonRpcController
	private static class Test6JsonRpcController {

		@JsonRpcRequestMapping(method = "session")
		@JsonRpcResponseResult
		public Mono<String> session(JsonRpcSession session) {
			return Mono.just(session.getId());
		}
	}
// end::snippet6[]

}
