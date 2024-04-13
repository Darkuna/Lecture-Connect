import {RoomTable} from "./room-table";
import {Course} from "./course";
import {Day} from "./enums/day";

export class Timing {
  constructor(
    private id: number,
    private startTime: Date,
    private endTime: Date,
    private day: Day,
    private course: Course,
    private roomTable: RoomTable
  ) {
  }
}
