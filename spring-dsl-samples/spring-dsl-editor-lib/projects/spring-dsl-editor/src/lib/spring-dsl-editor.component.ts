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
import { Component, EventEmitter, Output, Input, ViewChild, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { SpringDslEditorService } from './spring-dsl-editor.service';
import { SpringDslDocumentService } from "./spring-dsl-document.service";
import { SpringMonacoEditorComponent } from "./spring-monaco-editor/spring-monaco-editor.component";

/**
 * Component handling higher level integration with a monaco editor.
 *
 * Child component SpringMonacoEditorComponent is the one handling
 * lower lever monaco editor integration and this component integrates
 * into it by providing higher level functionalies which are most
 * likely needed in a real UI integration.
 *
 * @author Janne Valkealahti
 */
@Component({
  selector: 'spring-dsl-editor',
  templateUrl: './spring-dsl-editor.component.html',
  styleUrls: ['./spring-dsl-editor.component.css']
})
export class SpringDslEditorComponent implements OnInit, OnDestroy {

  @ViewChild(SpringMonacoEditorComponent)
  private springMonacoEditor: SpringMonacoEditorComponent;

  @Output('editorOptions')
  public editorOptions = {model: {}};

  @Output('editorDirty')
  public editorDirty = new EventEmitter<boolean>();

  @Input('language')
  set language(language: string) {
    this.editorOptions.model['language'] = language;
  }

  @Input('uri')
  set uri(uri: string) {
    this.editorOptions.model['uri'] = uri;
  }

  @Input('content')
  set content(content: string) {
    this.editorOptions.model['value'] = content;
  }

  private editorService: SpringDslEditorService;
  private documentService: SpringDslDocumentService;
  private editorServiceSubscription: Subscription;

  constructor(editorService: SpringDslEditorService, documentService: SpringDslDocumentService) {
    this.editorService = editorService;
    this.documentService = documentService;
  }

  ngOnInit() {
    this.editorServiceSubscription = this.editorService.saveRequests.subscribe(
      uri => {
        if (this.editorOptions.model['uri'] == uri) {
          this.save();
        }
      }
    );
  }

  ngOnDestroy() {
    if (this.editorServiceSubscription) {
      this.editorServiceSubscription.unsubscribe();
    }
  }

  public initLanguageClient(editor: any) {
    this.editorService.initLanguageClient();
    if (this.editorOptions.model['uri']) {
      this.documentService.getDocument(editor.getModel().uri.toString()).subscribe(
        data => {
          editor.setValue(data);
        },
        error => {
          console.log("Error", error);
        }
      );
    }
  }

  public save() {
    const editor = this.springMonacoEditor.getEditor();
    const uri = editor.getModel().uri.toString();
    const value = editor.getValue();
    this.documentService.saveDocument(uri, value).subscribe(
      data => {
      },
      error => {
        console.log("Error", error);
      }
    );
  }
}
