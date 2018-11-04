import { TestBed, inject } from '@angular/core/testing';

import { SpringDslEditorService } from './spring-dsl-editor.service';

describe('SpringDslEditorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SpringDslEditorService]
    });
  });

  it('should be created', inject([SpringDslEditorService], (service: SpringDslEditorService) => {
    expect(service).toBeTruthy();
  }));
});
