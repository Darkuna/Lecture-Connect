import {Component} from '@angular/core';
import {TableShareService} from "../../services/table-share.service";
import {Status} from "../../../assets/Models/enums/status";
import {TmpTimeTable} from "../../../assets/Models/tmp-time-table";
import {InfoDialogInterface} from "../../../assets/Models/interfaces/info-dialog-interface";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-wizard',
  templateUrl: './wizard.component.html',
  styleUrl: './wizard.component.css'
})
export class WizardComponent {
  selectedTable!: TmpTimeTable;

  active: number = 0;
  dialog: boolean = false;
  currentDialog: InfoDialogInterface;
  InfoDialogOptions: InfoDialogInterface[];

  constructor(
    private shareService: TableShareService,
    private localStorage: LocalStorageService,
    private messageService: MessageService,
    private router: Router,
  ) {
    this.selectedTable = this.shareService.selectedTable;
    this.active = this.selectedTable.currentPageIndex;
    this.InfoDialogOptions = [
      {
        header: 'Choose Courses',
        infoText: 'Here you select all the courses that will be held this semester. Even if you have forgotten certain courses, you can add or remove others later.',
        subTextHeader: 'Good to know!',
        subText: 'Changing the course data (number, name, visitors, ...) is only possible in the top menu under the Courses tab.'
      },
      {
        header: 'Define Details',
        infoText: 'This overview is used to determine whether courses are split. In the case of an introductory seminar, the toggle switch should be set to True. Furthermore, the number of introductory seminars must be specified so that a corresponding number can be generated.',
        subTextHeader: 'Example',
        subText: 'The VO Operating Systems lasts 3 hours. The VO is planned for a first part of 2 hours and a second part of one hour.'
      },
      {
        header: 'Choose Rooms',
        infoText: 'Here you select all the rooms that will be held for this semester. Even if you have forgotten certain rooms, you can add or remove others later.',
        subTextHeader: 'Good to know!',
        subText: 'Changing the room data (number, name, seats, ...) is only possible in the top menu under the Rooms tab.'
      },
      {
        header: 'Define Details',
        infoText: 'Finally, the fixed times must be determined for the respective subject of study.',
        subTextHeader: 'Important!',
        subText: 'To ensure the accuracy and efficiency of the algorithm, all timeslots (computer science, architecture, mathematics...) must be entered. Otherwise it can happen that a course is placed in a timeslot that is actually blocked.'
      }
    ];
    this.currentDialog = this.InfoDialogOptions[this.active];
  }

  checkIfCoursesSelected(): boolean{
    return this.selectedTable.courseTable === undefined || this.selectedTable.courseTable.length == 0;
  }

  checkIfRoomsSelected(): boolean{
    return this.selectedTable.roomTables === undefined || this.selectedTable.roomTables.length == 0;
  }


  getColorBasedOnIndex(type: string, index: number): string {
    if (index > this.active) {
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

  showDialog(): void {
    this.dialog = true;
  }

  getTextFromEnum(): string {
    switch (this.selectedTable.status) {
      case Status.NEW:
        return "NEW";
      case Status.EDITED:
        return "EDITED";
      case Status.FINISHED:
        return "FINISHED";
      case Status.IN_WORK:
        return "IN WORK";
      case Status.LOCAL:
        return "LOCAL SAVE";
      default:
        return "DEFAULT";
    }
  }

  saveLocal() {
    this.selectedTable.currentPageIndex = this.active;
    this.selectedTable.status = Status.LOCAL;
    this.localStorage.store('tmptimetable', this.selectedTable);
    this.messageService.add({severity: 'info', summary: 'Info', detail: 'The Table is only cached locally'});
  }

  closeWizard() {
    this.saveLocal();
    this.router.navigate(['/home'])
      .catch(message => {
        this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
      }
    );
  }
}
