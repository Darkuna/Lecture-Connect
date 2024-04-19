import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {ReloadService} from "../../services/reload.service";

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
  ) { }


  isLoggedIn(): boolean {
    return this.storage.retrieve('username') !== null; }

  hasAdminRole(): boolean {
    return this.storage.retrieve('roles') === 'ADMIN';
  }

  redirectToPage(page: string): void {
      this.router.navigate([page])
  }

  logout(): void {
    this.storage.clear()
    this.redirectToPage('/login')
  }

}
