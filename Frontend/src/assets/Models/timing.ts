import {RoomTable} from "./room-table";
import {Course} from "./course";
import {Day} from "./enums/day";

export class Timing {
  id!: number;
  startTime?: Date;
  endTime?: Date;
  day?: Day;
  course?: Course;
  roomTable?: RoomTable
}
