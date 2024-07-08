import {ChangeDetectorRef, Component, Input, signal} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {CalendarOptions, DateSelectArg, EventApi, EventClickArg} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {INITIAL_EVENTS} from "../../home/event-utils";
import {CourseColor} from "../../../../assets/Models/enums/lecture-color";
import {Room} from "../../../../assets/Models/room";

@Component({
  selector: 'app-base-selection',
  templateUrl: './base-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class BaseSelectionComponent {
  @Input() globalTable!: TmpTimeTable;
  selectedRoom: Room | null = null;
  lastUsedColor: CourseColor | null = null;

  showTimeDialog: boolean = false;
  dialogTop: number = 0;
  dialogLeft: number = 0;
  eventTitle: string = '';
  eventStartTime: string = '';
  eventEndTime: string = '';
  selectInfo: DateSelectArg | null = null;

  constructor(
    private cd: ChangeDetectorRef
  ) {
  }

  getButtonStyle(color: string): { [key: string]: string } {
    return {
      'background-color': color,
      'border-color': color,
      'padding': '.5rem',
      'width': '-webkit-fill-available'
    };
  }

  showCalendar(): boolean {
    return this.selectedRoom != null;
  }

  calendarOptions = signal<CalendarOptions>({
    events: undefined,
    plugins: [
      interactionPlugin,
      dayGridPlugin,
      timeGridPlugin,
      listPlugin,
    ],
    headerToolbar: {
      left: '',
      center: '',
      right: ''
    },
    initialView: 'timeGridWeek',
    initialEvents: INITIAL_EVENTS,
    weekends: false,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventsSet: this.handleEvents.bind(this),
    allDaySlot: false,
    height: "auto",
    eventBackgroundColor: "#666666",
    eventBorderColor: "#050505",
    eventTextColor: "var(--system-color-primary-white)",
    slotMinTime: '07:00:00',
    slotMaxTime: '23:00:00',
    slotDuration: '00:15:00',
    slotLabelInterval: '01:00:00',
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: true,
    slotEventOverlap: false,
    nowIndicator: false,
  });

  currentEvents = signal<EventApi[]>([]);

  handleDateSelect(selectInfo: DateSelectArg) {
    this.showTimeDialog = true;
    this.selectInfo = selectInfo;
  }

  private id = 0;

  saveEvent() {
    const calendarApi = this.selectInfo?.view.calendar;

    if (this.eventTitle && this.eventStartTime && this.eventEndTime) {
      calendarApi?.addEvent({
        id: this.id,
        title: this.eventTitle,
        start: this.eventStartTime,
        end: this.eventEndTime,
        allDay: false
      });

      this.resetDialog();
      this.id += 1;
    }
  }

  cancelEvent() {
    this.resetDialog();
  }

  resetDialog() {
    this.showTimeDialog = false;
    this.eventTitle = '';
    this.eventStartTime = '';
    this.eventEndTime = '';
    this.selectInfo = null;
  }

  handleEventClick(clickInfo: EventClickArg) {
    if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      clickInfo.event.remove();
    }
  }

  handleEvents(events: EventApi[]) {
    this.currentEvents.set(events);
    this.cd.detectChanges();
  }

  protected readonly CourseColor = CourseColor;
}
