import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import * as decoder from "jwt-decode";
import {tap} from "rxjs";
import {environment} from "../../enviroments/environment";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService {
  static API_PATH = `${environment.baseUrl}auth/login`;

  constructor(
    private http: HttpClient,
  ) { }

  loginUser(loginObj: any, remember: boolean){
    return this.http.post(`${LoginUserInfoService.API_PATH}`, loginObj)
      .pipe(tap((result) => {
        (remember ? localStorage : sessionStorage)
          .setItem("jwt-token", JSON.stringify(result));
      }))
  }

  clearCache(){
    localStorage.removeItem('jwt-token');
    sessionStorage.removeItem('jwt-token');
  }

  hasAdminRole():boolean{
    let token = localStorage.getItem('jwt-token') ?? sessionStorage.getItem('jwt-token');

    if (token) {
      const decodedToken = decoder.jwtDecode(token) as { [key: string]: string | string[] };
      const roles =  <string[]>decodedToken['role'];

      return !!roles!.find(t => t == 'ADMIN');
    }
    return false;
  }

  isLoggedIn():boolean{
    return localStorage.getItem('jwt-token') !== null || sessionStorage.getItem('jwt-token') !== null;
  }
}
