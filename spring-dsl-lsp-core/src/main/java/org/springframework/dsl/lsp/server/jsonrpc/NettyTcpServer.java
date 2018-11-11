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
package org.springframework.dsl.lsp.server.jsonrpc;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.lsp.server.LspServer;
import org.springframework.dsl.lsp.server.LspServerException;
import org.springframework.dsl.lsp.server.PortInUseException;
import org.springframework.util.Assert;

import reactor.netty.ChannelBindException;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

/**
 * Main entry point to handle lifecycle of a {@link TcpServer} and its used handler.
 *
 * @author Janne Valkealahti
 *
 */
public class NettyTcpServer implements LspServer {

	private static final Logger log = LoggerFactory.getLogger(NettyTcpServer.class);
	private final TcpServer tcpServer;
	private final ReactorJsonRpcHandlerAdapter handlerAdapter;
	private final Duration lifecycleTimeout;
	private DisposableServer disposableServer;

	/**
	 * Instantiates a new netty tcp server.
	 *
	 * @param tcpServer the tcp server
	 * @param handlerAdapter the handler adapter
	 * @param lifecycleTimeout the lifecycle timeout
	 */
	public NettyTcpServer(TcpServer tcpServer, ReactorJsonRpcHandlerAdapter handlerAdapter, Duration lifecycleTimeout) {
		Assert.notNull(tcpServer, "TcpServer must not be null");
		Assert.notNull(handlerAdapter, "ReactorJsonRpcHandlerAdapter must not be null");
		this.tcpServer = tcpServer;
		this.handlerAdapter = handlerAdapter;
		this.lifecycleTimeout = lifecycleTimeout;
	}

	@Override
	public void start() {
		log.debug("Netty start called");
		if (this.disposableServer == null) {
			try {
				disposableServer = startServer();
			} catch (Exception e) {
				ChannelBindException bindException = findBindException(e);
				if (bindException != null) {
					throw new PortInUseException(bindException.localPort());
				}
				throw new LspServerException("Unable to start Netty", e);
			}
			log.info("Netty started on port(s) {}", getPort());
			startDaemonAwaitThread(this.disposableServer, getClass().getClassLoader());
		}
	}

	@Override
	public void stop() {
		log.debug("Netty stop called");
		if (disposableServer != null) {
			disposableServer.disposeNow();
			disposableServer = null;
		}
	}

	@Override
	public int getPort() {
		if (disposableServer != null) {
			return disposableServer.port();
		}
		return -1;
	}

	private DisposableServer startServer() {
		if (this.lifecycleTimeout != null) {
			return tcpServer.handle(handlerAdapter).bindNow(this.lifecycleTimeout);
		}
		return tcpServer.handle(handlerAdapter).bindNow();
	}

	private static void startDaemonAwaitThread(DisposableServer disposableServer, ClassLoader classLoader) {
		Thread awaitThread = new Thread("server") {

			@Override
			public void run() {
				disposableServer.onDispose().block();
			}

		};
		awaitThread.setContextClassLoader(classLoader);
		awaitThread.setDaemon(false);
		awaitThread.start();
	}

	private ChannelBindException findBindException(Exception ex) {
		Throwable candidate = ex;
		while (candidate != null) {
			if (candidate instanceof ChannelBindException) {
				return (ChannelBindException) candidate;
			}
			candidate = candidate.getCause();
		}
		return null;
	}
}
