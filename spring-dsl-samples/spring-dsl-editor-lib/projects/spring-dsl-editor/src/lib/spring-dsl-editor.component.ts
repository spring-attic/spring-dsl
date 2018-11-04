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
import { Component, OnInit, OnDestroy, Output, Input } from '@angular/core';
import { SpringDslEditorService } from './spring-dsl-editor.service';

/**
 * Component handling high level integration with a monaco editor.
 *
 * @author Janne Valkealahti
 */
@Component({
  selector: 'spring-dsl-editor',
  templateUrl: './spring-dsl-editor.component.html',
  styleUrls: ['./spring-dsl-editor.component.css']
})
export class SpringDslEditorComponent implements OnInit, OnDestroy {

  @Output('editorOptions')
  editorOptions = {};

  @Input('language')
  set language(language: string) {
    this.editorOptions['language'] = language;
  }

  private editorService: SpringDslEditorService;

  constructor(editorService: SpringDslEditorService) {
    this.editorService = editorService;
  }

  ngOnInit() {
  }

  ngOnDestroy() {
  }

  public initLanguageClient(editor: any) {
    this.editorService.initLanguageClient();
  }
}
