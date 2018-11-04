import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringMonacoEditorComponent } from './spring-monaco-editor.component';

describe('SpringMonacoEditorComponent', () => {
  let component: SpringMonacoEditorComponent;
  let fixture: ComponentFixture<SpringMonacoEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringMonacoEditorComponent ]
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
