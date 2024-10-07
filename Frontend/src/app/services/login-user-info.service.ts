import {Injectable,} from '@angular/core';
import {tap} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

interface LoginObj {
  name: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService{
  static API_PATH = "/proxy/auth/login";

  constructor(
    private http: HttpClient,
  ) { }

  login(data: any, stayLogin: boolean) {
    return  this.http.post(LoginUserInfoService.API_PATH, data)
      .pipe(tap((result) => {
        if(stayLogin){
          localStorage.setItem('authUser', JSON.stringify(result));
        } else {
          sessionStorage.setItem('authUser', JSON.stringify(result));
        }
      }));
  }

  logout(){
    localStorage.removeItem('authUser');
  }

  isLoggedIn(): boolean{
    return localStorage.getItem('authUser') !== null;
  }

  hasUserRole():boolean{
    return localStorage.getItem('authUser') !== null;
  }

  hasAdminRole():boolean{
    return localStorage.getItem('authUser') !== null;
  }
}
