import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService {
  private _username: string = "";
  private _userRole: string = "";
  userLoggedIn: boolean = false;


  constructor() { }

  get username(): string {
    return this._username;
  }
  set username(value: string) {
    this._username = value;
  }


  get userRole(): string {
    return this._userRole;
  }

  set userRole(value: string) {
    this._userRole = value;
  }
}
