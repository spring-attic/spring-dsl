/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { Injectable } from '@angular/core';

/**
 * Abstract interface defining monaco editor service.
 *
 * @author Janne Valkealahti
 */
export abstract class MonacoEditorService {

    abstract create(domElement: HTMLElement,
                    options?: monaco.editor.IEditorConstructionOptions,
                    override?: monaco.editor.IEditorOverrideServices): monaco.editor.IStandaloneCodeEditor;

}

/**
 * Default implementation of a MonacoEditorService.
 *
 * @author Janne Valkealahti
 */
@Injectable()
export class DefaultMonacoEditorService extends MonacoEditorService {

  constructor() {
    super();
  }

  public create(domElement: HTMLElement,
                options?: monaco.editor.IEditorConstructionOptions,
                override?: monaco.editor.IEditorOverrideServices): monaco.editor.IStandaloneCodeEditor {
    return monaco.editor.create(domElement, options, override);
  }
}
