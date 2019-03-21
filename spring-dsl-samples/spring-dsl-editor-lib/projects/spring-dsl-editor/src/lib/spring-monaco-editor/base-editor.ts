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
import { AfterViewInit, ElementRef, EventEmitter, Input, OnDestroy, Output, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { SpringMonacoEditorConfig } from './config';
import { MonacoLoaderService } from "./monaco-loader.service";

/**
 * Base class for low level integration with a monaco editor.
 *
 * @author Janne Valkealahti
 */
export abstract class BaseEditor implements AfterViewInit, OnDestroy {

  @ViewChild('editorContainer')
  protected editorContainer: ElementRef;

  @Output()
  public editorChange = new EventEmitter<any>();

  @Input('options')
  set options(options: any) {
    this.editorOptions = Object.assign({}, this.config.defaultOptions, options);
    if (this.editor) {
      this.editor.dispose();
      this.initMonaco(options);
    }
  }

  get options(): any {
    return this.editorOptions;
  }

  private windowResizeSubscription: Subscription;
  private editor: any;
  private editorOptions: any;
  private config: SpringMonacoEditorConfig;
  private monacoLoaderService: MonacoLoaderService

  constructor(config: SpringMonacoEditorConfig, monacoLoaderService: MonacoLoaderService) {
    this.config = config;
    this.monacoLoaderService = monacoLoaderService;
  }

  ngAfterViewInit(): void {
    this.monacoLoaderService.load().then(() => {
      this.initMonaco(this.editorOptions);
    });
  }

  ngOnDestroy() {
    if (this.windowResizeSubscription) {
      this.windowResizeSubscription.unsubscribe();
    }
    if (this.editor) {
      this.editor.getModel().dispose();
      this.editor.dispose();
      this.editor = undefined;
    }
  }

  /**
   * Called to initialize a monaco editor.
   *
   * @param options
   */
  protected abstract initMonaco(options: any): void;

  protected getWindowResizeSubscription(): Subscription {
    return this.windowResizeSubscription;
  }

  protected setWindowResizeSubscription(subscription: Subscription): void {
    this.windowResizeSubscription = subscription;
  }

  public getEditor(): any {
    return this.editor;
  }

  protected setEditor(editor: any): void {
    this.editor = editor;
  }
}
