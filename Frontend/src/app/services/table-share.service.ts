import {Injectable} from '@angular/core';
import {TmpTimeTable} from "../../assets/Models/tmp-time-table";

@Injectable({
  providedIn: 'root'
})
export class TableShareService {
  private selectedTable: TmpTimeTable;

  constructor() {
    this.selectedTable = new TmpTimeTable();
  }

  setSelectedTable(table: TmpTimeTable) {
    this.selectedTable = table;
  }

  getSelectedTable(): TmpTimeTable {
    return this.selectedTable;
  }
}
