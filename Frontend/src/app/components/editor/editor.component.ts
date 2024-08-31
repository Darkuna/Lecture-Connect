import {AfterViewInit, Component, ElementRef, OnDestroy, ViewChild} from '@angular/core';
import {CalendarOptions, EventApi, EventChangeArg, EventClickArg, EventInput} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import {GlobalTableService} from "../../services/global-table.service";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {RoomTableDTO} from "../../../assets/Models/dto/room-table-dto";
import interactionPlugin, {Draggable, DropArg, EventReceiveArg} from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import {CalendarContextMenuComponent} from "../home/calendar-context-menu/calendar-context-menu.component";
import {ConfirmationService} from "primeng/api";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";

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
  tmpDuration: Date = new Date('2024-07-10T00:15:00');
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
    eventReceive: this.eventReceive.bind(this),
    eventChange: this.eventChange.bind(this)
  };

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
    private confirmationService: ConfirmationService
  ) {
    this.selectedTimeTable = this.globalTableService.currentTimeTable ?? new Observable<TimeTableDTO>();
    this.selectedTimeTable.subscribe( r => {
        this.availableRooms = r.roomTables;
        this.selectedRoom = r.roomTables[0];

        this.timeTable = r;
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
          editable: true,
        };
      }
    });
  }

  ngOnDestroy(): void {
    this.draggable.destroy()
  }

  loadNewRoom(newRoom: RoomTableDTO): void {
    this.saveChanges();

    this.allEvents = this.converter.convertMultipleCourseSessions(this.timeTable.courseSessions);
    this.maxEvents = this.allEvents.length;
    this.selectedRoom = newRoom;

    this.dragTableEvents = this.allEvents.filter(s => !s.extendedProps?.['assigned']);
    this.nrOfEvents = this.maxEvents - this.dragTableEvents.length;

    this.combinedTableEvents = [
      ... this.reloadNewEvents(newRoom.roomId),
      ...this.reloadNewBackgroundEvents(this.selectedRoom!),
    ]
  }

  reloadNewEvents(roomId: string): EventInput[] {
    return this.allEvents.filter(e => e['description'] === roomId);
  }

  reloadNewBackgroundEvents(room: RoomTableDTO) :EventInput[]{
    let combinedEvents: EventInput[] = [];
    room.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
    });

    return combinedEvents;
  }

  saveChanges(){
    console.log("saved changes", this.timeTable.courseSessions);
  }

  drop(arg: DropArg) {
    this.dragTableEvents =
      this.dragTableEvents.filter(
        e => e.id !== arg.draggedEl.getAttribute('data-id'))
    this.nrOfEvents += 1;
  }

  eventClick(args: EventClickArg) {
    if(args.event.title !== 'BLOCKED' && args.event.title !== 'COMPUTER_SCIENCE'){
      this.confirmationService.confirm({
        header: 'Remove selected Course',
        message: 'Are you sure you want to remove the selected Room?',

        accept: () => {
          const updatedSession = this.updateSession(args.event, false)!;
          updatedSession.timing = null;

          this.dragTableEvents.push(
            this.converter.convertTimingToEventInput(updatedSession, true)
          );

          args.event.remove();
          this.nrOfEvents -= 1;
          },

        reject: () => {}
      })
    }
  }

  eventReceive(args: EventReceiveArg){
    this.updateSession(args.event, true);
  }

  eventChange(args: EventChangeArg){
    this.updateSession(args.event, true);
  }

  private updateSession(event:EventApi, assigned: boolean): CourseSessionDTO | undefined{
    const session = this.timeTable.courseSessions
      .find(s => s.id.toString() === event.id);

    if(session){
      session!.roomTable = this.selectedRoom!;
      session!.assigned = assigned;
      session!.timing = new TimingDTO();
      session!.timing!.startTime = this.converter.convertLocalDateToString(event.start!);
      session!.timing!.endTime = this.converter.convertLocalDateToString(event.end!);
      session!.timing!.day = this.converter.weekNumberToDay(event.start?.getDay() || 1);
    }
    return session ;
  }

  updateCalendar(calendarOption: any, value: string) {
    if(value === '00:00:00'){
      value = '00:00:05';
    }
    this.calendarComponent.getApi().setOption(calendarOption, value);
  }

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  protected readonly JSON = JSON;
}
