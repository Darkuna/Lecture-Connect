import { Component } from '@angular/core';
import {ProgressService} from "../../services/progress.service";
import {animate, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-progress',
  templateUrl: './progress.component.html',
  styleUrl: './progress.component.css',
  animations: [
    trigger('fade', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('600ms ease-in', style({ opacity: 1 })),
      ]),
      transition(':leave', [
        animate('400ms ease-out', style({ opacity: 0 })),
      ]),
    ]),
  ],
})
export class ProgressComponent {
  constructor(
    protected loader: ProgressService
  ) {
  }
}
