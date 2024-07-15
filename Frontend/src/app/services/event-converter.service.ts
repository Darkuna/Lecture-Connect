import { Injectable } from '@angular/core';
import {Timing} from "../../assets/Models/timing";
import {EventInput} from "@fullcalendar/core";
import {CourseSession} from "../../assets/Models/course-session";

@Injectable({
  providedIn: 'root'
})
export class EventConverterService {
  convertTimingToEventInput(session: CourseSession): EventInput {
    return {
      title: session.name,
      id: session.id.toString(),
      start: session.timing?.startTime,
      end: session.timing?.endTime,
    };
  }
}

