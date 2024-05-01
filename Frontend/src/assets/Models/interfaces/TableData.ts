import {Userx} from "../userx";
import {Room} from "../room";
import {Course} from "../course";

export interface TableData {
  getTableColumns(): any[];

  getData(): any[];

  editItem(val: any): any;

  saveItem(val: any): any;

  uploadItemToBackend(val: any): void;

  deleteSingleItem(val: any): void;

  deleteMultipleItems(val: any[]): void;

  isInList(item: Userx | Room | Course, items: Userx[] | Room[] | Course[]): boolean;
}
