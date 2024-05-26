import {Injectable} from '@angular/core';
import {TimeTable} from "../../assets/Models/time-table";

@Injectable({
  providedIn: 'root'
})
export class TableShareService {
  private selectedTable!: TimeTable;

  constructor() {
  }

  setSelectedTable(table: TimeTable) {
    this.selectedTable = table;
  }

  getSelectedTable(): TimeTable {
    return this.selectedTable;
  }
}
