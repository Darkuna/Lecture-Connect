import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {MessageService} from "primeng/api";
import {LoginUserInfoService} from "../../services/login-user-info.service";
import {LoginUserInfoServiceNew} from "../../services/login-user-info.service_new";
import {tap} from "rxjs";

interface LoginObj {
  name: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponentNew {
  stayLogin:boolean = false;
  loginObj: LoginObj = {
    name: '',
    password: ''
  }

  constructor(
    private loginService: LoginUserInfoServiceNew,
    private router: Router,
  ) {
  }

  login(){
    this.loginService.login(this.loginObj)
      .pipe(tap((res) => {
        if(this.stayLogin){
          localStorage.setItem('authUser', JSON.stringify(res));
        } else {
          sessionStorage.setItem('authUser', JSON.stringify(res));
        }
    }));
  }}
