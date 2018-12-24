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
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringMonacoEditorComponent } from './spring-monaco-editor.component';
import { SPRING_MONACO_EDITOR_CONFIG } from "./config";
import { SPRING_DSL_EDITOR_CONFIG } from "../config";
import { MonacoLoaderService } from "./monaco-loader.service";
import { DefaultMonacoEditorService, MonacoEditorService } from "./monaco-editor.service";

describe('SpringMonacoEditorComponent', () => {
  let component: SpringMonacoEditorComponent;
  let fixture: ComponentFixture<SpringMonacoEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringMonacoEditorComponent ],
      providers: [
        MonacoLoaderService,
        { provide: MonacoEditorService, useClass: DefaultMonacoEditorService },
        { provide: SPRING_MONACO_EDITOR_CONFIG, useValue: {} },
        { provide: SPRING_DSL_EDITOR_CONFIG, useValue: {} }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringMonacoEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
