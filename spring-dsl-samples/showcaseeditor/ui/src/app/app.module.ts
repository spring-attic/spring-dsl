import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTabsModule, MatIconModule } from '@angular/material';
import { AppComponent } from './app.component';
import { SpringDslEditorModule, SpringMonacoEditorConfig, SpringDslEditorConfig } from 'spring-dsl-editor';
import { EditorTabGroupComponent } from './editor-tab-group/editor-tab-group.component';
import { ACTIONS } from './actions';

const springMonacoEditorConfig: SpringMonacoEditorConfig = {
  defaultOptions: {
    theme: 'vs',
    automaticLayout: true,
    fixedOverflowWidgets: true
  },
  onMonacoLoad: () => {
    (<any>window).monaco.languages.register({ id: 'simple' });
    (<any>window).monaco.languages.register({ id: 'wordcheck' });
    (<any>window).monaco.languages.register({ id: 'dot' });
  }
};
const springDslEditorConfig: SpringDslEditorConfig = {
  documentSelector: ['simple', 'wordcheck', 'dot']
};

@NgModule({
  declarations: [
    AppComponent,
    EditorTabGroupComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    MatTabsModule,
    MatIconModule,
    NoopAnimationsModule,
    SpringDslEditorModule.forRoot(springMonacoEditorConfig, springDslEditorConfig)
  ],
  providers: [
    ...ACTIONS
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
