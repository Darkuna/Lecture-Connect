import {HttpClient} from '@angular/common/http';
import {Component, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {LocalStorageService} from "ngx-webstorage";
import * as jwt_decode from 'jwt-decode';
import {MessageService} from "primeng/api";
import {Subscription} from "rxjs";
import {LoginUserInfoService} from "../../services/login-user-info.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {
  private loginSub: Subscription | null = null;

  loginObj: any = {
    name: "",
    password: ""
  }

  constructor(
    private router: Router,
    private http: HttpClient,
    private storage: LocalStorageService,
    private messageService: MessageService,
    private userInfoService: LoginUserInfoService
  ) {
  }

  ngOnDestroy(): void {
    if (!this.loginSub?.closed) {
      this.loginSub?.unsubscribe();
    }
  }

  login() {
    this.loginSub = this.http.post('/proxy/auth/login', this.loginObj)
      .subscribe({
        next: (token: any) => {
          if (token && token['token']) {
            const decodedToken = jwt_decode.jwtDecode(token['token']) as { [key: string]: string };
            this.userInfoService.username = decodedToken['username'];
            this.userInfoService.userRole = decodedToken['token'];
            this.userInfoService.userLoggedIn = true;

            this.storage.store('jwtToken', token['token']);

            this.messageService.add({severity: 'success', summary: `Welcome back ${decodedToken['username']}`});
            this.router.navigate(['/home']);
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Login Error',
              detail: 'Invalid credentials provided!'
            });
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Login Error',
            detail: error
          });
        }
      });
  }
}
