import {Component, signal, ViewChild} from '@angular/core';
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import rrulePlugin from "@fullcalendar/rrule";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {BehaviorSubject, combineLatest, from, map, Observable, of, Subject} from "rxjs";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent{
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

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
/*
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventAdd: this.interactWithModel.bind(this),
    eventRemove: this.interactWithModel.bind(this),
 */
    allDaySlot: false,
    height: "auto",
    eventBackgroundColor: "#666666",
    eventBorderColor: "#050505",
    eventTextColor: "var(--system-color-primary-white)",
    slotMinTime: this.formatTime(this.tmpStartDate),
    slotMaxTime: this.formatTime(this.tmpEndDate),
    slotDuration: this.formatTime(this.tmpDuration),
    slotLabelInterval: this.formatTime(this.tmpSlotInterval),
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: false,
    slotEventOverlap: false,
    nowIndicator: false,
    droppable: true,
  });

  externalEvents = [
    { title: 'Event 1', id: '1', duration: '01:00' },
    { title: 'Event 2', id: '2', duration: '02:00' },
    { title: 'Event 3', id: '3', duration: '01:30' }
  ];

  selectedTimeTable: Observable<TimeTableDTO>;
  availableRooms: RoomTableDTO[] = [];
  selectedRoom: RoomTableDTO | null = null;
  combinedTableEvents: Observable<EventInput[]> = new Observable<EventInput[]>();

  constructor(
    private globalTableService: GlobalTableService,
    private converter: EventConverterService,
  ) {
    this.selectedTimeTable = this.globalTableService.currentTimeTable ?? new Observable<TimeTableDTO>();
    this.selectedTimeTable.subscribe( r => {
        this.availableRooms = r.roomTables;
        this.selectedRoom = r.roomTables[0];

        this.loadNewRoom();
      }
    );
  }

  loadNewRoom(){
    this.clearCalendar();

    let obs1: Observable<EventInput[]> = this.reloadNewBackgroundEvents();
    let obs2: Observable<EventInput[]> = this.reloadNewEvents();

    this.combinedTableEvents = combineLatest([obs1, obs2]).pipe(
      map(([events1, events2]) => [...events1, ...events2])
    );


  }

  reloadNewEvents(): Observable<EventInput[]> {
    let subject = new Subject<EventInput[]>();

    this.selectedTimeTable.subscribe(t => {
      let sessions: CourseSessionDTO[] = t.courseSessions
        .filter(s => s.roomTable?.id === this.selectedRoom?.id);

      let eventInputs: EventInput[] = sessions.map(s =>
        this.converter.convertTimingToEventInput(s, true)
      );

      subject.next(eventInputs);
      subject.complete();
    });

    return subject.asObservable();
  }

  reloadNewBackgroundEvents() :Observable<EventInput[]>{
    let combinedEvents: EventInput[] = [];
    this.selectedRoom?.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
    });

    return of(combinedEvents);
  }

  clearCalendar(){
    this.calendarComponent.getApi().removeAllEvents();
  }

  unselectRoom(){
    this.selectedRoom = null;
    this.clearCalendar();
  }

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  protected readonly JSON = JSON;
}
