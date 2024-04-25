import {Role} from "./enums/role";
import {TableData} from "./interfaces/TableData";

export class Userx implements TableData {
  serialVersionUID?: number;
  username?: string;
  createDate?: Date;
  updateDate?: Date;
  password?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  enabled?: boolean;
  role?: Role

  public getTableColumns(): any[] {
    return [
      {field: 'serialVersionUID', header: 'Id' },
      {field: 'username', header: 'Username' },
      {field: 'firstName', header: 'First Name' },
      {field: 'lastName', header: 'Last Name' },
      {field: 'email', header: 'E-Mail' },
      {field: 'password', header: 'Password' },
      {field: 'enabled', header: 'Enabled' },
      {field: 'roles', header: 'Roles' }
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

}

