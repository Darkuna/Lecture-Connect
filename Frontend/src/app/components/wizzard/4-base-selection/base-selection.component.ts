import {Component} from '@angular/core';
import {WizardComponent} from "../wizard.component";

@Component({
  selector: 'app-base-selection',
  templateUrl: './base-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class BaseSelectionComponent {
  constructor(
    private wizard: WizardComponent,
  ) {
  }

  getColor(type: string, index: number): string {
    return this.wizard.getColorBasedOnIndex(type, index);
  }

  isActive(): number {
    return this.wizard.active;
  }
}
