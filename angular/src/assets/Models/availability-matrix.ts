import {CourseSession} from "./course-session";
import {TimingConstants} from "./timing-constants"

export class AvailabilityMatrix {
  private static DAYS_IN_WEEK = 5;
  private static START_TIME = TimingConstants.START_TIME;
  private static END_TIME = TimingConstants.END_TIME;
  private SLOTS_PER_DAY = 5;

  constructor(
    private matrix: CourseSession[][]
  ) {
  }
}

