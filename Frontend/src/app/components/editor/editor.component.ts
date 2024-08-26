import {AfterViewInit, Component, ElementRef, OnDestroy, ViewChild} from '@angular/core';
import {CalendarOptions, EventClickArg, EventInput} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import interactionPlugin, {Draggable, DropArg} from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import {CalendarContextMenuComponent} from "../home/calendar-context-menu/calendar-context-menu.component";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent implements AfterViewInit, OnDestroy{
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @ViewChild('calendarContextMenu') calendarContextMenu! : CalendarContextMenuComponent;
  @ViewChild('external') external!: ElementRef;

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

  destCalendarOptions: CalendarOptions= {
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
    weekends: false,
    editable: false,
    selectable: false,
    selectMirror: true,
    dayMaxEvents: true,
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
    eventOverlap: true,
    slotEventOverlap: true,
    nowIndicator: false,
    drop: this.drop.bind(this),
    eventClick: this.eventClick.bind(this),
  };

  changedCourses: EventInput[] = [];
  selectedTimeTable: Observable<TimeTableDTO>;
  timeTable!: TimeTableDTO;
  availableRooms: RoomTableDTO[] = [];
  selectedRoom: RoomTableDTO | null = null;
  allEvents: EventInput[] = [];
  combinedTableEvents: EventInput[] = [];
  dragTableEvents: EventInput[] = [];
  draggable!: Draggable;

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
        this.allEvents = this.convertMultipleCourseSessions(r.courseSessions);
        this.maxEvents = this.allEvents.length;
        this.loadNewRoom(this.selectedRoom!);
      }
    );
  }

  ngAfterViewInit(): void {
    this.calendarContextMenu.calendarComponent = this.calendarComponent;

    this.draggable = new Draggable(this.external.nativeElement, {
      itemSelector: '.fc-event',
      eventData: (eventEl) => {
        return {
          id: eventEl.getAttribute('data-id'),
          title: eventEl.getAttribute('data-title'),
          duration: eventEl.getAttribute('data-duration'),
          editable: true
        };
      }
    });
  }



  ngOnDestroy(): void {
    this.draggable.destroy()
  }

  loadNewRoom(newRoom: RoomTableDTO): void {
    this.clearCalendar();
    this.selectedRoom = newRoom;

    this.dragTableEvents = this.allEvents.filter(s => !s.extendedProps?.['assigned']);
    this.nrOfEvents = this.maxEvents - this.dragTableEvents.length;

    this.combinedTableEvents = [
      ... this.reloadNewEvents(newRoom.roomId),
      ...this.reloadNewBackgroundEvents(this.selectedRoom!),
    ]
  }

  convertMultipleCourseSessions(sessions: CourseSessionDTO[]){
    return sessions
      .map((s:CourseSessionDTO) =>
        this.converter.convertTimingToEventInput(s, true)
      );
  }

  reloadNewEvents(roomId: string): EventInput[] {
    console.log(roomId)
    console.log(this.allEvents);
    return this.allEvents.filter(e => e['description'] === roomId);
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

  drop(arg: DropArg) {
    this.dragTableEvents =
      this.dragTableEvents.filter(
        e => e.id !== arg.draggedEl.getAttribute('data-id'))
    this.nrOfEvents += 1;
  }

  eventClick(arg: EventClickArg) {
    if(arg.event.title !== 'BLOCKED' && arg.event.title !== 'COMPUTER_SCIENCE'){
      this.dragTableEvents.push(this.converter.convertImplToInput(arg.event));
      arg.event.remove();
      this.nrOfEvents -= 1;
    }
  }

  protected readonly JSON = JSON;
}
