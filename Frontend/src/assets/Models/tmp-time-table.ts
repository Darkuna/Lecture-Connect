import {TimeTableNames} from "./time-table-names";
import {Room} from "./room";
import {Course} from "./course";

export class TmpTimeTable {
  tableName!: TimeTableNames;
  courseTable!: Course[]
  roomTables!: Room[];
}
