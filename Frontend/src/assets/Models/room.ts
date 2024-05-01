import {Timing} from "./timing";
import {RoomTable} from "./room-table";

export class Room {
  id!: string;
  capacity?: number;
  computersAvailable?: boolean;
  timingConstraints?: Timing[];
  roomTables?: RoomTable[];

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'capacity', header: 'Capacity'},
      {field: 'computersAvailable', header: 'Room has PCs'}
    ]
  }
}
