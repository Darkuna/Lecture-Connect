import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {RoomInterface} from "./interfaces/roomInterface";

export class Room implements RoomInterface {
  id!: string;
  capacity!: number;
  computersAvailable?: boolean;
  timingConstraints?: Timing[];
  tmpCalendarTimes: any[] = [];
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
