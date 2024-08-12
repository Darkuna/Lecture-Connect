import {Injectable} from '@angular/core';
import {EventInput} from '@fullcalendar/core';
import {map, OperatorFunction} from "rxjs";
import {EventImpl} from "@fullcalendar/core/internal";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";
import {CourseColor} from "../../../assets/Models/enums/lecture-color";

@Injectable({
  providedIn: 'root'
})
export class EventConverterService {
  convertTimingToEventInput(session: CourseSessionDTO): EventInput {
    return {
      id: session.id.toString(),
      title: session.name,
      description: session.roomTable?.roomId,
      daysOfWeek: this.weekDayToNumber(session.timing?.day!),
      startTime: session.timing?.startTime,
      endTime: session.timing?.endTime!,
      extendedProps: {
        'type': session.name.slice(0, 2),
        'semester': session.semester,
        'studyType': session.studyType}
    };
  }

  convertRoomToEventInputs(room: RoomTableDTO): EventInput[] {
    let finalEvents:EventInput[] = [];

    room.timingConstraints?.forEach(t => {
      finalEvents.push(this.convertTimingEventInput(t));
    })

    return finalEvents;
  }

  convertTimingEventInput(timing: TimingDTO): EventInput {
    return {
      description: timing.timingType,
      daysOfWeek: this.weekDayToNumber(timing.day),
      startTime: timing.startTime,
      endTime: timing.endTime,
      color: CourseColor.COMPUTER_SCIENCE,
      borderColor: CourseColor.COMPUTER_SCIENCE,
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

  private convertLocalDateToString(date: Date):string{
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

  convertCourseSessionToEventInput(): OperatorFunction<CourseSessionDTO, EventInput> {
    return map((session: CourseSessionDTO) => this.convertTimingToEventInput(session));
  }

  convertTimingToBackgroundEventInput(): OperatorFunction<RoomTableDTO, EventInput> {
    return map((room: RoomTableDTO) => this.convertRoomToEventInputs(room));
  }
}
