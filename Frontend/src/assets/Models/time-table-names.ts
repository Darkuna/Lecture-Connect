import {Semester} from "./enums/semester";
import {RoomTable} from "./room-table";
import {CourseSession} from "./course-session";
import {tableStatus} from "./enums/table-status";

export class TimeTableNames {
  id!: number;
  semester?: Semester;
  year?: number;
  status?: tableStatus;
}
