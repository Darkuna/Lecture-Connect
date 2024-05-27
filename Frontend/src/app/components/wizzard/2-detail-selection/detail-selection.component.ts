import {Component, Input} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";

@Component({
  selector: 'app-detail-selection',
  templateUrl: './detail-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class DetailSelectionComponent {
  @Input() globalTable!: TmpTimeTable;
  headers: any[];

  constructor(
  ) {
    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'semester', header: 'Semester'}
    ];
  }
}
