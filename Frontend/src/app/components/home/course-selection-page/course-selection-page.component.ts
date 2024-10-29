import { Component } from '@angular/core';
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {CourseUpdaterService} from "../../../services/course-updater.service";

@Component({
  selector: 'app-course-selection-page',
  templateUrl: './course-selection-page.component.html',
  styleUrl: '../home.component.css'
})
export class CourseSelectionPageComponent {
  currentPageIndex: number = 0;

  constructor(
    private messageService: MessageService,
    protected updaterService: CourseUpdaterService,
    private router: Router
  ) {
  }

  updateData():void {

  };

  goToHomeScreen():void {
    this.router.navigate(['/home']).catch(message => {
      this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
    });
  }

  showLastChanges():void {

  }

  saveGlobal() {

  }

  getColorBasedOnIndex(type: string, index: number): string {
    if (index > this.currentPageIndex) {
      return '#CDCDCC';
    }

    if (type === 'i') {
      switch (index) {
        case 0:
          return '#9EC252';
        case 1:
          return '#75CF84';
        case 2:
          return '#75CFCA';
        case 3:
          return '#75A9CF';
        default:
          return '#CDCDCC';
      }
    } else {
      return '#070707';
    }
  }
}
