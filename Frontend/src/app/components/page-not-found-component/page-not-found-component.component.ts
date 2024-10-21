import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService, SessionStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-page-not-found-component',
  templateUrl: './page-not-found-component.component.html',
  styleUrl: './page-not-found-component.component.css'
})
export class PageNotFoundComponentComponent {
  constructor(
    private router: Router,
    private storage: LocalStorageService,
    private sessionStorageService: SessionStorageService,
  ) {
  }

  redirectToLoginPage(){
    this.storage.clear();
    this.sessionStorageService.clear();
    this.router.navigate(['/login']).then();
  }
}
