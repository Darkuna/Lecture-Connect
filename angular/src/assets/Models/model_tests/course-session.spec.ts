import {CourseSession} from '../course-session';
import {Timing} from "../timing";

describe('CourseSession', () => {
  it('should create an instance', () => {
    expect(new CourseSession(0, [], false, false, 1, null, null, null, null)).toBeTruthy();
  });
});
