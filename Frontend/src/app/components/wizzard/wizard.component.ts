import {Component} from '@angular/core';
import {TimeTable} from "../../../assets/Models/time-table";
import {TableShareService} from "../../services/table-share.service";

interface InfoDialog {
  header: string;
  infoText: string;
  subTextHeader: string;
  subText: string;
}

@Component({
  selector: 'app-wizard',
  templateUrl: './wizard.component.html',
  styleUrl: './wizard.component.css'
})
export class WizardComponent {
  selectedTable!: TimeTable;
  active: number = 0;
  dialog: boolean = false;
  currentDialog: InfoDialog;
  InfoDialogOptions: InfoDialog[];

  constructor(
    private shareService: TableShareService,
  ) {
    this.selectedTable = this.shareService.getSelectedTable();
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

}
