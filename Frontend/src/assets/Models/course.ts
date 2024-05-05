import {CourseSession} from "./course-session";
import {Timing} from "./timing";
import {CourseType} from "./enums/course-type";

export class Course {
  id!: number;
  courseType?: CourseType;
  name?: string;
  lecturer?: string;
  semester?: number;
  duration?: number;
  numberOfParticipants?: number;
  computersNecessary?: boolean;
  courseSessions?: CourseSession[];
  timingConstraints?: Timing[];
  numberOfGroups?: number;
  isSplit?: boolean;
  splitTimes?: number[];
  createDate?: Date;
  updateDate?: Date;

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'lecturer', header: 'Lecturer'},
      {field: 'semester', header: 'Semester'},
      {field: 'duration', header: 'Duration'},
      {field: 'numberOfParticipants', header: '#Participants'},
      {field: 'computersNecessary', header: 'computer needed'},
      {field: 'createDate', header: 'Creation Date'},
      {field: 'updateDate', header: 'last time updated'}
    ]
  }
}
