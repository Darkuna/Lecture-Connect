import {Injectable} from '@angular/core';
import {TmpTimeTable} from "../../assets/Models/tmp-time-table";

@Injectable({
  providedIn: 'root'
})
export class TableShareService {
  private _selectedTable: TmpTimeTable;

  constructor() {
    this._selectedTable = new TmpTimeTable();
  }

  get selectedTable(): TmpTimeTable {
    return this._selectedTable;
  }

  set selectedTable(value: TmpTimeTable) {
    this._selectedTable = value;
  }
}
