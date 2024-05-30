import {Injectable} from '@angular/core';
import {SessionStorageService} from "ngx-webstorage";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService {
  userLoggedIn: boolean = false;

  constructor(
    private sessionStorageService: SessionStorageService,
  ) {
    this.userLoggedIn = !!sessionStorageService.retrieve("name");
  }

  get username(): string {
    return this.sessionStorageService.retrieve("name") || "";
  }

  set username(value: string) {
    this.sessionStorageService.store("name", value);
  }

  get userRole(): string {
    return this.sessionStorageService.retrieve("role") || "";
  }

  set userRole(value: string) {
    this.sessionStorageService.store("role", value.at(0));
  }
}
