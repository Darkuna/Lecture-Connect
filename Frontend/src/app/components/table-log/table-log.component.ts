import {Component, OnInit} from '@angular/core';
import {GlobalTableChangeDTO} from "../../../assets/Models/dto/global-table-change-dto";
import {TableLogService} from "../../services/table-log.service";
import {TimeTableNames} from "../../../assets/Models/time-table-names";
import {Observable} from "rxjs";

@Component({
  selector: 'app-table-log',
  templateUrl: './table-log.component.html',
  styleUrl: './table-log.component.css'
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

  private loadChanges(): void {
    if (this.changes == null) {
      this.changes = this.logService.getTimeTableLogs();
    }
  }

  showChanges(){
    this.loadChanges();
    this.visible = true;
  }

  closeView(){
    this.visible = false;
  }

  ngOnInit(): void {
    this.cols = [
      {field: 'changeType', header: 'Type'},
      {field: 'description', header: 'Description'},
      {field: 'changeUser', header: 'Made by'}
    ];
  }
}
