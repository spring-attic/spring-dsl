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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link JsonDeserializer} for a {@link DefaultJsonRpcRequest}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultJsonRpcRequestJsonDeserializer extends JsonDeserializer<DefaultJsonRpcRequest> {

	@Override
	public DefaultJsonRpcRequest deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec c = p.getCodec();
	    JsonNode node = c.readTree(p);
	    String jsonrpc = node.get("jsonrpc").asText();
	    JsonNode jsonNodeParams = node.get("params");
	    String params = null;
	    if (jsonNodeParams != null) {
		    if (jsonNodeParams.isValueNode()) {
		    	params = jsonNodeParams.asText();
		    } else {
		    	params = jsonNodeParams.toString();
		    }
	    }
	    JsonNode jsonNodeId = node.get("id");
	    String id = jsonNodeId != null ? jsonNodeId.asText() : null;
	    JsonNode jsonNodeMethod = node.get("method");
	    String method = jsonNodeMethod != null ? jsonNodeMethod.asText() : null;
		return new DefaultJsonRpcRequest(jsonrpc, id, method, params);
	}
}
