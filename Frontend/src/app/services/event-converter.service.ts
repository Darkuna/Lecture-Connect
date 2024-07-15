import { Injectable } from '@angular/core';
import {Timing} from "../../assets/Models/timing";
import {EventInput} from "@fullcalendar/core";
import {CourseSession} from "../../assets/Models/course-session";
import {min} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EventConverterService {
  convertTimingToEventInput(session: CourseSession): EventInput {
    return {
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      title: session.name,
      id: session.id.toString(),
      startTime: this.convertArrayToTime(session.timing?.startTime!),
      endTime: this.convertArrayToTime(session.timing?.endTime!),
    };
  }

  private weekDayToNumber(day: string):[string]{
    switch (day){
      case ('SUNDAY'): return ['0'];
      case ('MONDAY'): return ['1'];
      case ('TUESDAY'): return ['2'];
      case ('WEDNESDAY'): return ['3'];
      case ('THURSDAY'): return ['4'];
      case ('FRIDAY'): return ['5'];
      case ('SATURDAY'): return ['6'];
      default: return ['-1']
    }
  }

  private convertArrayToTime(date: number[]): string {
    let hours;
    let minutes;

    if(date.length === 2){
      hours = date[0].toString().padStart(2, '0');
      minutes = date[1].toString().padStart(2, '0');
    } else {
      hours = date[0].toString().padStart(2, '0');
      minutes = '00'
    }
    return `${hours}:${minutes}:00`;
  }
}

