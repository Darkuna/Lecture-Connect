import {Room} from "./room";
import {Course} from "./course";
import {EventInput} from "@fullcalendar/core";
import {Semester} from "./enums/semester";
import {Status} from "./enums/status";

export class TmpTimeTable {
  currentPageIndex: number = 0;
  id!: number;
  semester?: Semester;
  year?: number;
  status: Status = Status.NEW;
  courseTable!: Course[];
  roomTables!: Room[];
}
