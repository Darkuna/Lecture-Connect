import {RoomTable} from '../room-table';

describe('RoomTable', () => {
  it('should create an instance', () => {
    expect(new RoomTable(1, [], [], null, null, null)).toBeTruthy();
  });
});
