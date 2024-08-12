import {Component, ElementRef, signal, ViewChild} from '@angular/core';
import {CalendarOptions} from "@fullcalendar/core";
import rrulePlugin from "@fullcalendar/rrule";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent  {
  calendarOptions = signal<CalendarOptions>({
    plugins: [
      rrulePlugin,
      dayGridPlugin,
      timeGridPlugin
    ],
    headerToolbar: {
      left: '',
      center: '',
      right: ''
    },
    initialView: 'timeGridWeek',
    weekends: false,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
/*    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventAdd: this.interactWithModel.bind(this),
    eventRemove: this.interactWithModel.bind(this),
 */
    allDaySlot: false,
    height: "auto",
    /*
    eventBackgroundColor: this.lastUsedColor,
    eventBorderColor: this.lastUsedColor,
    */
    eventTextColor: "var(--system-color-primary-white)",
    /*
    slotMinTime: this.formatTime(this.tmpStartDate),
    slotMaxTime: this.formatTime(this.tmpEndDate),
    slotDuration: this.formatTime(this.tmpDuration),
    slotLabelInterval: this.formatTime(this.tmpSlotInterval),
     */
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: false,
    slotEventOverlap: false,
    nowIndicator: false,
    droppable: true,
    eventReceive: this.handleEventReceive.bind(this)
  });
  @ViewChild('calendar') calendarComponent!: ElementRef;

  externalEvents = [
    { title: 'Event 1', id: '1', duration: '01:00' },
    { title: 'Event 2', id: '2', duration: '02:00' },
    { title: 'Event 3', id: '3', duration: '01:30' }
  ];

  handleEventReceive(eventInfo: any) {
    console.log('Event dropped on calendar:', eventInfo.event);
  }

  protected readonly JSON = JSON;
}
