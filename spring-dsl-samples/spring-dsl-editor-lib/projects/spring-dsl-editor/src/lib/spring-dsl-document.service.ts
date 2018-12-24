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
import {Inject, Injectable} from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SPRING_DSL_EDITOR_CONFIG, SpringDslEditorConfig} from "./config";

/**
 * Service handling remove document open and save actions.
 *
 * @author Janne Valkealahti
 */
@Injectable()
export class SpringDslDocumentService {

  private editorConfig: SpringDslEditorConfig;
  private basePath: string = '/document';

  constructor(
    private httpClient: HttpClient,
    @Inject(SPRING_DSL_EDITOR_CONFIG) editorConfig: SpringDslEditorConfig) {
    this.editorConfig = editorConfig;
  }

  public getDocument(uri: string): Observable<string> {
    let params = new HttpParams().set('uri', uri);
    return this.httpClient
      .get(this.getBasePath(), {responseType: 'text', params: params});
  }

  public saveDocument(uri: string, content: string): Observable<any> {
    let params = new HttpParams().set('uri', uri);
    return this.httpClient
      .post(this.getBasePath(), content, {params: params});
  }

  public getBasePath(): string {
    if(!!this.editorConfig.documentServiceBasePath) {
      return this.editorConfig.documentServiceBasePath;
    } else {
      return this.basePath;
    }
  }
}
