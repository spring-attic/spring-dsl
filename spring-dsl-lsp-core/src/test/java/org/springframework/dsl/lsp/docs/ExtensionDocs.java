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
package org.springframework.dsl.lsp.docs;

import org.springframework.dsl.jsonrpc.annotation.JsonRpcController;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcNotification;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestMapping;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcRequestParams;
import org.springframework.dsl.jsonrpc.annotation.JsonRpcResponseResult;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession;
import org.springframework.dsl.lsp.client.LspClient;

public class ExtensionDocs {

// tag::snippet1[]
	@JsonRpcController
	@JsonRpcRequestMapping(method = "myextension1/")
	public class MyExtensionController1 {

		@JsonRpcRequestMapping(method = "mynotification")
		@JsonRpcNotification
		public void mynotification() {
		}

		@JsonRpcRequestMapping(method = "mymethod")
		@JsonRpcResponseResult
		public String mymethod() {
			return "hi";
		}
	}
// end::snippet1[]

// tag::snippet2[]
	@JsonRpcController
	@JsonRpcRequestMapping(method = "myextension2/")
	public class MyExtensionController2 {

		@JsonRpcRequestMapping(method = "mymethod1")
		@JsonRpcResponseResult
		public String mymethod1(LspClient lspClient) {
			return "hi";
		}

		@JsonRpcRequestMapping(method = "mymethod2")
		@JsonRpcResponseResult
		public String mymethod2(JsonRpcSession session) {
			return "hi";
		}

		@JsonRpcRequestMapping(method = "mymethod3")
		@JsonRpcResponseResult
		public MyPojo mymethod3(@JsonRpcRequestParams MyPojo pojo) {
			return new MyPojo(pojo.getMessage());
		}
	}
// end::snippet2[]

// tag::snippet3[]
	public class MyPojo {
		private String message;
		
		public MyPojo(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
	}
// end::snippet3[]
}
