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
package org.springframework.dsl.jsonrpc.support;

import org.springframework.dsl.jsonrpc.JsonRpcResponse;

/**
 * Represents a {@code JSONRPC Response Object}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultJsonRpcResponse extends AbstractJsonRpcObject implements JsonRpcResponse {

	private String result;
	private String error;

	/**
	 * Instantiates a new json rpc response.
	 */
	public DefaultJsonRpcResponse() {
		this(null);
	}

	/**
	 * Instantiates a new json rpc response.
	 *
	 * @param id the id
	 */
	public DefaultJsonRpcResponse(String id) {
		this(id, null, null);
	}

	/**
	 * Instantiates a new json rpc response.
	 *
	 * @param id the id
	 * @param result the result
	 * @param error the error
	 */
	public DefaultJsonRpcResponse(String id, String result, String error) {
		this(null, id, result, error);
	}

	/**
	 * Instantiates a new json rpc response.
	 *
	 * @param jsonrpc the jsonrpc
	 * @param id the id
	 * @param result the result
	 * @param error the error
	 */
	public DefaultJsonRpcResponse(String jsonrpc, String id, String result, String error) {
		super(jsonrpc, id);
		this.result = result;
		this.error = error;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	@Override
	public String getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	@Override
	public String getError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "DefaultJsonRpcResponse [result=" + result + ", error=" + error + ", toString()=" + super.toString()
				+ "]";
	}
}
