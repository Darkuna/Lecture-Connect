import { Injectable } from '@angular/core';
import {LoginUserInfoService} from "./login-user-info.service";
import {TableLogDto} from "../../assets/Models/dto/table-log-dto";
import {LoggerType} from "../../assets/Models/enums/logger-types";
import {Room} from "../../assets/Models/room";
import {Course} from "../../assets/Models/course";
import {RoomTableDTO} from "../../assets/Models/dto/room-table-dto";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";

@Injectable({
  providedIn: 'root'
})
export class TableLoggerService {
  constructor(
    private userInfoService: LoginUserInfoService
  ) { }

  create(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.CREATE, logObject, this.userInfoService.username);
  }

  add(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.ADD, logObject, this.userInfoService.username);
  }

  update(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.UPDATE, logObject, this.userInfoService.username);
  }

  delete(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.DELETE, logObject, this.userInfoService.username);
  }

  schedule(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.SCHEDULE, logObject, this.userInfoService.username);
  }

  status(logObject: Room | Course | RoomTableDTO | CourseSessionDTO | undefined){
    return new TableLogDto(Date.now().toString(), LoggerType.STATUS, logObject, this.userInfoService.username);
  }
}
