import {Timing} from "./timing";
import {Course} from "./course";
import {TimeTable} from "./time-table";
import {RoomTable} from "./room-table";

export class CourseSession {
  static BLOCKED: CourseSession;
  id!: number;
  name!: string;
  timingConstraints?: Timing[];
  isAssigned?: boolean;
  isFixed?: boolean;
  duration?: number;
  timing?: Timing;
  course?: Course;
  timeTable?: TimeTable;
  roomTable?: RoomTable;
}
