import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
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
import {ConfirmationService, MenuItem, MessageService} from "primeng/api";
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";
import {EditorService} from "../../services/editor.service";
import {Router} from "@angular/router";
import {ContextMenu} from "primeng/contextmenu";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent implements AfterViewInit, OnInit,OnDestroy{
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @ViewChild('external') external!: ElementRef;
  @ViewChild('cm') contextMenu!: ContextMenu

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
    eventChange: this.eventChange.bind(this),
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

  items: MenuItem[] | undefined;
  lastSelection: EventClickArg | null = null;
  currentSelection: EventClickArg | null = null;

  constructor(
    private globalTableService: GlobalTableService,
    private converter: EventConverterService,
    private editorService: EditorService,
    private router: Router,
    private messageService: MessageService,
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

  ngOnInit(): void{
    this.items = [
      { label: 'unassign Course', icon: 'pi pi-copy',command: () => {this.unassignCourse()} },
      { label: 'fix Course', icon: 'pi pi-copy',command: () => {this.changeSessionBlockState()} },
      { label: 'free Course', icon: 'pi pi-copy' },
      { label: 'add Group', icon: 'pi pi-file-edit', disabled:true},
      { label: 'remove Group', icon: 'pi pi-file-edit', disabled:true },
      { label: 'split Course', icon: 'pi pi-file-edit', disabled:true},
    ];
  }

  ngAfterViewInit(): void {
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
    //this.saveChanges();
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
    this.editorService.pushSessionChanges(this.timeTable.id, this.timeTable.courseSessions);
  }

  saveAndGoHome(){
    this.saveChanges();

    this.router.navigate(['/home']).catch(message => {
      this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
    });
  }

  unassignCourse(){
    if(this.currentSelection?.event.title !== 'BLOCKED' && this.currentSelection?.event.title !== 'COMPUTER_SCIENCE') {
      const updatedSession = this.updateSession(this.currentSelection?.event! , false)!;
      updatedSession.timing = null;

      this.dragTableEvents.push(
        this.converter.convertTimingToEventInput(updatedSession)
      );

      this.currentSelection?.event.remove();
      this.nrOfEvents -= 1;
    }
  }

  drop(arg: DropArg) {
    this.dragTableEvents =
      this.dragTableEvents.filter(
        e => e.id !== arg.draggedEl.getAttribute('data-id'))
    this.nrOfEvents += 1;
  }

  eventClick(args: EventClickArg) {
    this.lastSelection = this.currentSelection;
    this.lastSelection?.event.setProp('borderColor', '#666666');

    this.currentSelection = args;
    args.event.setProp('borderColor', 'var(--system-color-primary-orange)');
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

  changeSessionBlockState(){
    const session = this.timeTable.courseSessions
      .find(s => s.id.toString() === this.currentSelection?.event.id);

    if(session){
      console.log(session);
      session.fixed = session.fixed!

      console.log(session);
      this.currentSelection?.event.setProp('editable', session.fixed);
      this.lastSelection?.event.setProp('borderColor', '#666666');
      if(session.fixed){
        this.currentSelection?.event.setProp('backgroundColor','#666666');
      } else {
        this.currentSelection?.event.setProp('backgroundColor','#5D6B5B');
      }
    }
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

  onHide(){
    this.currentSelection = null;
  }
}
