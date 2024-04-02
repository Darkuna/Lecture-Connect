import {Userx} from '../userx';
import {Role} from "../enums/role";

describe('Userx', () => {
  it('should create an instance', () => {
    expect(new Userx(1, 'null', null, null, null, null, null, null, null, Role.USER)).toBeTruthy();
  });
});
