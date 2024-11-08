import {Component, OnInit} from '@angular/core';
import {GlobalTableChangeDTO} from "../../../assets/Models/dto/global-table-change-dto";
import {TableLogService} from "../../services/table-log.service";
import {TimeTableNames} from "../../../assets/Models/time-table-names";
import {Observable} from "rxjs";
import {getChangeTypes} from "../../../assets/Models/enums/change-type";

@Component({
  selector: 'app-table-log',
  templateUrl: './table-log.component.html',
  styleUrl: './table-log.component.css',
})
export class TableLogComponent implements OnInit{
  tableName: TimeTableNames | null = null;
  visible: boolean = false;
  changes: Observable<GlobalTableChangeDTO[]> | null = null;
  cols: any[] = [];

  constructor(
    private logService: TableLogService,
  ) {
  }

  private loadChanges(id: number): void {
    this.changes = this.logService.getTimeTableLogs(id);
  }

  showChanges(id: number){
    this.loadChanges(id);
    this.visible = true;
  }

  closeView(){
    this.visible = false;
  }

  ngOnInit(): void {
    this.cols = [
      {field: 'changeType', header: 'Type'},
      {field: 'description', header: 'Description'},
      {field: 'changeUser', header: 'Made by'},
      {field: 'date', header: 'Date'}
    ];
  }

  protected readonly getChangeTypes = getChangeTypes;
}
