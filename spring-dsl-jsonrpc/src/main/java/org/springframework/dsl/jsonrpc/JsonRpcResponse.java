/*
 * Copyright 2018 the original author or authors.
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
package org.springframework.dsl.jsonrpc;

/**
 * Base {@code JSONRPC} request message aware of {@code result} and {@code error} fields.
 *
 * @author Janne Valkealahti
 *
 */
public interface JsonRpcResponse extends JsonRpcMessage {

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult();

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public String getError();
}
