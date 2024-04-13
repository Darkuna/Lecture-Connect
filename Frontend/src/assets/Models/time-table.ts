import {Semester} from "./enums/semester";
import {RoomTable} from "./room-table";
import {CourseSession} from "./course-session";

export class TimeTable {
  constructor(
    private id: number,
    private semester: Semester,
    private year: number,
    private roomTables: RoomTable[],
    private courseSessions: CourseSession[]
  ) {
  }
}
