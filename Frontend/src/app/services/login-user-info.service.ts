import {Injectable} from '@angular/core';
import {SessionStorageService} from "ngx-webstorage";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService {
  userLoggedIn: boolean = false;
  private _username: string | null = null;
  private _role: string | null = null;
  constructor(
    private sessionStorageService: SessionStorageService,
  ) {
    this.userLoggedIn = !!sessionStorageService.retrieve("name");
  }

  get username(): string {
    return this._username || "";
  }

  set username(value: string) {
    this.sessionStorageService.store("name", value);
    this._username = value;
  }

  get role(): string {
    this.sessionStorageService.retrieve("role");
    return this._role || "";
  }

  set role(value: string) {
    this.sessionStorageService.store("role", value);
    this._role = value;
  }
}
