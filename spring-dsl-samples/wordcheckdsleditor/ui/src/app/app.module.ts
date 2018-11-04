import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SpringDslEditorModule, SpringDslEditorConfig, SpringMonacoEditorConfig } from 'spring-dsl-editor';

const springMonacoEditorConfig: SpringMonacoEditorConfig = {
  defaultOptions: {
    language: 'wordcheck'
  },
  onMonacoLoad: () => {
    (<any>window).monaco.languages.register({ id: 'wordcheck' });
  }
};

const springDslEditorConfig: SpringDslEditorConfig = {
  documentSelector: ['wordcheck']
};


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    SpringDslEditorModule.forRoot(springMonacoEditorConfig, springDslEditorConfig)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
