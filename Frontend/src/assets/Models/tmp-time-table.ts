import {TimeTableNames} from "./time-table-names";
import {Room} from "./room";
import {Course} from "./course";
import {EventInput} from "@fullcalendar/core";
import {createEventId} from "../../app/components/home/event-utils";

let eventGuid = 0;
const TODAY_STR = new Date().toISOString().replace(/T.*$/, ''); // YYYY-MM-DD of today

export class TmpTimeTable {
  currentPageIndex: number = 0;
  tableName!: TimeTableNames;
  courseTable!: Course[]
  roomTables!: Room[];
  selectedTimes: EventInput[];

  constructor() {
    this.selectedTimes = [
      {
        id: createEventId(),
        title: 'Timed event',
        start: TODAY_STR + 'T00:00:00',
        end: TODAY_STR + 'T03:00:00'
      },
      {
        id: createEventId(),
        title: 'Timed event',
        start: TODAY_STR + 'T12:00:00',
        end: TODAY_STR + 'T15:00:00'
      }
    ];
  }
}
