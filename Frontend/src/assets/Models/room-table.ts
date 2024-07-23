import {Timing} from "./timing";
import {Room} from "./room";
import {TimeTable} from "./time-table";
import {CourseSession} from "./course-session";

export class RoomTable {
  id!: number;
  assignedCourseSessions?: CourseSession[];
  timingConstraints?: Timing[];
  room?: Room;
  timeTable?: TimeTable;
}
