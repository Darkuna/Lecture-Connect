import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {LocalStorageService, SessionStorageService} from "ngx-webstorage";
import {LoginUserInfoService} from "../../services/login-user-info.service";

@Component({
  selector: 'app-page-not-found-component',
  templateUrl: './page-not-found-component.component.html',
  styleUrl: './page-not-found-component.component.css'
})
export class PageNotFoundComponentComponent {
  constructor(
    private router: Router,
    private storage: LocalStorageService,
    private userInfoService: LoginUserInfoService,
    private sessionStorageService: SessionStorageService,
  ) {
  }

  redirectToLoginPage(){
    this.userInfoService.userLoggedIn = false;
    this.storage.clear();
    this.sessionStorageService.clear();
    this.router.navigate(['/login']).then();
  }
}
