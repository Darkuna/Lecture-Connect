import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {Observable, of} from "rxjs";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import interactionPlugin, {Draggable} from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import {CalendarContextMenuComponent} from "../home/calendar-context-menu/calendar-context-menu.component";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent implements AfterViewInit{
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
    drop: this.drop.bind(this)
  };

  selectedTimeTable: Observable<TimeTableDTO>;
  availableRooms: RoomTableDTO[] = [];
  selectedRoom: RoomTableDTO | null = null;
  combinedTableEvents: Observable<EventInput[]> = new Observable<EventInput[]>();
  dragTableEvents: EventInput[] =
    [{ title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' },
      { title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' },
      { title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' },
      { title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' },
      { title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' },
      { title: 'Event 1', start: '10:40:00', end: '11:40:00', description: 'Description for Event 1' }];

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
    this.calendarContextMenu.calendarComponent = this.calendarComponent;

    new Draggable(this.external.nativeElement, {
      itemSelector: '.fc-event',
      eventData: function(eventEl) {
        return {
          title: eventEl.innerText
        };
      }
    });
  }

  loadNewRoom(newRoom: RoomTableDTO): void {
    this.clearCalendar();
    this.selectedRoom = newRoom;

    this.selectedTimeTable.subscribe(t => {
      const placedEvents: EventInput[] = this.reloadNewEvents(this.selectedRoom?.id!, t);
      this.dragTableEvents = placedEvents; //only for testing!!
      //const unusedEvents: EventInput[] = placedEvents.filter(event => event;
      this.nrOfEvents = placedEvents.length;
      this.maxEvents = placedEvents.length; //add events on right side of page

      this.combinedTableEvents = of([
        ...placedEvents,
        ...this.reloadNewBackgroundEvents(this.selectedRoom!),
      ]);
    })
  }

  reloadNewEvents(IdOfRoom: number, table: TimeTableDTO): EventInput[] {
    return table.courseSessions
        .filter(
          (s:CourseSessionDTO) => s.roomTable?.id === IdOfRoom
        )
        .map((s:CourseSessionDTO) =>
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

  drop(arg: any) {
    console.log(arg);
  arg.draggedEl.parentNode.removeChild(arg.draggedEl);
}

  protected readonly JSON = JSON;
}
