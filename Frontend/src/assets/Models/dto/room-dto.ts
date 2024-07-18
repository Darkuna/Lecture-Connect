import {Timing} from "../timing";
import {RoomTable} from "../room-table";

export class RoomDTO {
  id!: string;
  capacity!: number;
  computersAvailable?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  timingConstraints?: Timing[];
}
