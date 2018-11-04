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
import {Component, OnInit, forwardRef, Inject, Input, NgZone, Optional} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { fromEvent } from 'rxjs';
import { BaseEditor } from './base-editor';
import { SPRING_DSL_ACTION, SPRING_MONACO_EDITOR_CONFIG, SpringDslAction, SpringMonacoEditorConfig } from './config';
import { MonacoLoaderService } from "./monaco-loader.service";
import { MonacoEditorService } from "./monaco-editor.service";
// import { SpringMonacoEditorModel } from './types';

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
export class SpringMonacoEditorComponent extends BaseEditor implements ControlValueAccessor {
  private _value: string = '';

  propagateChange = (_: any) => {};
  onTouched = () => {};

  // @Input('model')
  // set model(model: SpringMonacoEditorModel) {
  //   this.options.model = model;
  //   if (this.editor) {
  //     this.editor.dispose();
  //     this.initMonaco(this.options);
  //   }
  // }

  constructor(private zone: NgZone,
              @Inject(SPRING_MONACO_EDITOR_CONFIG) private editorConfig: SpringMonacoEditorConfig,
              @Inject(SPRING_DSL_ACTION) @Optional() private actions: SpringDslAction[],
              monacoLoaderService: MonacoLoaderService,
              private monacoEditorService: MonacoEditorService) {
    super(editorConfig, monacoLoaderService);
  }

  writeValue(value: any): void {
    // this._value = value || '';
    // // Fix for value change while dispose in process.
    // setTimeout(() => {
    //   if (this.editor && !this.options.model) {
    //     this.editor.setValue(this._value);
    //   }
    // });
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  protected initMonaco(options: any): void {

    const hasModel = !!options.model;

    if (hasModel) {
      options.model = monaco.editor.createModel(options.model.value, options.model.language, options.model.uri);
    }
    // this.editor = monaco.editor.create(this.editorContainer.nativeElement, options);
    this.editor = this.monacoEditorService.create(this.editorContainer.nativeElement, options);


    if (this.actions) {
      this.actions.forEach( (action) => {
        this.editor.addAction(action);
      });
    }

    if (!hasModel) {
      this.editor.setValue(this._value);
    }

    this.editor.onDidChangeModelContent((e: any) => {
      const value = this.editor.getValue();
      this.propagateChange(value);
      // value is not propagated to parent when executing outside zone.
      this.zone.run(() => this._value = value);
    });

    this.editor.onDidBlurEditor((e: any) => {
      this.onTouched();
    });

    // refresh layout on resize event.
    if (this.windowResizeSubscription) {
      this.windowResizeSubscription.unsubscribe();
    }
    this.windowResizeSubscription = fromEvent(window, 'resize').subscribe(() => this.editor.layout());
    this.editorChange.emit(this.editor);
  }

}
