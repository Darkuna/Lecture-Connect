import {Role} from "./enums/role";
import {TableData} from "./interfaces/TableData";

export class Userx implements TableData {
  constructor(
    private serialVersionUID?: number,
    private username?: string,
    private createDate?: Date,
    private updateDate?: Date,
    private password?: string,
    private firstName?: string,
    private lastName?: string,
    private email?: string,
    private enabled?: boolean,
    private role?: Role
  ) { }

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

}

