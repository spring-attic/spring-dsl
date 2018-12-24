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
import { TestBed, inject } from '@angular/core/testing';

import { MonacoLoaderService } from './monaco-loader.service';
import { SPRING_MONACO_EDITOR_CONFIG } from "./config";

describe('MonacoLoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MonacoLoaderService,
        { provide: SPRING_MONACO_EDITOR_CONFIG, useValue: {} }
      ]
    });
  });

  it('should be created', inject([MonacoLoaderService], (service: MonacoLoaderService) => {
    expect(service).toBeTruthy();
  }));
});
