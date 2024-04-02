import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private usersUrl: string;
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  save(name: String, password: String) {
    let bodyData = {
      "name" : name,
      "password" : password
    };


    this.http.post(this.usersUrl, bodyData)
      alert("Employee Registered Successfully");
  }
}
