import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-header-self',
  templateUrl: './header-self.component.html',
  styleUrl: './header-self.component.css'
})
export class HeaderSelfComponent {
  pageTitle = "Lecture Connect"
  constructor(private router: Router) {
  }

  shouldShowButtons(): boolean {
    // Check if the current route is '/home'
    return this.router.url === '/home';
  }
}
