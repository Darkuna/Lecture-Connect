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
  private getEditablePage(session: CourseSessionDTO, call: 'editor' | 'home'): boolean{
    return (call === 'home') ? false : !session.fixed;
  }

  private getColorBasedOnPage(session: CourseSessionDTO, call: 'editor' | 'home'): string{
    if (session.fixed && call === 'editor') return  '#7a4444';
    else return '#666666';
  }

  convertSessionToEvent(session: CourseSessionDTO, call: 'editor' | 'home'): EventInput {
    return {
      id: session.id.toString(),
      title: session.name,
      description: session.roomTable?.roomId,
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      startTime: session.timing?.startTime ?? '',
      endTime: session.timing?.endTime ?? '',
      editable: this.getEditablePage(session, call),
      backgroundColor: this.getColorBasedOnPage(session, call),
      durationEditable: false,
      droppable: true,
      extendedProps: {
        'type': session.name.slice(0, 2),
        'semester': session.semester,
        'studyType': session.studyType,
        'assigned': session.assigned,
        'duration': this.convertDurationToHours(session.duration),
        'nrOfParticipants': session.numberOfParticipants
      }
    };
  }

  convertToBackgroundEvent(timing: TimingDTO): EventInput {
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
    if(event.start) timing.day = this.weekNumberToDay(event.start?.getDay());
    if (event.start) timing.startTime = this.convertLocalDateToString(event.start);
    if (event.end) timing.endTime = this.convertLocalDateToString(event.end);

    return timing;
  }

  convertLocalDateToString(date: Date):string{
    const hours: number = date.getHours();
    const minutes: number = date.getMinutes();
    const seconds: number = date.getSeconds();

    const formattedHours: string = hours < 10 ? '0' + hours : hours.toString();
    const formattedMinutes: string = minutes < 10 ? '0' + minutes : minutes.toString();
    const formattedSeconds: string = seconds < 10 ? '0' + seconds : seconds.toString();

    return `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
  }

  convertDurationToHours(duration: number): string {
    const hours = Math.floor(duration / 60);
    const mins = duration % 60;

    const formattedHours = hours.toString().padStart(2, '0');
    const formattedMinutes = mins.toString().padStart(2, '0');

    return `${formattedHours}:${formattedMinutes}`;
  }

  convertMultipleCourseSessions(sessions: CourseSessionDTO[], call: 'editor' | 'home'){
    return sessions
      .map((s:CourseSessionDTO) =>
        this.convertSessionToEvent(s, call)
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
    return map((session: CourseSessionDTO) => this.convertSessionToEvent(session, call));
  }

  formatTime(date: Date): string {
    return date.toString().split(' ')[4];
  }
}
