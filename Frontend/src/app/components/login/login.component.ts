import {Component, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {MessageService} from "primeng/api";
import {LoginUserInfoService} from "../../services/login-user-info.service";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

interface LoginObj {
  name: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy{
  rememberMe: boolean = false;
  loginObj: LoginObj = {
    name: '',
    password: ''
  }
  _loginSub: Subscription | null = null;

  constructor(
    private router: Router,
    private messageService: MessageService,
    private userInfoService: LoginUserInfoService
  ) { }

  ngOnDestroy():void{
    if (this._loginSub)
      this._loginSub.unsubscribe();
  }

  login():void{
    this._loginSub = this.userInfoService.loginUser(this.loginObj, this.rememberMe)
      .subscribe({
          next: () => {
            if(this.userInfoService.isLoggedIn())
              this.router.navigate(['/user/home']);
          },
        error: (error:HttpErrorResponse) => { this.messageService
          .add({severity: 'error', summary: 'Error', detail: error.error}); }
        });
  }
}
