import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {RoomInterface} from "./interfaces/roomInterface";
import {Subject} from "rxjs";

export class Room implements RoomInterface {
  id!: string;
  capacity!: number;
  computersAvailable?: boolean;
  timingConstraints?: Timing[];
  tmpEvents?: Subject<any[]>;
  roomTables?: RoomTable[];
  createdAt?: Date;
  updatedAt?: Date;

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'capacity', header: 'Capacity'},
      {field: 'computersAvailable', header: 'Room has PCs'},
      {field: 'createdAt', header: 'Creation Date'},
      {field: 'updatedAt', header: 'last time updated'}
    ]
  }
}
