import { Injectable } from '@angular/core';
import { EventInput } from '@fullcalendar/core';
import { CourseSession } from '../../assets/Models/course-session';

import {Timing} from "../../assets/Models/timing";
import LocalTime from "ts-time/LocalTime";
import {map, OperatorFunction} from "rxjs";
import {EventImpl} from "@fullcalendar/core/internal";

@Injectable({
  providedIn: 'root'
})
export class EventConverterService {
  convertTimingToEventInput(session: CourseSession): EventInput {
    return {
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      title: session.name,
      description: session.name,
      id: session.id.toString(),
      startTime: this.convertArrayToTime(session.timing?.startTime!),
      endTime: this.convertArrayToTime(session.timing?.endTime!),
    };
  }

  convertUTCDateToLocalDate(date: Date){
    return new Date(date.getTime() - date.getTimezoneOffset()*60*1000);
  }

  convertEventInputToTiming(event: EventImpl): Timing {
    let timing = new Timing();

    timing.id = 0;
    timing.timingType = event.title;
    timing.day = event.start?.getDay().toString();
    if (event.start) {
      timing.startTime = LocalTime.parse(event.start.toLocaleTimeString());
    }
    if (event.end) {
      timing.endTime = LocalTime.parse(event.end.toLocaleTimeString());
    }

    return timing;
  }

  private weekDayToNumber(day: string): string[] {
    switch (day) {
      case 'SUNDAY': return ['0'];
      case 'MONDAY': return ['1'];
      case 'TUESDAY': return ['2'];
      case 'WEDNESDAY': return ['3'];
      case 'THURSDAY': return ['4'];
      case 'FRIDAY': return ['5'];
      case 'SATURDAY': return ['6'];
      default: return ['-1'];
    }
  }

  //TODO change date to date: LocalTime and apply to function
  private convertArrayToTime(date: any): string {
    let hours;
    let minutes;

    if (date.length === 2) {
      hours = date[0].toString().padStart(2, '0');
      minutes = date[1].toString().padStart(2, '0');
    } else {
      hours = date[0].toString().padStart(2, '0');
      minutes = '00';
    }
    return `${hours}:${minutes}:00`;
  }

  convertCourseSessionToEventInput(): OperatorFunction<CourseSession, EventInput> {
    return map((session: CourseSession) => this.convertTimingToEventInput(session));
  }
}
