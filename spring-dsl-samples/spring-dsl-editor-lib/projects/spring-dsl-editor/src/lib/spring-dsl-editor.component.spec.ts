import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringDslEditorComponent } from './spring-dsl-editor.component';

describe('SpringDslEditorComponent', () => {
  let component: SpringDslEditorComponent;
  let fixture: ComponentFixture<SpringDslEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringDslEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringDslEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
