export interface TableData{
  getTableColumns(): any[];
  getData(): any[];
  saveItem(val: any): any;
  editItem(val: any): any;
  deleteSingleItem(val: any): void;
  deleteMultipleItems(val: any[]): void;
}
