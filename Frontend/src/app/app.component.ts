import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  @HostListener("window:onbeforeunload", ["$event"])
  clearLocalStorage(){
    localStorage.clear();
}
}
