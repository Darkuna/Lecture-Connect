import { Injectable } from '@angular/core';
import {LoginUserInfoService} from "./login-user-info.service";
import {TableLogDto} from "../../assets/Models/dto/table-log-dto";
import LocalTime from "ts-time/LocalTime";
import {LoggerType} from "../../assets/Models/enums/logger-types";

@Injectable({
  providedIn: 'root'
})
export class TableLoggerService {
  constructor(
    private userInfoService: LoginUserInfoService
  ) { }

  create(){
    return new TableLogDto(Date.now().toString(), LoggerType.CREATE, undefined, this.userInfoService.username);
  }

  add(){
    return new TableLogDto(Date.now().toString(), LoggerType.ADD, undefined, this.userInfoService.username);
  }

  update(){
    return new TableLogDto(Date.now().toString(), LoggerType.UPDATE, undefined, this.userInfoService.username);
  }

  delete(){
    return new TableLogDto(Date.now().toString(), LoggerType.DELETE, undefined, this.userInfoService.username);
  }

  schedule(){
    return new TableLogDto(Date.now().toString(), LoggerType.SCHEDULE, undefined, this.userInfoService.username);
  }

  status(){
    return new TableLogDto(Date.now().toString(), LoggerType.STATUS, undefined, this.userInfoService.username);
  }
}
