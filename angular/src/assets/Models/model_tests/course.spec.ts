import {Course} from '../course';

describe('Course', () => {
  it('should create an instance', () => {
    expect(new Course('null', null, null, null, null, null, null, null, null, null, null, null, null)).toBeTruthy();
  });
});
