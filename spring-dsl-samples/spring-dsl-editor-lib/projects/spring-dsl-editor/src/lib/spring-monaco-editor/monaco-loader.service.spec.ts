import { TestBed, inject } from '@angular/core/testing';

import { MonacoLoaderService } from './monaco-loader.service';

describe('MonacoLoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MonacoLoaderService]
    });
  });

  it('should be created', inject([MonacoLoaderService], (service: MonacoLoaderService) => {
    expect(service).toBeTruthy();
  }));
});
