import { Component } from '@angular/core';
import {GlobalTableService} from "../../../services/global-table.service";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../../../assets/Models/dto/time-table-dto";

@Component({
  selector: 'app-room-selection-page',
  templateUrl: './room-selection-page.component.html',
  styleUrl: './room-selection-page.component.css'
})
export class RoomSelectionPageComponent {
  currentTable: Observable<TimeTableDTO> | null;

  constructor(
    private globalTableService: GlobalTableService
  ) {
    this.currentTable = this.globalTableService.currentTimeTable;
  }
}
