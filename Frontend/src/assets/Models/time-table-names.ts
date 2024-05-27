import {Semester} from "./enums/semester";
import {tableStatus} from "./enums/table-status";

export class TimeTableNames {
  id!: number;
  semester?: Semester;
  year?: number;
  status: tableStatus = tableStatus.NEW;
}
