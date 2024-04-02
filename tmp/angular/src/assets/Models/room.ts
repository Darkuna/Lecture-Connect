import {Timing} from "./timing";
import {RoomTable} from "./room-table";

export class Room {
  constructor(
    private id: string,
    private capacity: number,
    private computersAvailable: Boolean,
    private timingConstraints: Timing[],
    private roomTables: RoomTable[]
  ) {
  }
}
