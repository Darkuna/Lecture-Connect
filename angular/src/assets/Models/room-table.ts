import {Timing} from "./timing";
import {Room} from "./room";
import {TimeTable} from "./time-table";
import {AvailabilityMatrix} from "./availability-matrix";
import {CourseSession} from "./course-session";

export class RoomTable {
  constructor(
    private id: number,
    private assignedCourseSessions: CourseSession[],
    private timingConstraints: Timing[],
    private room: Room,
    private timeTable: TimeTable,
    private availabilityMatrix: AvailabilityMatrix) {
  }
}
