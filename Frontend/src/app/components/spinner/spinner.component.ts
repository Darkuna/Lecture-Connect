import { Component } from '@angular/core';
import {LoadingService} from "../../services/loading.service";

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrl: './spinner.component.css'
})
export class SpinnerComponent {
  constructor(
    public loader: LoadingService
  ) { }
}
