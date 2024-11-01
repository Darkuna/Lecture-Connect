import { Component } from '@angular/core';
import {ProgressService} from "../../services/progress.service";

@Component({
  selector: 'app-progress',
  templateUrl: './progress.component.html',
  styleUrl: './progress.component.css'
})
export class ProgressComponent {
  constructor(
    protected loader: ProgressService
  ) {
  }
}
