import {Timing} from "./timing";
import {RoomTable} from "./room-table";

export class Room {
  id!: string;
  capacity?: number;
  computersAvailable?: boolean;
  timingConstraints?: Timing[];
  roomTables?: RoomTable[];
  createDate?: Date;
  updateDate?: Date;

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'capacity', header: 'Capacity'},
      {field: 'computersAvailable', header: 'Room has PCs'},
      {field: 'createDate', header: 'Creation Date'},
      {field: 'updateDate', header: 'last time updated'}
    ]
  }
}
