import {RoomTable} from "./room-table";
import {Course} from "./course";
import {Day} from "./enums/day";

export class Timing {
  id!: number;
  startTime?: number[];
  endTime?: number[];
  day?: string;
}
