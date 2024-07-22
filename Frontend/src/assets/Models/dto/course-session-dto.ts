import { TimingDTO } from './timing-dto';

export interface CourseSessionDTO {
  id: number;
  name: string;
  isAssigned: boolean;
  isFixed: boolean;
  duration: number;
  timingConstraints: TimingDTO[];
  timing?: TimingDTO;
  roomTableId?: number;
}
