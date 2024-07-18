import {Room} from "../room";
import {Course} from "../course";
import {Semester} from "../enums/semester";
import {Status} from "../enums/status";
import {RoomDTO} from "./room-dto";

export class TmpTimeTableDTO {
  semester?: Semester;
  year?: number;
  status: Status | string = Status.NEW;
  courses!: Course[];
  rooms!: RoomDTO[];
}
