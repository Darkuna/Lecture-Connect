import { Component } from '@angular/core';
import {GlobalTableService} from "../../../services/global-table.service";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../../../assets/Models/dto/time-table-dto";
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {Status} from "../../../../assets/Models/enums/status";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-room-selection-page',
  templateUrl: './room-selection-page.component.html',
  styleUrl: '../home.component.css'
})
export class RoomSelectionPageComponent {
  currentTable: Observable<TimeTableDTO> | null;
  selectedTable!: TmpTimeTable;
  currentPageIndex: number = 0;

  constructor(
    private globalTableService: GlobalTableService,
    private localStorage: LocalStorageService,
    private messageService: MessageService
  ) {
    this.currentTable = this.globalTableService.currentTimeTable;
    this.selectedTable = new TmpTimeTable();
    this.selectedTable.status = Status.NEW;
  }

  updateData():void {

  };

  goToHomeScreen():void {

  }

  showLastChanges():void {

  }

  saveLocal() {
    this.selectedTable.currentPageIndex = this.currentPageIndex;
    this.selectedTable.status = Status.LOCAL;
    this.localStorage.store('tmptimetable', this.selectedTable);
    this.messageService.add({
      severity: 'info',
      summary: 'Info',
      detail: 'The Table is only cached locally'
    });
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
}
