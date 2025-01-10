import { Injectable } from '@angular/core';
import {Userx, UserxDTO} from "../../../assets/Models/userx";

@Injectable({
  providedIn: 'root'
})
export class UserxConverterService {

  constructor() { }

  toDTO(userx: Userx): UserxDTO {
    const dto = new UserxDTO();
    dto.id = userx.id;
    dto.username = userx.username;
    dto.createDate = userx.createDate;
    dto.updateDate = userx.updateDate;
    dto.password = userx.password;
    dto.firstName = userx.firstName;
    dto.lastName = userx.lastName;
    dto.email = userx.email;
    dto.enabled = userx.enabled;
    dto.roles = [userx.role!];
    dto.new = userx.new
    return dto;
  }

  fromDTO(userxDTO: UserxDTO): Userx {
    const userx = new Userx();
    userx.id = userxDTO.id ?? 'not defined';
    userx.username = userxDTO.username;
    userx.createDate = userxDTO.createDate;
    userx.updateDate = userxDTO.updateDate;
    userx.password = userxDTO.password;
    userx.firstName = userxDTO.firstName;
    userx.lastName = userxDTO.lastName;
    userx.email = userxDTO.email;
    userx.enabled = userxDTO.enabled;
    userx.role = userxDTO.roles?.sort()[0];
    return userx;
  }

  convertUserList(users: UserxDTO[]): Userx[] {
    return users.map(this.fromDTO);
  }
}
