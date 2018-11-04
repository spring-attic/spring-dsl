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
import { ModuleWithProviders, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SpringDslEditorComponent } from './spring-dsl-editor.component';
import { SpringMonacoEditorComponent } from './spring-monaco-editor/spring-monaco-editor.component';
import { SpringDslEditorService } from './spring-dsl-editor.service';
import { SPRING_MONACO_EDITOR_CONFIG, SpringMonacoEditorConfig } from './spring-monaco-editor/config';
import { SPRING_DSL_EDITOR_CONFIG, SpringDslEditorConfig } from './config';
import { MonacoLoaderService } from "./spring-monaco-editor/monaco-loader.service";
import { DefaultMonacoEditorService, MonacoEditorService } from "./spring-monaco-editor/monaco-editor.service";

/**
 * Main module definition for Spring Dsl Editor.
 *
 * @author Janne Valkealahti
 */
@NgModule({
  imports: [
    FormsModule
  ],
  declarations: [
    SpringDslEditorComponent,
    SpringMonacoEditorComponent
  ],
  exports: [
    SpringDslEditorComponent,
    SpringMonacoEditorComponent
  ],
  providers: [
    SpringDslEditorService,
    MonacoLoaderService,
    { provide: MonacoEditorService, useClass: DefaultMonacoEditorService }
  ]
})
export class SpringDslEditorModule {
  public static forRoot(springMonacoEditorConfig: SpringMonacoEditorConfig = {},
                        springDslEditorConfig: SpringDslEditorConfig = {}): ModuleWithProviders {
    return {
      ngModule: SpringDslEditorModule,
      providers: [
        { provide: SPRING_MONACO_EDITOR_CONFIG, useValue: springMonacoEditorConfig },
        { provide: SPRING_DSL_EDITOR_CONFIG, useValue: springDslEditorConfig }
      ]
    };
  }
}
