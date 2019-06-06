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
package org.springframework.dsl.lsp.server.config;

/**
 * Generic properties for dsl.
 *
 * @author Janne Valkealahti
 *
 */
public class DslProperties {

	private LspProperties lsp = new LspProperties();

	public LspProperties getLsp() {
		return lsp;
	}

	public void setLsp(LspProperties lsp) {
		this.lsp = lsp;
	}

	public static class LspProperties {

		private LspServerProperties server = new LspServerProperties();
		private LspClientProperties client = new LspClientProperties();

		public LspServerProperties getServer() {
			return server;
		}

		public void setServer(LspServerProperties server) {
			this.server = server;
		}

		public LspClientProperties getClient() {
			return client;
		}

		public void setClient(LspClientProperties client) {
			this.client = client;
		}
	}

	public static class WebSocketProperties {

		String path = "/ws";

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}

	public static class LspServerProperties {

		private Integer port;
		private LspServerSocketMode mode = LspServerSocketMode.PROCESS;
		private boolean forceJvmExitOnShutdown;
		private WebSocketProperties websocket = new WebSocketProperties();
		private TextDocumentProperties textDocument = new TextDocumentProperties();

		public LspServerSocketMode getMode() {
			return mode;
		}

		public void setMode(LspServerSocketMode mode) {
			this.mode = mode;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public boolean isForceJvmExitOnShutdown() {
			return forceJvmExitOnShutdown;
		}

		public void setForceJvmExitOnShutdown(boolean forceJvmExitOnShutdown) {
			this.forceJvmExitOnShutdown = forceJvmExitOnShutdown;
		}

		public WebSocketProperties getWebsocket() {
			return websocket;
		}

		public void setWebsocket(WebSocketProperties websocket) {
			this.websocket = websocket;
		}

		public TextDocumentProperties getTextDocument() {
			return textDocument;
		}

		public void setTextDocument(TextDocumentProperties textDocument) {
			this.textDocument = textDocument;
		}
	}

	public static class LspClientProperties {

		private Integer port;

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}
	}

	public enum LspServerSocketMode {
		PROCESS,
		SOCKET,
		WEBSOCKET;
	}

	public static class TextDocumentProperties {

		private DocumentSymbolProperties documentSymbol = new DocumentSymbolProperties();

		public DocumentSymbolProperties getDocumentSymbol() {
			return documentSymbol;
		}

		public void setDocumentSymbol(DocumentSymbolProperties documentSymbol) {
			this.documentSymbol = documentSymbol;
		}
	}

	public static class DocumentSymbolProperties {
		DocumentSymbolPrefer prefer = DocumentSymbolPrefer.DocumentSymbol;

		public DocumentSymbolPrefer getPrefer() {
			return prefer;
		}

		public void setPrefer(DocumentSymbolPrefer prefer) {
			this.prefer = prefer;
		}
	}

	public enum DocumentSymbolPrefer {
		DocumentSymbol,SymbolInformation;
	}
}
