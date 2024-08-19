import { Injectable } from '@angular/core';
import {CourseDTO} from "../../../assets/Models/dto/course-dto";
import {Course} from "../../../assets/Models/course";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";
import {TimingToDtoConverterService} from "./timing-to-dto-converter.service";

@Injectable({
  providedIn: 'root'
})
export class CourseToDtoConverterService {
  constructor(
    private timingConverter: TimingToDtoConverterService,
  ) {
  }

  convertCourseToDto(courses: Course[]): CourseDTO[]{
    let dto: CourseDTO[] = [];

    courses.forEach(c => {
      dto.push(
        new CourseDTO(
          c.id,
          c.courseType!.toString(),
          c.studyType!,
          c.name!,
          c.lecturer!,
          c.semester!,
          c.duration!,
          c.numberOfParticipants!,
          c.computersNecessary!,
          this.timingConverter.convertMultiple(c.timingConstraints!),
          c.createdAt?.toLocaleString()!,
          c.updatedAt?.toLocaleString()!,
          c.numberOfGroups!, c.isSplit!, c.splitTimes!)
      )
    })

    return dto;
  }
}
