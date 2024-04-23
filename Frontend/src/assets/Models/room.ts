import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {TableData} from "./interfaces/TableData";

export class Room implements TableData {

  constructor(
    private id?: string,
    private capacity?: number,
    private computersAvailable?: Boolean,
    private timingConstraints?: Timing[],
    private roomTables?: RoomTable[]
  ) { }

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id' },
      {field: 'capacity', header: 'Capacity' },
      {field: 'computersAvailable', header: 'Room has PCs' }
    ]
  }

}
