import {Timing} from "./timing";
import {TimeTable} from "./time-table";
import {CourseSession} from "./course-session";

export class RoomTable {
  id!: number;
  capacity?: number;
  computersAvailable?: boolean;
  assignedCourseSessions?: CourseSession[];
  timingConstraints?: Timing[];
  roomId?: string;
  timeTable?: TimeTable;
}
