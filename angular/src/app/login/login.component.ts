import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginObj: any = {
    name: "",
    password: ""
  }

  constructor(
    private router: Router,
    private http: HttpClient,
  ) {
  }

  login() {
    this.http.post('http://localhost:8080/api/login', this.loginObj)
      .subscribe((res: any) => {
        if (res.result) {
          alert('login was successful')
          //localStorage.setItem('loginToken', res.data.token)
          this.router.navigateByUrl('/home');
        } else {
          alert(res.result)
        }
      })
  }
}
