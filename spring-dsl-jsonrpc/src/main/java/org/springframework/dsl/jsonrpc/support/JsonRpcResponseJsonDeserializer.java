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

import java.io.IOException;

import org.springframework.dsl.jsonrpc.JsonRpcResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link JsonDeserializer} for a {@link JsonRpcResponse}.
 *
 * @author Janne Valkealahti
 *
 */
public class JsonRpcResponseJsonDeserializer extends JsonDeserializer<JsonRpcResponse> {

	@Override
	public JsonRpcResponse deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		DefaultJsonRpcResponse response = new DefaultJsonRpcResponse();
		JsonNode node = p.getCodec().readTree(p);
		JsonNode jsonrpcNode = node.get("jsonrpc");
		response.setJsonrpc(jsonrpcNode.asText());
		JsonNode idNode = node.get("id");
		if (idNode != null) {
			response.setId(idNode.asText());
		}
		JsonNode resultsNode = node.get("result");
		if (resultsNode != null) {
			if (resultsNode.isValueNode()) {
				response.setResult(resultsNode.asText());
			} else {
				response.setResult(resultsNode.toString());
			}
		}
		JsonNode errorNode = node.get("error");
		if (errorNode != null) {
			response.setError(errorNode.asText());
		}
		return response;
	}
}
