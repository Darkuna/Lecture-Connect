import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-page-not-found-component',
  templateUrl: './page-not-found-component.component.html',
  styleUrl: './page-not-found-component.component.css'
})
export class PageNotFoundComponentComponent {
  constructor(
    private router: Router
  ) {
  }

  redirectToLoginPage(){
    this.router.navigate(['/login']);
  }
}
