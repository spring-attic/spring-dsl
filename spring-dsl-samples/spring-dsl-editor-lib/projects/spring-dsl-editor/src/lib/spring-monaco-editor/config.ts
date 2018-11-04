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
import { InjectionToken } from '@angular/core';

export const SPRING_MONACO_EDITOR_CONFIG = new InjectionToken('SPRING_MONACO_EDITOR_CONFIG');
export const SPRING_DSL_ACTION = new InjectionToken('SPRING_DSL_ACTION');

export interface SpringMonacoEditorConfig {
  baseUrl?: string;
  defaultOptions?: { [key: string]: any; };
  onMonacoLoad?: Function;
}

export abstract class SpringDslAction implements monaco.editor.IActionDescriptor {
  abstract id: string;
  abstract label: string;
  abstract run(editor: monaco.editor.ICodeEditor): void | monaco.Promise<void>;
}
