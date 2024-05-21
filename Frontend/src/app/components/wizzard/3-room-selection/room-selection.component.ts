import {Component} from '@angular/core';
import {WizardComponent} from "../wizard.component";

@Component({
  selector: 'app-room-selection',
  templateUrl: './room-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class RoomSelectionComponent {
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
