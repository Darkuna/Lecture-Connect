import { TimingDTO } from './timing-dto';

export class CourseDTO {
  id!: string;
  courseType?: string;
  name?: string;
  lecturer?: string;
  semester?: number;
  duration?: number;
  numberOfParticipants?: number;
  computersNecessary?: boolean;
  timingConstraints: TimingDTO[] = [];
  createdAt?: string;
  updatedAt?: string;
  numberOfGroups?: number;
  isSplit?: boolean;
  splitTimes?: number[];


  constructor(id: string, courseType: string, name: string, lecturer: string, semester: number, duration: number, numberOfParticipants: number, computersNecessary: boolean, timingConstraints: TimingDTO[], createdAt: string, updatedAt: string, numberOfGroups: number, isSplit: boolean, splitTimes: number[]) {
    this.id = id;
    this.courseType = courseType;
    this.name = name;
    this.lecturer = lecturer;
    this.semester = semester;
    this.duration = duration;
    this.numberOfParticipants = numberOfParticipants;
    this.computersNecessary = computersNecessary;
    this.timingConstraints = timingConstraints;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.numberOfGroups = numberOfGroups;
    this.isSplit = isSplit;
    this.splitTimes = splitTimes;
  }
}
