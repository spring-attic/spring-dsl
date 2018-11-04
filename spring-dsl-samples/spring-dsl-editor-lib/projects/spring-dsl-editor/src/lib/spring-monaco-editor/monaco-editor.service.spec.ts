import { TestBed, inject } from '@angular/core/testing';

import { MonacoEditorService } from './monaco-editor.service';

describe('MonacoEditorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MonacoEditorService]
    });
  });

  it('should be created', inject([MonacoEditorService], (service: MonacoEditorService) => {
    expect(service).toBeTruthy();
  }));
});
