import {Component, signal, ViewChild} from '@angular/core';
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import rrulePlugin from "@fullcalendar/rrule";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {BehaviorSubject, Observable} from "rxjs";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent{
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
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
  });

  externalEvents = [
    { title: 'Event 1', id: '1', duration: '01:00' },
    { title: 'Event 2', id: '2', duration: '02:00' },
    { title: 'Event 3', id: '3', duration: '01:30' }
  ];

  selectedTimeTable: Observable<TimeTableDTO>;
  availableRooms: RoomTableDTO[] = [];
  selectedRoom: RoomTableDTO | null = null;
  private combinedTableEventsSubject: BehaviorSubject<EventInput[]> = new BehaviorSubject<EventInput[]>([]);
  combinedTableEvents: Observable<EventInput[]> = this.combinedTableEventsSubject.asObservable();

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

  reloadNewEvents(){
    let combinedEvents = [];
    this.selectedRoom?.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
      this.combinedTableEventsSubject.next(combinedEvents);
    })
  }


  reloadNewBackgroundEvents(){
    let combinedEvents = [];
    this.selectedRoom?.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
      this.combinedTableEventsSubject.next(combinedEvents);
    })
  }

  clearCalendar(){
    this.calendarComponent.getApi().removeAllEvents();
  }

  protected readonly JSON = JSON;
}
