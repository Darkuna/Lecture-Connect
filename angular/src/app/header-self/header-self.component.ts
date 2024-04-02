import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-header-self',
  templateUrl: './header-self.component.html',
  styleUrl: './header-self.component.css'
})
export class HeaderSelfComponent {
  pageTitle = "Lecture Connect"

  constructor(
    private router: Router,
    private storage: LocalStorageService
  ) {
  }

  isLoggedIn(): boolean {
    // Check if the current route is '/home'
    return this.router.url !== '/login';
  }

  hasAdminRole(): boolean {
    // Check if the current route is '/home'
    return true;
  }


  redirectToPage(page: string): void {
    this.router.navigate([page]);
  }

  logout(): void {
    this.storage.clear('username')
    this.storage.clear('isLoggedIn')
    this.redirectToPage('/logout')
  }
}
