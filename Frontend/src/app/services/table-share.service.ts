import {Injectable} from '@angular/core';
import {TmpTimeTable} from "../../assets/Models/tmp-time-table";
import {CourseDTO} from "../../assets/Models/dto/course-dto";
import {Course} from "../../assets/Models/course";

@Injectable({
  providedIn: 'root'
})
export class TableShareService {
  private _selectedTable: TmpTimeTable;
  private _initialCourses: Course[] = [];

  constructor() {
    this._selectedTable = new TmpTimeTable();
  }

  get selectedTable(): TmpTimeTable {
    return this._selectedTable;
  }

  set selectedTable(value: TmpTimeTable) {
    this._selectedTable = value;
  }


  get initialCourses(): Course[] {
    return this._initialCourses;
  }

  set initialCourses(value: Course[]) {
    this._initialCourses = value;
  }
}
