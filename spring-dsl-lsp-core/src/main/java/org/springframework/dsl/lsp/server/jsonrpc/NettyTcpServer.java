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

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dsl.lsp.server.LspServer;
import org.springframework.dsl.lsp.server.LspServerException;
import org.springframework.dsl.lsp.server.PortInUseException;
import org.springframework.util.Assert;

import reactor.ipc.netty.tcp.BlockingNettyContext;
import reactor.ipc.netty.tcp.TcpServer;

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
	private BlockingNettyContext nettyContext;

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
		if (this.nettyContext == null) {
			try {
				nettyContext = startServer();
			} catch (Exception e) {
				if (findBindException(e) != null) {
					SocketAddress address = this.tcpServer.options().getAddress();
					if (address instanceof InetSocketAddress) {
						throw new PortInUseException(
								((InetSocketAddress) address).getPort());
					}
				}
				throw new LspServerException("Unable to start Netty", e);
			}
			log.info("Netty started on port(s) {}", getPort());
			startDaemonAwaitThread(this.nettyContext);
		}
	}

	@Override
	public void stop() {
		log.debug("Netty stop called");
		if (nettyContext != null) {
			nettyContext.shutdown();
			nettyContext = null;
		}
	}

	@Override
	public int getPort() {
		if (nettyContext != null) {
			return nettyContext.getPort();
		}
		return -1;
	}

	private BlockingNettyContext startServer() {
		if (this.lifecycleTimeout != null) {
			return tcpServer.start(handlerAdapter, lifecycleTimeout);
		}
		return tcpServer.start(handlerAdapter);
	}

	private void startDaemonAwaitThread(BlockingNettyContext nettyContext) {
		Thread awaitThread = new Thread("server") {

			@Override
			public void run() {
				nettyContext.getContext().onClose().block();
			}

		};
		awaitThread.setContextClassLoader(getClass().getClassLoader());
		awaitThread.setDaemon(false);
		awaitThread.start();
	}

	private BindException findBindException(Exception ex) {
		Throwable candidate = ex;
		while (candidate != null) {
			if (candidate instanceof BindException) {
				return (BindException) candidate;
			}
			candidate = candidate.getCause();
		}
		return null;
	}
}
