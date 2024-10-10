import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService implements OnDestroy{
  static API_PATH = "/proxy/auth/login";

  constructor(
    private http: HttpClient,
  ) { }

  ngOnDestroy(): void {

  }

  loginUser(loginObj: any, remember: boolean){
    return this.http.post(`${LoginUserInfoService.API_PATH}`, loginObj)
      .pipe(tap((result) => {
        console.log('result: '+ result + '\nJSON ' + JSON.stringify(result));
        if (remember)
          localStorage.setItem("jwt-token", JSON.stringify(result));
        else
          sessionStorage.setItem("jwt-token", JSON.stringify(result));
      }))
  }

  logoutUser(){
    localStorage.removeItem('jwt-token');
    sessionStorage.removeItem('jwt-token');
  }

  hasAdminRole():boolean{
    return false;
  }

  isLoggedIn():boolean{
    return localStorage.getItem('jwt-token') !== null || sessionStorage.getItem('jwt-token') !== null;
  }
}
