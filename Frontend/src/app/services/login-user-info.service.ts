import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import * as jwt_decode from "jwt-decode";
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
    let token = localStorage.getItem('jwt-token') ?? sessionStorage.getItem('jwt-token');

    if (token) {
      const decodedToken = jwt_decode.jwtDecode(token) as { [key: string]: string | string[] };
      console.log(decodedToken);
      const roles =  <string[]>decodedToken['role'];

      return !!roles!.find(t => t == 'ADMIN');
    }
    return false;
  }

  isLoggedIn():boolean{
    return localStorage.getItem('jwt-token') !== null || sessionStorage.getItem('jwt-token') !== null;
  }
}
