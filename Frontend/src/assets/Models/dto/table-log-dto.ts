import {LoggerType} from "../enums/logger-types";
import {Course} from "../course";
import {Room} from "../room";

export class TableLogDto {
  eventDate?: string; //due to converting problems
  text?: string;
  logType!: LoggerType;
  logObject?: Room | Course;
  username!: string;
}
