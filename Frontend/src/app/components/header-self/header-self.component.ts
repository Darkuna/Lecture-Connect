import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService, SessionStorageService} from "ngx-webstorage";
import {LoginUserInfoService} from "../../services/login-user-info.service";

@Component({
  selector: 'app-header-self',
  templateUrl: './header-self.component.html',
  styleUrl: './header-self.component.css'
})
export class HeaderSelfComponent {
  pageTitle = "Lecture Connect"

  constructor(
    private router: Router,
    private storage: LocalStorageService,
    private userInfoService: LoginUserInfoService,
    private sessionStorageService: SessionStorageService,
  ) {
  }

  isLoggedIn(): boolean {
    return this.userInfoService.isLoggedIn();
  }

  hasAdminRole(): boolean {
    return this.userInfoService.hasAdminRole();
  }

  redirectToPage(page: string): void {
    this.router.navigate([page]);
  }

  logout(): void {
    this.userInfoService.logoutUser();
    this.sessionStorageService.clear();
    this.redirectToPage('/login');
  }
}
