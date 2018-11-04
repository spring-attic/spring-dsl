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
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';

/**
 * Wrapping tab name and language together.
 *
 * @author Janne Valkealahti
 */
export class TabInfo {
  constructor(public name: string, public language: string) {}
}

/**
 * Component wrapping logic handling editors in tabs.
 *
 * @author Janne Valkealahti
 */
@Component({
  selector: 'app-editor-tab-group',
  templateUrl: 'editor-tab-group.component.html',
  styleUrls: ['editor-tab-group.component.css'],
})
export class EditorTabGroupComponent {

  public tabs: TabInfo[] = [];
  public selected = new FormControl(0);

  public openTab(name: string, language?: string) {
    const index = this.findTabIndex(name);
    if (index > -1) {
      this.selected.setValue(index);
    } else {
      this.tabs.push(new TabInfo(name, language));
      this.selected.setValue(this.tabs.length - 1);
    }
  }

  public closeTab(name: string) {
    const index = this.findTabIndex(name);
    if (index > -1) {
      this.tabs.splice(index, 1);
    }
  }

  private findTabIndex(name: string): number {
    return this.tabs.findIndex(item => item.name === name);
  }
}
