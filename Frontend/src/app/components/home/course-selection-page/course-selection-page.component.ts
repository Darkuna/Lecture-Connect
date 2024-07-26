import { Component } from '@angular/core';
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../../../assets/Models/dto/time-table-dto";
import {GlobalTableService} from "../../../services/global-table.service";

@Component({
  selector: 'app-course-selection-page',
  templateUrl: './course-selection-page.component.html',
  styleUrl: './course-selection-page.component.css'
})
export class CourseSelectionPageComponent {
  currentTable: Observable<TimeTableDTO> | null;

  constructor(
    private globalTableService: GlobalTableService
  ) {
    this.currentTable = this.globalTableService.currentTimeTable;
  }
}
