import {Component} from '@angular/core';
import {Router} from "@angular/router";
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
    private userInfoService: LoginUserInfoService,
  ) {
  }

  logout(){
    this.userInfoService.logout();
  }

  isLoggedIn(): boolean {
    return this.userInfoService.userIsLoggedIn();
  }

  hasAdminRole(): boolean {
    return this.userInfoService.hasAdminRole();
  }

  redirectToPage(page: string): void {
    this.router.navigate([page]);
  }
}
