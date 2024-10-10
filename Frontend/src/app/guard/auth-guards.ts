import {CanActivateFn, CanDeactivateFn} from '@angular/router';
import {inject} from "@angular/core";
import {LoginUserInfoService} from "../services/login-user-info.service";
import {EditorComponent} from "../components/editor/editor.component";
import {WizardComponent} from "../components/wizzard/wizard.component";

export const AuthGuardLogin: CanActivateFn = () => {
  const userService:LoginUserInfoService = inject(LoginUserInfoService);
  return userService.isLoggedIn();
};

export const AuthGuardEditorClose: CanDeactivateFn<any> = (component: EditorComponent) => {
  return component.canDeactivate();
};

export const AuthGuardWizardClose: CanDeactivateFn<any> = (component: WizardComponent) => {
  return component.canDeactivate();
};

export const AuthGuardAdmin: CanActivateFn = () => {
  const userService:LoginUserInfoService = inject(LoginUserInfoService);
  return userService.hasAdminRole();
};
