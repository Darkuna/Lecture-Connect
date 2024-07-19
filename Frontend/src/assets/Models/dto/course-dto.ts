import { TimingDTO } from './timing-dto';

export interface CourseDTO {
  id: string;
  courseType: string;
  name: string;
  lecturer: string;
  semester: number;
  duration: number;
  numberOfParticipants: number;
  computersNecessary: boolean;
  timingConstraints: TimingDTO[];
  numberOfGroups: number;
  isSplit: boolean;
  splitTimes: number[];
  createdAt: string;  // Format: yyyy-MM-dd'T'HH:mm:ss
  updatedAt: string;  // Format: yyyy-MM-dd'T'HH:mm:ss
}
