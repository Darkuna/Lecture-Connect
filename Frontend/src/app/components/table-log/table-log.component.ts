import {Component, OnInit} from '@angular/core';
import {TableLogService} from "../../services/table-log.service";
import {getChangeTypes} from "../../../assets/Models/enums/change-type";

@Component({
  selector: 'app-table-log',
  templateUrl: './table-log.component.html',
  styleUrl: './table-log.component.css',
})
export class TableLogComponent implements OnInit{
  cols: any[] = [];

  constructor(
    protected logService: TableLogService,
  ) {}

  ngOnInit(): void {
    this.cols = [
      {field: 'changeType', header: 'Type'},
      {field: 'description', header: 'Description'},
      {field: 'changeUser', header: 'Made by'},
      {field: 'date', header: 'Date'}
    ];
  }

  closeView(){
    this.logService.showChangeDialog = false;
  }

  protected readonly getChangeTypes = getChangeTypes;
}
