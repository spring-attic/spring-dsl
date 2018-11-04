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
import { Inject, Injectable } from '@angular/core';
import { SPRING_MONACO_EDITOR_CONFIG, SpringMonacoEditorConfig } from "./config";

@Injectable()
export class MonacoLoaderService {

  private loadedMonaco: boolean = false;
  private loadPromise: Promise<void>;

  constructor(@Inject(SPRING_MONACO_EDITOR_CONFIG) private editorConfig: SpringMonacoEditorConfig) {
  }

  public load(): Promise<void> {
    if (this.loadedMonaco) {
      return this.loadPromise;
    } else {
      this.loadedMonaco = true;
      this.loadPromise = new Promise<void>((resolve: any) => {
        const baseUrl = this.editorConfig.baseUrl || '/assets';
        if (typeof((<any>window).monaco) === 'object') {
          resolve();
          return;
        }
        const onGotAmdLoader: any = () => {
          // Load monaco
          (<any>window).require.config({ paths: { 'vs': `${baseUrl}/monaco/vs` } });
          (<any>window).require(['vs/editor/editor.main'], () => {
            if (typeof this.editorConfig.onMonacoLoad === 'function') {
              this.editorConfig.onMonacoLoad();
            }
            resolve();
          });
        };

        // Load AMD loader if necessary
        if (!(<any>window).require) {
          const loaderScript: HTMLScriptElement = document.createElement('script');
          loaderScript.type = 'text/javascript';
          loaderScript.src = `${baseUrl}/monaco/vs/loader.js`;
          loaderScript.addEventListener('load', onGotAmdLoader);
          document.body.appendChild(loaderScript);
        } else {
          onGotAmdLoader();
        }
      });
      return this.loadPromise;
    }
  }
}
