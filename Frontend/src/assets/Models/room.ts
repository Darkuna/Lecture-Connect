import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {TableData} from "./interfaces/TableData";

export class Room implements TableData {
  id?: string;
  private capacity?: number;
  private computersAvailable?: boolean;
  private timingConstraints?: Timing[];
  private roomTables?: RoomTable[];

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id' },
      {field: 'capacity', header: 'Capacity' },
      {field: 'computersAvailable', header: 'Room has PCs' }
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

  saveItem(val: any): any {
  }

  uploadItemToBackend(val: any): void {
  }



}
