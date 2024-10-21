import {Role} from "./enums/role";

export class Userx {
  serialVersionUID!: number;
  username?: string;
  createDate?: Date;
  updateDate?: Date;
  password!: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  enabled?: boolean;
  role?: Role[];


  public getTableColumns(): any[] {
    return [
      {field: 'serialVersionUID', header: 'Id'},
      {field: 'username', header: 'Username'},
      {field: 'firstName', header: 'First Name'},
      {field: 'lastName', header: 'Last Name'},
      {field: 'email', header: 'E-Mail'},
      {field: 'enabled', header: 'Enabled'},
      {field: 'role', header: 'Role'},
      {field: 'createDate', header: 'Creation Date'},
      {field: 'updateDate', header: 'last time updated'}
    ]
  }
}

