import {Semester} from "./enums/semester";
import {Status} from "./enums/status";

export class TimeTableNames {
  id!: number;
  semester?: Semester;
  year?: number;
  status: Status = Status.NEW;
}
