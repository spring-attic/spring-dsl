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
package org.springframework.dsl.lsp.server.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dsl.lsp.server.LspServer;
import org.springframework.dsl.lsp.server.LspServerFactory;
import org.springframework.util.Assert;

// TODO: Auto-generated Javadoc
/**
 * Class which can be created into application context as bean and will listen
 * {@link ContextRefreshedEvent} to start associated {@link LspServer} and stop
 * it via {@link DisposableBean}.
 * <p>
 * Effectively needed if CoapServer is created via
 * {@link LspServerFactory#getLspServer()} as instance is paused when returned
 * from a factory.
 *
 * @author Janne Valkealahti
 *
 */
public class LspServerRefreshListener implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

	/** The lsp server. */
	private final LspServer lspServer;

	/**
	 * Instantiates a new lsp server refresh listener.
	 *
	 * @param lspServer the lsp server
	 */
	public LspServerRefreshListener(LspServer lspServer) {
		Assert.notNull(lspServer, "lspServer cannot be null");
		this.lspServer = lspServer;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		lspServer.start();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		lspServer.stop();
	}
}
