import {CourseSession} from "./course-session";
import {Timing} from "./timing";
import {CourseType} from "./enums/course-type";

export class Course {
  id!: string;
  courseType?: CourseType;
  name?: string;
  lecturer?: string;
  semester?: number;
  duration?: number;
  numberOfParticipants?: number;
  computersNecessary?: boolean;
  courseSessions?: CourseSession[];
  timingConstraints?: Timing[];
  numberOfGroups: number | null = null;
  isSplit: boolean = false;
  splitTimes: number[] = [];
  createdAt?: Date;
  updatedAt?: Date;

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
      {field: 'createdAt', header: 'created at'},
      {field: 'updatedAt', header: 'updated at'}
    ]
  }
}
