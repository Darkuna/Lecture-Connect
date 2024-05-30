import {Injectable} from '@angular/core';
import {SessionStorageService} from "ngx-webstorage";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService {
  private _username: string = "username";
  private _userRole: string = "userRole";
  userLoggedIn: boolean = false;

  constructor(
    private sessionStorageService: SessionStorageService,
  ) {
    this.userLoggedIn = !!sessionStorageService.retrieve(this._username);
    this._username = this.username;
    this._userRole = this.userRole;
  }

  get username(): string {
    return this.sessionStorageService.retrieve(this._username) || "";
  }

  set username(value: string) {
    this.sessionStorageService.store(this._username, value);
  }

  get userRole(): string {
    return this.sessionStorageService.retrieve(this._userRole) || "";
  }

  set userRole(value: string) {
    this.sessionStorageService.store(this._userRole, value);
  }
}
