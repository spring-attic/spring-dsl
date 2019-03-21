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
import {Component, EventEmitter, forwardRef, Inject, NgZone, Optional, Output} from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { fromEvent } from 'rxjs';
import { BaseEditor } from './base-editor';
import { SPRING_DSL_ACTION, SPRING_MONACO_EDITOR_CONFIG, SpringDslAction, SpringMonacoEditorConfig } from './config';
import { MonacoLoaderService } from "./monaco-loader.service";
import { MonacoEditorService } from "./monaco-editor.service";

/**
 * Component handling low level integration with a monaco editor.
 *
 * @author Janne Valkealahti
 */
@Component({
  selector: 'spring-monaco-editor',
  templateUrl: './spring-monaco-editor.component.html',
  styleUrls: ['./spring-monaco-editor.component.css'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SpringMonacoEditorComponent),
    multi: true
  }]
})
export class SpringMonacoEditorComponent extends BaseEditor {

  propagateChange = (_: any) => {};
  onTouched = () => {};

  @Output('editorDirty')
  editorDirty = new EventEmitter<boolean>();

  constructor(private zone: NgZone,
              @Inject(SPRING_MONACO_EDITOR_CONFIG) private editorConfig: SpringMonacoEditorConfig,
              @Inject(SPRING_DSL_ACTION) @Optional() private actions: SpringDslAction[],
              monacoLoaderService: MonacoLoaderService,
              private monacoEditorService: MonacoEditorService) {
    super(editorConfig, monacoLoaderService);
  }

  public registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  public registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  protected initMonaco(options: any): void {
    const hasModel = !!options.model;

    if (hasModel) {
      options.model = monaco.editor.createModel(options.model.value, options.model.language, options.model.uri);
    }
    this.setEditor(this.monacoEditorService.create(this.editorContainer.nativeElement, options));

    if (this.actions) {
      this.actions.forEach( (action) => {
        this.getEditor().addAction(action);
      });
    }

    this.getEditor().onDidChangeModelContent((e: any) => {
      const value = this.getEditor().getValue();
      this.propagateChange(value);
      this.editorDirty.emit(true);
      // value is not propagated to parent when executing outside zone.
      // this.zone.run(() => this._value = value);
    });

    this.getEditor().onDidBlurEditor((e: any) => {
      this.onTouched();
    });

    // refresh layout on resize event.
    if (this.getWindowResizeSubscription()) {
      this.getWindowResizeSubscription().unsubscribe();
    }
    this.setWindowResizeSubscription(fromEvent(window, 'resize').subscribe(() => this.getEditor().layout()));
    this.editorChange.emit(this.getEditor());
  }
}
