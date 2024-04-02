import {TimeTable} from '../time-table';
import {Semester} from "../enums/semester";

describe('TimeTable', () => {
  it('should create an instance', () => {
    expect(new TimeTable(1, Semester.SS, 1, [], [])).toBeTruthy();
  });
});
