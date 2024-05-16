import {Semester} from "./enums/semester";
import {RoomTable} from "./room-table";
import {CourseSession} from "./course-session";
import {tableStatus} from "./enums/table-status";

export class TimeTable {
  id!: number;
  semester?: Semester;
  year?: number;
  roomTables?: RoomTable[];
  courseSessions?: CourseSession[];
  status?: tableStatus;
}
