import {Semester} from "./enums/semester";
import {RoomTable} from "./room-table";
import {CourseSession} from "./course-session";
import {Status} from "./enums/status";

export class TimeTable {
  id!: number;
  semester?: Semester;
  year?: number;
  status?: Status;
  roomTables?: RoomTable[];
  courseSessions?: CourseSession[];
  createdAt?: Date;
  updatedAt?: Date;
}
