import {Role} from "./enums/role";

export class Userx {
  constructor(
    private serialVersionUID: number,
    private username: string,
    private createDate: Date,
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

