import {CourseSession} from "./course-session";
import {Timing} from "./timing";
import {CourseType} from "./course-type";

export class Course {
  constructor(
    id: string,
    courseType: CourseType,
    name: string,
    lecturer: string,
    semester: number,
    duration: number,
    numberOfParticipants: number,
    computersNecessary: boolean,
    courseSessions: CourseSession[],
    timingConstraints: Timing[],
    numberOfGroups: number,
    isSplit: boolean,
    splitTimes: number[]
  ) {
  }
}
