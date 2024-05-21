import {Component} from '@angular/core';

@Component({
  selector: 'app-wizard',
  templateUrl: './wizard.component.html',
  styleUrl: './wizard.component.css'
})
export class WizardComponent {
  active: number = 0;


  public getColorBasedOnIndex(type: string, index: number): string {
    if (index > this.active) {
      return '#CDCDCC';
    }

    if (type === 'i') {
      switch (index) {
        case 0:
          return '#9EC252';
        case 1:
          return '#75CF84';
        case 2:
          return '#75CFCA';
        case 3:
          return '#75A9CF';
        default:
          return '#CDCDCC';
      }
    } else {
      return '#070707';
    }
  }
}
