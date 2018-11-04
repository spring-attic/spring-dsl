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
import { Injectable } from "@angular/core";
import { SpringDslAction, SPRING_DSL_ACTION, SpringDslEditorService } from 'spring-dsl-editor';

@Injectable()
export class Action1 extends SpringDslAction {

  id = 'action1';
  label = 'Showcase log hi';
  contextMenuGroupId = 'navigation';
  run(editor: monaco.editor.ICodeEditor): void {
    console.log('hi');
    return null;
  };
}

@Injectable()
export class Action2 extends SpringDslAction {

  constructor(private editorService: SpringDslEditorService) {
    super();
  }

  id = 'action2';
  label = 'Showcase ping';
  contextMenuGroupId = 'navigation';
  run(editor: monaco.editor.ICodeEditor): void {
    this.editorService.getLanguageClient().sendNotification('showcase/ping');
    return null;
  };
}

@Injectable()
export class Action3 extends SpringDslAction {

  constructor(private editorService: SpringDslEditorService) {
    super();
  }

  id = 'action3';
  label = 'Showcase client log';
  contextMenuGroupId = 'navigation';
  run(editor: monaco.editor.ICodeEditor): void {
    this.editorService.getLanguageClient().sendNotification('showcase/log');
    return null;
  };
}

@Injectable()
export class Action4 extends SpringDslAction {

  constructor(private editorService: SpringDslEditorService) {
    super();
  }

  id = 'action4';
  label = 'Showcase client message';
  contextMenuGroupId = 'navigation';
  run(editor: monaco.editor.ICodeEditor): void {
    this.editorService.getLanguageClient().sendNotification('showcase/message');
    return null;
  };
}

export const ACTIONS = [
  { provide: SPRING_DSL_ACTION, multi: true, useClass: Action1 },
  { provide: SPRING_DSL_ACTION, multi: true, useClass: Action2 },
  { provide: SPRING_DSL_ACTION, multi: true, useClass: Action3 },
  { provide: SPRING_DSL_ACTION, multi: true, useClass: Action4 }
];
