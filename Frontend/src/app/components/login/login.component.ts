import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {LocalStorageService} from "ngx-webstorage";
import * as jwt_decode from 'jwt-decode';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginObj: any = {
    name: "",
    password: ""
  }

  constructor(
    private router: Router,
    private http: HttpClient,
    private storage: LocalStorageService,
    private messageService: MessageService,
  ) {
  }

  login() {
    this.http.post('/proxy/auth/login', this.loginObj)
      .subscribe((token: any) => {
        if (token['token'] != 'null') {
          const decodedToken = jwt_decode.jwtDecode(token['token']) as { [key: string]: any };

          this.storage.store('username', decodedToken['username']);
          this.storage.store('roles', decodedToken['role']);
          this.storage.store('jwtToken', token['token']);


          this.messageService.add({severity: 'success', summary: `Willkommen zurück ${decodedToken['username']}`});
          this.router.navigate(['/home'])
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Login Error',
            detail: 'Falscher Username oder Passwort'
          });

        }
      }).unsubscribe();
  }

}
