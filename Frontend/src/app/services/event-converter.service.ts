import { Injectable } from '@angular/core';
import { EventInput } from '@fullcalendar/core';
import { CourseSession } from '../../assets/Models/course-session';

import {Timing} from "../../assets/Models/timing";
import LocalTime from "ts-time/LocalTime";
import {map, OperatorFunction} from "rxjs";
import {EventImpl} from "@fullcalendar/core/internal";
import {TimingDTO} from "../../assets/Models/dto/timing-dto";

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
    const timeParts = timeString.match(/(\d+):(\d+):(\d+)\s(AM|PM)/i);
    if (!timeParts) {
      throw new Error('Invalid time format.');
    }

    let hours = parseInt(timeParts[1], 10);
    const minutes = parseInt(timeParts[2], 10);
    const seconds = parseInt(timeParts[3], 10);
    const period = timeParts[4];

    if (period.toUpperCase() === 'PM' && hours < 12) {
      hours += 12;
    }
    if (period.toUpperCase() === 'AM' && hours === 12) {
      hours = 0;
    }

    // Convert to string in HH:MM:SS format
    const formattedTimeString = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    return LocalTime.parse(formattedTimeString);
  }

  convertEventInputToTiming(event: EventImpl): TimingDTO {
    let timing = new TimingDTO();

    timing.id = 0;
    timing.timingType = event.title;
    if(event.start){
      timing.day = event.start?.getDay().toString();
    }
    if (event.start) {
      timing.startTime = event.start.toLocaleTimeString();
    }
    if (event.end) {
      timing.endTime = event.end.toLocaleTimeString();
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
  private convertArrayToTime(date: LocalTime): string {
    return date.toString();
  }

  convertCourseSessionToEventInput(): OperatorFunction<CourseSession, EventInput> {
    return map((session: CourseSession) => this.convertTimingToEventInput(session));
  }
}
