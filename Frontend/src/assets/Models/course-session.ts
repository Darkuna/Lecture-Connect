import {Timing} from "./timing";
import {Course} from "./course";
import {TimeTable} from "./time-table";
import {RoomTable} from "./room-table";

export class CourseSession {
  id!: number;
  name!: string;
  isAssigned?: boolean;
  isFixed?: boolean;
  duration?: number;
  timingConstraints?: Timing[];
  timing?: Timing;
  roomTable?: RoomTable;
}
