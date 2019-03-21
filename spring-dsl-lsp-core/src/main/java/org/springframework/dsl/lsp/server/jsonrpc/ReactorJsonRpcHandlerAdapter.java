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
package org.springframework.dsl.lsp.server.jsonrpc;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.dsl.DslException;
import org.springframework.dsl.jsonrpc.JsonRpcInputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.JsonRpcRequest;
import org.springframework.dsl.jsonrpc.JsonRpcResponse;
import org.springframework.dsl.jsonrpc.session.JsonRpcSession.JsonRpcSessionCustomizer;
import org.springframework.dsl.lsp.client.LspClientResponse;
import org.springframework.dsl.lsp.client.NettyBoundedLspClient;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.NettyPipeline;

/**
 * Adapter to hook into netty's channels to dispatch messages into {@link RpcHandler}.
 *
 * @author Janne Valkealahti
 *
 */
public class ReactorJsonRpcHandlerAdapter implements BiFunction<NettyInbound, NettyOutbound, Mono<Void>> {

	private static final Logger log = LoggerFactory.getLogger(ReactorJsonRpcHandlerAdapter.class);
	private final RpcHandler rpcHandler;
	private final ObjectMapper objectMapper;
	private final Function<String, JsonRpcRequest> requestDecoder;
	private final Function<String, JsonRpcResponse> responseDecoder;

	/**
	 * Instantiates a new reactor json rpc handler adapter.
	 *
	 * @param rpcHandler the rpc handler
	 * @param objectMapper the object mapper
	 */
	public ReactorJsonRpcHandlerAdapter(RpcHandler rpcHandler, ObjectMapper objectMapper) {
		Assert.notNull(rpcHandler, "RpcHandler must be set");
		Assert.notNull(objectMapper, "ObjectMapper must be set");
		this.rpcHandler = rpcHandler;
		this.objectMapper = objectMapper;
		this.requestDecoder = s -> {
			try {
				return objectMapper.readValue(s, JsonRpcRequest.class);
			} catch (Exception e) {
				throw new DslException("Unable to convert json to rpc request", e);
			}
		};
		this.responseDecoder = s -> {
			try {
				return objectMapper.readValue(s, JsonRpcResponse.class);
			} catch (Exception e) {
				throw new DslException("Unable to convert json to rpc response", e);
			}
		};
	}

	@Override
	public Mono<Void> apply(NettyInbound in, NettyOutbound out) {
		NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(out.alloc());
		Map<String, Disposable> disposables = new HashMap<>();

		in.withConnection(c -> {
			c.addHandlerLast(new LspJsonRpcDecoder());
		});
		out.withConnection(c -> {
			c.addHandlerLast(new LspJsonRpcEncoder());
		});

		// we can only have one subscriber to NettyInbound, so need to dispatch
		// relevant responses to client.
		NettyBoundedLspClient lspClient = new NettyBoundedLspClient(out, objectMapper);
		JsonRpcSessionCustomizer customizer = session -> session.getAttributes().put("lspClient", lspClient);

		Flux<String> shared = in.receiveObject()
			.ofType(String.class)
			.share();

		shared
			.map(responseDecoder)
			.filter(response -> response.getResult() != null || response.getError() != null)
			.subscribe(bb -> {
				lspClient.getResponses().onNext(LspClientResponse.create().response(bb).build());
			});

		shared
			.map(requestDecoder)
			.filter(request -> request.getMethod() != null)
			.subscribe(request -> {
				log.info("Receive request {}", request);

				JsonRpcInputMessage inputMessage = new JsonRpcInputMessage() {

					@Override
					public Mono<String> getJsonrpc() {
						return Mono.justOrEmpty(request.getJsonrpc());
					}

					@Override
					public Mono<String> getId() {
						return Mono.justOrEmpty(request.getId());
					}

					@Override
					public Mono<String> getMethod() {
						return Mono.justOrEmpty(request.getMethod());
					}

					@Override
					public Mono<String> getParams() {
						return Mono.justOrEmpty(request.getParams().toString());
					}

					@Override
					public Mono<String> getSessionId() {
						return Mono.create(sink -> {
							in.withConnection(c -> sink.success(c.channel().id().asLongText()));
						});
					}
				};

				JsonRpcOutputMessage adaptedResponse = new ReactorJsonRpcOutputMessage(out, bufferFactory);

				if (ObjectUtils.nullSafeEquals("$/cancelRequest", request.getMethod())) {

					if (request.getId() != null) {
						in.withConnection(c -> {
							String disposableId = c.channel().id().asLongText() + request.getId();
							if (disposableId != null) {
								Disposable disposable = disposables.remove(disposableId);
								disposable.dispose();
								String error = "{\"jsonrpc\":\"2.0\", \"id\":" + request.getId() + ", \"error\":{\"code\":-32800, \"message\": \"cancel\"}}";
								DataBuffer buffer = bufferFactory.wrap(error.getBytes(Charset.defaultCharset()));
								Flux<DataBuffer> body = Flux.just(buffer);
								adaptedResponse.writeWith(body).subscribe();
								adaptedResponse.setComplete().subscribe();
							}
						});
					}
					return;
				}

				Disposable disposable = rpcHandler.handle(inputMessage, adaptedResponse, customizer)
						.doOnError(ex -> {
							log.error("Handling completed with error", ex);

							String error = "{\"jsonrpc\":\"2.0\", \"id\":" + request.getId() + ", \"error\":{\"code\":-32603, \"message\": \"internal server error\"}}";

							DataBuffer buffer = bufferFactory.wrap(error.getBytes(Charset.defaultCharset()));
							Flux<DataBuffer> body = Flux.just(buffer);
							adaptedResponse.writeWith(body).subscribe();
							adaptedResponse.setComplete().subscribe();
						})
						.doOnSuccess(aVoid -> log.debug("Handling completed with success"))
						.subscribe();


				if (request.getId() != null) {
					in.withConnection(c -> {
						String disposableId = c.channel().id().asLongText() + request.getId();
						if (disposableId != null) {
							disposables.put(disposableId, disposable);
						}
					});
				}
			});

		return out.options(NettyPipeline.SendOptions::flushOnEach)
				.neverComplete();
	}
}
