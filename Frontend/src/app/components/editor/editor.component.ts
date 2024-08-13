import {AfterViewInit, Component, signal, ViewChild} from '@angular/core';
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import rrulePlugin from "@fullcalendar/rrule";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {Observable, of} from "rxjs";
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
export class EditorComponent implements AfterViewInit{
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
    selectMirror: false,
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
    slotMinTime: this.converter.formatTime(this.tmpStartDate),
    slotMaxTime: this.converter.formatTime(this.tmpEndDate),
    slotDuration: this.converter.formatTime(this.tmpDuration),
    slotLabelInterval: this.converter.formatTime(this.tmpSlotInterval),
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

  nrOfEvents: number = 0;
  maxEvents: number = 0;

  constructor(
    private globalTableService: GlobalTableService,
    private converter: EventConverterService,
  ) {
    this.selectedTimeTable = this.globalTableService.currentTimeTable ?? new Observable<TimeTableDTO>();
    this.selectedTimeTable.subscribe( r => {
        this.availableRooms = r.roomTables;
        this.selectedRoom = r.roomTables[0];

      }
    );
  }

  ngAfterViewInit(): void {
    this.loadNewRoom(this.selectedRoom!);
  }

  loadNewRoom(newRoom: RoomTableDTO): void {
    this.clearCalendar();
    this.selectedRoom = newRoom;

    this.selectedTimeTable.subscribe(t => {
      this.combinedTableEvents = of([
        ...this.reloadNewEvents(this.selectedRoom?.id!, t),
        ...this.reloadNewBackgroundEvents(this.selectedRoom!),
      ]);

    })
  }

  reloadNewEvents(IdOfRoom: number, table: TimeTableDTO): EventInput[] {
    return table.courseSessions
        .filter((s:CourseSessionDTO) => s.roomTable?.id === IdOfRoom)
        .map(s =>
          this.converter.convertTimingToEventInput(s, true)
        );
  }

  reloadNewBackgroundEvents(room: RoomTableDTO) :EventInput[]{
    let combinedEvents: EventInput[] = [];
    room.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
    });

    return combinedEvents;
  }

  clearCalendar(){
    this.calendarComponent.getApi().removeAllEvents();
  }

  protected readonly JSON = JSON;
}
