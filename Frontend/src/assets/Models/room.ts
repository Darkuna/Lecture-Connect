import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {TableData} from "./interfaces/TableData";
import {TableVariables} from "./table-variables";

export class Room implements TableData {
  id?: string;
  private capacity?: number;
  private computersAvailable?: boolean;
  private timingConstraints?: Timing[];
  private roomTables?: RoomTable[];

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'capacity', header: 'Capacity'},
      {field: 'computersAvailable', header: 'Room has PCs'}
    ]
  }

  deleteMultipleItems(val: any[]): void {
  }

  deleteSingleItem(val: any): void {
  }

  editItem(val: any): any {
  }

  getData(): any[] {
    return [];
  }

  saveItem(val: TableVariables): any {
    let tmp = new Room();
    tmp.timingConstraints = val.timingConstraintsRoom;
    tmp.computersAvailable = val.computersAvailable;
    tmp.roomTables = val.roomTables;
    tmp.capacity = val.capacity;
    tmp.id = val.idRoom;
    return tmp;
  }

  uploadItemToBackend(val: any): void {
  }

  equals(other: Room): boolean {
    return this.id !== other.id;
  }

  isInList(item: Room, items: Room[]): boolean {
    for (const listItem of items) {
      // Check if items are of the same type
      if (item.constructor !== listItem.constructor) {
        continue;
      }

      // Check if the item exists in the list based on its id
      if (item.id && listItem.id && item.id === listItem.id) {
        return true;
      }
    }
    return false;
  }


}
