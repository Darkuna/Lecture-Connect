import { HttpClient } from '@angular/common/http';
import {Component, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {LocalStorageService} from "ngx-webstorage";
import * as jwt_decode from 'jwt-decode';
import {MessageService} from "primeng/api";
import {Subscription} from "rxjs";
import {LoginUserInfoService} from "../../services/login-user-info.service";

interface LoginObj {
  name: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginObj: LoginObj = {
    name: "",
    password: ""
  }

  constructor(
    private router: Router,
    private messageService: MessageService,
    private userInfoService: LoginUserInfoService
  ) {
  }

  login() {
    this.userInfoService.loginUser(this.loginObj) .then(([status, message]) => {
      if (status) {
        this.messageService.add({severity: 'success',summary: `Welcome back ${decodedToken['username']}`});
        this.router.navigate(['/home']);
      } else {
        this.messageService.add({severity: 'error', summary: 'Upload Fault', detail: message});
      }
    })
      .catch(([status, message]) => {
        this.messageService.add({severity: 'error', summary: 'Upload Fault', detail: message});
      });
  }


}
