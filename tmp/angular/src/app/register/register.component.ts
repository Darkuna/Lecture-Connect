import { Component } from '@angular/core';
import { RegisterService } from './register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name: string = "";
  passwd: string = "";

  constructor(private registerService: RegisterService) { }

  save() {
    this.registerService.save(this.name, this.passwd);
  }
}
