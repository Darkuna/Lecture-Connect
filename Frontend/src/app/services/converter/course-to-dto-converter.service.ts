import { Injectable } from '@angular/core';
import {CourseDTO} from "../../../assets/Models/dto/course-dto";
import {Course} from "../../../assets/Models/course";
import {TimingToDtoConverterService} from "./timing-to-dto-converter.service";

@Injectable({
  providedIn: 'root'
})
export class CourseToDtoConverterService {
  constructor(
    private timingConverter: TimingToDtoConverterService,
  ) {
  }

  convertCourseToDto(course: Course): CourseDTO {
    return new CourseDTO(
      course.id,
      course.courseType!.toString(),
      course.studyType!.toString(),
      course.name!,
      course.lecturer!,
      course.semester!,
      course.duration!,
      course.numberOfParticipants!,
      course.computersNecessary!,
      this.timingConverter.convertMultiple(course.timingConstraints!),
      '',
      '',
      course.numberOfGroups!,
      course.isSplit,
      course.splitTimes
    );
  }

  convertListToDto(courses: Course[]): CourseDTO[]{
    let dto: CourseDTO[] = [];

    courses.forEach(c => {
      dto.push( this.convertCourseToDto(c) )
    })

    return dto;
  }
}
