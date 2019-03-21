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
package org.springframework.dsl.lsp.server.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.lsp.server.jsonrpc.NettyTcpServer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import reactor.netty.Connection;
import reactor.netty.NettyPipeline;
import reactor.netty.tcp.TcpClient;

public class StdioSocketBridge {

	private static final Logger log = LoggerFactory.getLogger(StdioSocketBridge.class);
	private NettyTcpServer server;
	private BufferedReader reader;
	private BufferedWriter writer;
	private Connection connection;

	public StdioSocketBridge(NettyTcpServer server) {
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new BufferedWriter(new OutputStreamWriter(System.out));
		this.server = server;
	}

	private static class StdioGenerator implements Consumer<SynchronousSink<byte[]>> {

		BufferedReader reader;

		public StdioGenerator(BufferedReader reader) {
			this.reader = reader;
		}

		@Override
		public void accept(SynchronousSink<byte[]> sink) {
			log.trace("accept");
			try {
				int read = reader.read();
				log.trace("read {}", read);

				if (read == -1) {
					sink.complete();
				} else {
					byte[] bb = new byte[1];
					bb[0] = (byte)read;
					sink.next(bb);
				}

			} catch (IOException e) {
				log.error("{}", e);
				sink.error(e);
			}
		}

	}

	public void start() {
		log.trace("start 1 {}", server.getPort());

		StdioGenerator generator = new StdioGenerator(reader);
		Flux<byte[]> flux = Flux.generate(generator);

		connection = TcpClient.create()
				.port(server.getPort())
				.handle((in, out) -> {
					in.receive()
						.subscribe(c -> {
							String data = c.retain().duplicate().toString(Charset.defaultCharset());
							try {
								log.debug("Writing to client {}", data);
								writer.write(data);
								writer.flush();
							} catch (IOException e) {
								log.error("{}", e);
							}
						});

					flux.doOnNext(b -> {
						log.trace("sending {}", b);
						out.sendByteArray(Mono.just(b)).then().subscribe();
					})
					.subscribeOn(Schedulers.parallel())
					.subscribe();


					return out.options(NettyPipeline.SendOptions::flushOnEach)
							.neverComplete();

				})
				.connect()
				.block();

		log.trace("start 2");
	}

	public void stop() {
		if (connection != null) {
			connection.disposeNow();
		}
		connection = null;
	}

}
