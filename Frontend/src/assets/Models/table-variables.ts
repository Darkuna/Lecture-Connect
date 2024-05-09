import {Role} from "./enums/role";
import {Timing} from "./timing";
import {RoomTable} from "./room-table";
import {CourseType} from "./enums/course-type";
import {CourseSession} from "./course-session";

export class TableVariables {
  //userx variables
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

  //rooms
  idRoom?: string;
  capacity?: number;
  computersAvailable?: boolean;
  timingConstraintsRoom?: Timing[];
  roomTables?: RoomTable[];

  //course
  idCourse?: string;
  courseType?: CourseType;
  name?: string;
  lecturer?: string;
  semester?: number;
  duration?: number;
  numberOfParticipants?: number;
  computersNecessary?: boolean;
  courseSessions?: CourseSession[];
  timingConstraintsCourse?: Timing[];
  numberOfGroups?: number;
  isSplit?: boolean;
  splitTimes?: number[];

  stateOptions: any[] = [
    {label: 'Yes', value: true},
    {label: 'No', value: false}
  ];
}
