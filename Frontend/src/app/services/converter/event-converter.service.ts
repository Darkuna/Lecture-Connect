import {Injectable} from '@angular/core';
import {EventInput} from '@fullcalendar/core';
import {map, OperatorFunction} from "rxjs";
import {EventImpl} from "@fullcalendar/core/internal";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import {CourseColor} from "../../../assets/Models/enums/lecture-color";

@Injectable({
  providedIn: 'root'
})
export class EventConverterService {
  getValueBasedOnPage(session: CourseSessionDTO, call: 'editor' | 'home'): boolean{
    return (call === 'home') ? false : !session.fixed;
  }

  getColorBasedOnPage(session: CourseSessionDTO, call: 'editor' | 'home'): string{
    if(call === 'home') return '#666666';
    if (session.fixed && call === 'editor') return  '#5D6B5B';
    else return '#666666';
  }

  convertTimingToEventInput(session: CourseSessionDTO, call: 'editor' | 'home'): EventInput {
    return {
      id: session.id.toString(),
      title: session.name,
      description: session.roomTable?.roomId,
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      startTime: session.timing?.startTime ?? '',
      endTime: session.timing?.endTime ?? '',
      editable: this.getValueBasedOnPage(session, call),
      backgroundColor: this.getColorBasedOnPage(session, call),
      droppable: true,
      durationEditable: false,
      extendedProps: {
        'type': session.name.slice(0, 2),
        'semester': session.semester,
        'studyType': session.studyType,
        'assigned': session.assigned,
        'duration': this.convertDurationToHours(session.duration)
      }
    };
  }

  convertTimingEventInput(timing: TimingDTO): EventInput {
    return {
      title: timing.timingType,
      daysOfWeek: this.weekDayToNumber(timing.day),
      startTime: timing.startTime,
      endTime: timing.endTime,
      color: CourseColor[timing.timingType as keyof typeof CourseColor],
      borderColor: CourseColor[timing.timingType as keyof typeof CourseColor],
      display: 'background',
      editable: false,
    }
  };

  convertEventInputToTiming(event: EventImpl): TimingDTO {
    let timing = new TimingDTO();

    timing.id = 0;
    timing.timingType = event.title;
    if(event.start){
      timing.day = this.weekNumberToDay(event.start?.getDay());
    }
    if (event.start) {
      timing.startTime = this.convertLocalDateToString(event.start);
    }
    if (event.end) {
      timing.endTime = this.convertLocalDateToString(event.end);
    }

    return timing;
  }

  convertLocalDateToString(date: Date):string{
    let hours: number = date.getHours();
    let minutes: number = date.getMinutes();
    let seconds: number = date.getSeconds();

    // Pad single digit minutes and seconds with leading zeroes
    let formattedHours: string = hours < 10 ? '0' + hours : hours.toString();
    let formattedMinutes: string = minutes < 10 ? '0' + minutes : minutes.toString();
    let formattedSeconds: string = seconds < 10 ? '0' + seconds : seconds.toString();

    // Combine into a string of 'hh:mm:ss' format
    return `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
  }

  convertDurationToHours(duration: number): string {
    // Calculate hours and remaining minutes
    const hours = Math.floor(duration / 60);
    const mins = duration % 60;

    // Format with leading zero if needed
    const formattedHours = hours.toString().padStart(2, '0');
    const formattedMinutes = mins.toString().padStart(2, '0');

    // Return formatted time string
    return `${formattedHours}:${formattedMinutes}`;
  }

  convertMultipleCourseSessions(sessions: CourseSessionDTO[], call: 'editor' | 'home'){
    return sessions
      .map((s:CourseSessionDTO) =>
        this.convertTimingToEventInput(s, call)
      );
  }

  weekDayToNumber(day: string): string[] {
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

  weekNumberToDay(day: number):string {
    switch (day) {
      case 0: return 'SUNDAY';
      case 1: return 'MONDAY';
      case 2: return 'TUESDAY';
      case 3: return 'WEDNESDAY';
      case 4: return 'THURSDAY';
      case 5: return 'FRIDAY';
      case 6: return 'SATURDAY';
      default: return 'ERR';
    }
  }

  convertCourseSessionToEventInput(call: 'editor' | 'home'): OperatorFunction<CourseSessionDTO, EventInput> {
    return map((session: CourseSessionDTO) => this.convertTimingToEventInput(session, call));
  }

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  convertImplToInput(event: EventImpl): EventInput {
    return {
      id: event.id,
      title: event.title,
      extendedProps: event.extendedProps,
    }
  }
}
