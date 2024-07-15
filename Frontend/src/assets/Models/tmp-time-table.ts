import {TimeTableNames} from "./time-table-names";
import {Room} from "./room";
import {Course} from "./course";
import {EventInput} from "@fullcalendar/core";

let eventGuid = 0;
const TODAY_STR = new Date().toISOString().replace(/T.*$/, ''); // YYYY-MM-DD of today

export class TmpTimeTable {
  currentPageIndex: number = 0;
  tableName!: TimeTableNames;
  courseTable!: Course[]
  roomTables!: Room[];
  selectedTimes: EventInput[] = [];

  constructor() {

  }
}
