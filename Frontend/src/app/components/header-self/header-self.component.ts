import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {ReloadService} from "../../services/reload.service";

@Component({
  selector: 'app-header-self',
  templateUrl: './header-self.component.html',
  styleUrl: './header-self.component.css'
})
export class HeaderSelfComponent implements OnInit {
  pageTitle = "Lecture Connect"
  loginStatus = false;
  adminStatus = false;


  constructor(
    private router: Router,
    private storage: LocalStorageService,
    private reloadService: ReloadService
  ) { }

  ngOnInit(): void {
    this.reloadService.currentMessageSubscriber.subscribe((data : any)=>{
      if( data.isRefresh ) {
        this.loginStatus = this.isLoggedIn();
        this.adminStatus = this.hasAdminRole();
      }
    })
  }


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
