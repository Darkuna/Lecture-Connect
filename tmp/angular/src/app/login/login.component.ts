import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  username: string ="";
  password: string ="";

  constructor(private router: Router,private http: HttpClient) {}

  login() {
    console.log(this.username);
    console.log(this.password);

    let bodyData = {
      email: this.username,
      password: this.password,
    };

    this.http.post("http://localhost:8080/login", bodyData).subscribe(  (resultData: any) => {
      console.log(resultData);

      if (resultData.message == "Email not exits")
      {

        alert("Email not exits");


      }
      else if(resultData.message == "Login Success")

      {
        this.router.navigateByUrl('/home');
      }
      else
      {
        alert("Incorrect Email and Password not match");
      }
    });
  }

}
