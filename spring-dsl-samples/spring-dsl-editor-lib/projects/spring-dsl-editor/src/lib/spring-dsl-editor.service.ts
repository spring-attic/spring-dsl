/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import {Inject, Injectable, OnDestroy, Optional} from '@angular/core';
import { BaseLanguageClient, CloseAction, createConnection, ErrorAction, MonacoLanguageClient,
  MonacoServices} from 'monaco-languageclient';
import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import { SPRING_DSL_EDITOR_CONFIG, SpringDslEditorConfig } from './config';

const normalizeUrl = require('normalize-url');

/**
 * Service providing various features.
 *
 * @author Janne Valkealahti
 */
@Injectable()
export class SpringDslEditorService implements OnDestroy {

  private initialised: boolean;
  private editorConfig: SpringDslEditorConfig;
  private languageClient: BaseLanguageClient;

  constructor(@Inject(SPRING_DSL_EDITOR_CONFIG) editorConfig: SpringDslEditorConfig) {
    this.initialised = false;
    this.editorConfig = editorConfig;
  }

  ngOnDestroy() {
  }

  public getLanguageClient(): BaseLanguageClient {
    return this.languageClient;
  }

  public initLanguageClient() {
    if (this.initialised) {
      return;
    }
    this.initialised = true;
    MonacoServices.install(<monaco.editor.IStandaloneCodeEditor>{});
    const url = this.createUrl('ws');
    const webSocket = new WebSocket(url);

    listen({
      webSocket,
      onConnection: connection => {
        // create and start the language client
        // const languageClient = this.createLanguageClient(connection);
        // const disposable = languageClient.start();
        this.languageClient = this.createLanguageClient(connection);
        const disposable = this.languageClient.start();
        connection.onClose(() => disposable.dispose());
      }
    });
  }

  private createLanguageClient(connection: MessageConnection): BaseLanguageClient {
    return new MonacoLanguageClient({
      name: 'Sample Language Client',
      clientOptions: {
        // use a language id as a document selector
        documentSelector: this.editorConfig.documentSelector,
        synchronize: {
        },
        // disable the default error handler
        errorHandler: {
          error: () => ErrorAction.Continue,
          closed: () => CloseAction.DoNotRestart
        }
      },
      // create a language client connection from the JSON RPC connection on demand
      connectionProvider: {
        get: (errorHandler, closeHandler) => {
          return Promise.resolve(createConnection(connection, errorHandler, closeHandler));
        }
      }
    });
  }

  private createUrl(path: string): string {
    const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
    return normalizeUrl(`${protocol}://${location.host}${location.pathname}${path}`);
  }
}
