import {CourseSession} from "./course-session";
import {Timing} from "./timing";
import {CourseType} from "./enums/course-type";
import {TableData} from "./interfaces/TableData";
import {Userx} from "./userx";
import {Room} from "./room";

export class Course implements TableData {
  id?: string;
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

  getTableColumns(): any[] {
    return [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'lecturer', header: 'Lecturer'},
      {field: 'semester', header: 'Semester'},
      {field: 'duration', header: 'Duration'},
      {field: 'numberOfParticipants', header: '#Participants'},
      {field: 'computersNecessary', header: 'computer needed'}
    ]
  }

  deleteMultipleItems(val: any[]): void {
  }

  deleteSingleItem(val: any): void {
  }

  editItem(val: any): any {
  }

  getData(): any[] {
    return [];
  }

  saveItem(val: any): any {
  }

  uploadItemToBackend(val: any): void {
  }

  isInList(item: Userx | Room | Course, items: (Userx | Room | Course)[]): boolean {
    return false;
  }


}
