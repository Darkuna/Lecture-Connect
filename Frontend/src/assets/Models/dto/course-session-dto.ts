import { TimingDTO } from './timing-dto';
import {RoomTableDTO} from "./room-table-dto";

export interface CourseSessionDTO {
  id: number;
  name: string;
  isAssigned: boolean;
  isFixed: boolean;
  duration: number;
  semester: number;
  studyType: string;
  timingConstraints: TimingDTO[];
  timing?: TimingDTO;
  roomTable?: RoomTableDTO;
}
