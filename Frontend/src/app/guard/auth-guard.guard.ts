import { CanActivateFn } from '@angular/router';
import {inject} from "@angular/core";
import {LoginUserInfoService} from "../services/login-user-info.service";

export const AuthGuardLogin: CanActivateFn = () => {
  const userService:LoginUserInfoService = inject(LoginUserInfoService);
  return userService.userIsLoggedIn();
};

export const AuthGuardEditorClose: CanActivateFn = () => {
  return !!localStorage.getItem('ngx-webstorage|jwt-token');
};

export const AuthGuardWizardClose: CanActivateFn = () => {
  return !!localStorage.getItem('ngx-webstorage|jwt-token');
};

export const AuthGuardAdmin: CanActivateFn = () => {
  const userService:LoginUserInfoService = inject(LoginUserInfoService);
  return userService.hasAdminRole();
};
