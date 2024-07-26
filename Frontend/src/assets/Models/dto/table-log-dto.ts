import {LoggerType} from "../enums/logger-types";
import {Course} from "../course";
import {Room} from "../room";

export class TableLogDto {
  eventDate?: string; //due to converting problems
  logType!: LoggerType;
  logObject: Room | Course | undefined;
  username!: string;


  constructor(eventDate: string, logType: LoggerType, logObject: Room | Course | undefined, username: string) {
    this.eventDate = eventDate;
    this.logType = logType;
    this.logObject = logObject;
    this.username = username;
  }
}
