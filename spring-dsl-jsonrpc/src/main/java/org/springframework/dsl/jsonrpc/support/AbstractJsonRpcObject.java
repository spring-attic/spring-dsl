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

import org.springframework.dsl.jsonrpc.JsonRpcMessage;

/**
 * Base {@code JSONRPC Object} sharing fields with requests and responses.
 * Essentially fields {@code jsonrcp} and {@code id} exists on both requests and
 * responses where in a response {@code id} matches one from a request.
 * <p>
 * In a context of a {@code JSONRPC} message this can be as is as required, or
 * {@code JSONRPC} message can be a batch of these messages.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractJsonRpcObject implements JsonRpcMessage {

	public static String DEFAULT_JSONRPC = "2.0";
	private String jsonrpc;
	private String id;

	/**
	 * Instantiates a new abstract json rpc object.
	 */
	public AbstractJsonRpcObject() {
		this(null);
	}

	/**
	 * Instantiates a new abstract json rpc object.
	 *
	 * @param id the id
	 */
	public AbstractJsonRpcObject(String id) {
		this(null, id);
	}

	/**
	 * Instantiates a new abstract json rpc object.
	 *
	 * @param jsonrpc the jsonrpc
	 * @param id the id
	 */
	public AbstractJsonRpcObject(String jsonrpc, String id) {
		this.jsonrpc = jsonrpc != null ? jsonrpc : DEFAULT_JSONRPC;
		this.id = id;
	}

	/**
	 * Gets the jsonrpc field.
	 *
	 * @return the jsonrpc
	 */
	@Override
	public final String getJsonrpc() {
		return jsonrpc;
	}

	/**
	 * Sets the jsonrpc field.
	 *
	 * @param jsonrpc the new jsonrpc
	 */
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	/**
	 * Gets the id field.
	 *
	 * @return the id
	 */
	@Override
	public final String getId() {
		return id;
	}

	/**
	 * Sets the id field.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AbstractJsonRpcObject [jsonrpc=" + jsonrpc + ", id=" + id + "]";
	}
}
