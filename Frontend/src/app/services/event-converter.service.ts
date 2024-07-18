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
      id: session.id.toString(),
      title: session.name,
      description: session.name,
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      startTime: this.convertArrayToTime(session.timing?.startTime!),
      endTime: this.convertArrayToTime(session.timing?.endTime!),
    };
  }

  parseTime(timeString: string): LocalTime {
    const timeParts = timeString.match(/(\d+):(\d+):(\d+) (\w+)/);
    if (!timeParts) {
      throw new Error('Invalid time format.');
    }

    let hours = parseInt(timeParts[1]);
    const minutes = parseInt(timeParts[2]);
    const seconds = parseInt(timeParts[3]);
    const period = timeParts[4];

    if (period === 'PM' && hours < 12) {
      hours += 12;
    }
    if (period === 'AM' && hours === 12) {
      hours = 0;
    }

    return LocalTime.of(hours, minutes, seconds);
  }

  convertEventInputToTiming(event: EventImpl): Timing {
    let timing = new Timing();

    timing.id = 0;
    timing.timingType = event.title;
    timing.day = event.start?.getDay().toString();
    if (event.start) {
      console.log(event.start.toLocaleTimeString());
      timing.startTime = this.parseTime(event.start.toLocaleTimeString());
    }
    if (event.end) {
      timing.endTime = this.parseTime(event.end.toLocaleTimeString());
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
    let hours = '00';
    let minutes = '00';
    if(date){
      if (date.length === 2) {
        hours = date[0].toString().padStart(2, '0');
        minutes = date[1].toString().padStart(2, '0');
      } else {
        hours = date[0].toString().padStart(2, '0');
        minutes = '00';
      }
    }
    return `${hours}:${minutes}:00`;
  }

  convertCourseSessionToEventInput(): OperatorFunction<CourseSession, EventInput> {
    return map((session: CourseSession) => this.convertTimingToEventInput(session));
  }
}
