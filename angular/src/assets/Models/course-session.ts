import {Timing} from "./timing";
import {Course} from "./course";
import {TimeTable} from "./time-table";
import {RoomTable} from "./room-table";

export class CourseSession {

  static BLOCKED: CourseSession;

  constructor(
    private id: number,
    private timingConstraints: Timing[],
    private isAssigned: boolean,
    private isFixed: boolean,
    private duration: number,
    private timing: Timing,
    private course: Course,
    private timeTable: TimeTable,
    private roomTable: RoomTable
  ) {
  }
}
