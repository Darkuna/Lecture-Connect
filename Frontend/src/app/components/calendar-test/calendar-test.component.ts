import { Component } from '@angular/core';
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin, { DateClickArg } from "@fullcalendar/interaction";
import {FullCalendarModule} from "@fullcalendar/angular";

@Component({
  selector: 'app-calendar-test',
  templateUrl: './calendar-test.component.html',
  styleUrl: './calendar-test.component.css'
})
export class CalendarTestComponent {
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    dateClick: (arg) => this.handleDateClick(arg)
  };
  // @ts-ignore
  eventsPromise: Promise<EventInput[]>;

  handleDateClick(arg: DateClickArg) {
    alert('date click! ' + arg.dateStr);
  }
}
