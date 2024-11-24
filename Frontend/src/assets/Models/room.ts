import {Timing} from "./timing";
import {EventImpl} from "@fullcalendar/core/internal";

export class Room {
  id!: string;
  capacity!: number;
  computersAvailable?: boolean;
  timingConstraints?: Timing[];
  tmpEvents?: EventImpl[];
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
