import {Timing} from '../timing';
import {Day} from "../enums/day";

describe('Timing', () => {
  it('should create an instance', () => {
    expect(new Timing(1, null, null, Day.FRIDAY, null, null)).toBeTruthy();
  });
});
