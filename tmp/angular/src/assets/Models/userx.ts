import {Role} from "./enums/role";

export class Userx {
  constructor(
    private serialVersionUID: number,
    private username: string,
    private createUser: Userx,
    private createDate: Date,
    private updateUser: Userx,
    private updateDate: Date,
    private password: string,
    private firstName: string,
    private lastName: string,
    private email: string,
    private enabled: boolean,
    private role: Role
  ) {
  }
}

