import {Pipe, PipeTransform} from '@angular/core';
import {EventInput} from "@fullcalendar/core";
import {CourseSession} from "../../assets/Models/course-session";

const TODAY_STR = new Date().toISOString().replace(/T.*$/, ''); // YYYY-MM-DD of today

@Pipe({
  name: 'timingToEvent'
})
export class TimingToEventPipe implements PipeTransform {
  transform(value: CourseSession): EventInput {
    return {
      id: value.id.toString(),
      title: value.name,
      start: value.timing?.startTime,
      end: value.timing?.startTime
    };
  }
}
