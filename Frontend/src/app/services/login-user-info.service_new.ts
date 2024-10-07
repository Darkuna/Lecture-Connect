import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoServiceNew implements OnDestroy{
  static API_PATH = "/proxy/auth/login";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
  ) { }


  ngOnDestroy(): void {
  }

  login(loginObj: any):Observable<any>{
    return this.http.post(LoginUserInfoServiceNew.API_PATH, loginObj, this.httpOptions);
  }

  logout(){
    localStorage.removeItem('authUser');
  }

  isLoggedIn(): boolean{
    return localStorage.getItem('authUser') !== null;
  }

  hasUserRole(){

  }

  hasAdminRole(){

  }
}
