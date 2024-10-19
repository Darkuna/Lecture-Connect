import {CanActivateFn, CanDeactivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LoginUserInfoService} from "../services/login-user-info.service";
import {EditorComponent} from "../components/editor/editor.component";
import {WizardComponent} from "../components/wizzard/wizard.component";

export const AuthGuardRemember: CanActivateFn = () => {
  const userService: LoginUserInfoService = inject(LoginUserInfoService);
  const router: Router = inject(Router);
  if (userService.isLoggedIn()) {
    router.navigate(['/user/home']);
    return false;
  }
  else { return true; }
};


export const AuthGuardLogin: CanActivateFn = () => {
  return true;
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
