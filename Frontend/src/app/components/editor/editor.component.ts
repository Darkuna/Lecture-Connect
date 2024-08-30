import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, ViewChild} from '@angular/core';
import { CalendarOptions, EventClickArg, EventInput } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import { GlobalTableService } from "../../services/global-table.service";
import { Observable, of, BehaviorSubject, Subscription } from "rxjs";
import { TimeTableDTO } from "../../../assets/Models/dto/time-table-dto";
import { EventConverterService } from "../../services/converter/event-converter.service";
import { FullCalendarComponent } from "@fullcalendar/angular";
import { RoomTableDTO } from "../../../assets/Models/dto/room-table-dto";
import { CourseSessionDTO } from "../../../assets/Models/dto/course-session-dto";
import interactionPlugin, { Draggable, DropArg } from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import { CalendarContextMenuComponent } from "../home/calendar-context-menu/calendar-context-menu.component";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements AfterViewInit, OnDestroy {
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @ViewChild('calendarContextMenu') calendarContextMenu!: CalendarContextMenuComponent;
  @ViewChild('external') external!: ElementRef;

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

  destCalendarOptions: CalendarOptions = {
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
    dayHeaderFormat: { weekday: 'long' },
    eventOverlap: true,
    slotEventOverlap: true,
    nowIndicator: false,
    drop: this.drop.bind(this),
    eventClick: this.eventClick.bind(this),
  };

  selectedTimeTable$: BehaviorSubject<TimeTableDTO | null> = new BehaviorSubject<TimeTableDTO | null>(null);
  availableRooms: RoomTableDTO[] = [];
  selectedRoom: RoomTableDTO | null = null;
  combinedTableEvents: Observable<EventInput[]> = new Observable<EventInput[]>();
  dragTableEvents: EventInput[] = [];
  draggable!: Draggable;

  nrOfEvents: number = 0;
  maxEvents: number = 0;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private globalTableService: GlobalTableService,
    private converter: EventConverterService,
    private cdr: ChangeDetectorRef,
  ) {

    if (this.globalTableService.currentTimeTable) {
      this.selectedTimeTable$.next(this.globalTableService.currentTimeTable);
    } else {
      this.selectedTimeTable$.next({
        id: 0,
        semester: '',
        year: 0,
        status: '',
        roomTables: [],
        courseSessions: [],
        createdAt: '',
        updatedAt: ''
      });
    }

    console.log(this.selectedTimeTable$)

    this.subscriptions.add(
      this.selectedTimeTable$.subscribe(r => {
        if (r) {
          this.availableRooms = r.roomTables;
          this.selectedRoom = r.roomTables.length > 0 ? r.roomTables[0] : null;
        }
      })
    );
  }

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  ngAfterViewInit(): void {
    if (this.selectedRoom) {
      this.loadNewRoom(this.selectedRoom);
    }
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
    this.draggable.destroy();
    this.subscriptions.unsubscribe();
  }

  loadNewRoom(newRoom: RoomTableDTO): void {
    if (!newRoom) return;

    this.clearCalendar();
    this.selectedRoom = newRoom;

    this.selectedTimeTable$.subscribe(t => {
      if (t) {
        const placedEvents: EventInput[] = this.reloadNewEvents(newRoom.roomId, t);

        this.maxEvents = placedEvents.length;
        this.dragTableEvents = placedEvents.filter(s => !s.extendedProps?.['assigned']);
        this.nrOfEvents = this.maxEvents - this.dragTableEvents.length;

        this.combinedTableEvents = of([
          ...placedEvents,
          ...this.reloadNewBackgroundEvents(this.selectedRoom!)
        ]);
      }
    });
  }

  reloadNewEvents(roomId: string, table: TimeTableDTO): EventInput[] {
    return table.courseSessions
      .filter(s => s.roomTable?.roomId === roomId)
      .map((s: CourseSessionDTO) =>
        this.converter.convertTimingToEventInput(s, true)
      );
  }

  reloadNewBackgroundEvents(room: RoomTableDTO): EventInput[] {
    let combinedEvents: EventInput[] = [];
    room.timingConstraints?.forEach(t => {
      combinedEvents.push(this.converter.convertTimingEventInput(t));
    });

    return combinedEvents;
  }

  clearCalendar() {
    this.calendarComponent.getApi().removeAllEvents();
  }

  drop(arg: DropArg) {
    console.log("drop event", arg);
    this.dragTableEvents =
      this.dragTableEvents.filter(
        e => e.id !== arg.draggedEl.getAttribute('data-id'))
  }

  eventClick(arg: EventClickArg) {
    this.dragTableEvents.push(this.converter.convertImplToInput(arg.event));
    arg.event.remove();
  }

  protected readonly JSON = JSON;
}
