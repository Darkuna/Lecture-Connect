import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CalendarOptions, EventApi, EventChangeArg, EventInput, EventMountArg} from "@fullcalendar/core";
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
import {MenuItem, MessageService} from "primeng/api";
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
  @ViewChild('cm') contextMenu!: ContextMenu;

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
    dragScroll: true,
    height: 'auto',
    eventBackgroundColor: '#666666',
    eventBorderColor: '#050505',
    eventTextColor: 'var(--system-color-primary-white)',
    slotMinTime: this.converter.formatTime(this.tmpStartDate),
    slotMaxTime: this.converter.formatTime(this.tmpEndDate),
    slotDuration: this.converter.formatTime(this.tmpDuration),
    slotLabelInterval: this.converter.formatTime(this.tmpSlotInterval),
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: true,
    slotEventOverlap: true,
    nowIndicator: false,
    drop: this.drop.bind(this),
    eventReceive: this.eventReceive.bind(this),
    eventChange: this.eventChange.bind(this),
    eventDidMount: this.eventDidMount.bind(this),
    eventAllow: this.eventAllow.bind(this),
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

  items: MenuItem[] = [];
  rightClickEvent: EventMountArg | null = null;
  firstSearchedEvent: EventInput | null = null;
  dirtyData: boolean = false;
  searchCourse: string = '';

  constructor(
    private globalTableService: GlobalTableService,
    private converter: EventConverterService,
    private editorService: EditorService,
    private router: Router,
    private messageService: MessageService,
    private cd: ChangeDetectorRef
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
      { label: 'fix Course', icon: 'pi pi-copy', command: () => { this.changeSessionBlockState() }},
      { label: 'unassign Course', icon: 'pi pi-copy', command: () => { this.unassignCourse() } },
      { label: 'add Group', icon: 'pi pi-file-edit', disabled: true },
      { label: 'remove Group', icon: 'pi pi-file-edit', disabled: true },
      { label: 'split Course', icon: 'pi pi-file-edit', disabled: true }
    ]  }

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
    this.allEvents = this.converter.convertMultipleCourseSessions(this.timeTable.courseSessions, 'editor');
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
    this.dirtyData = false;
    this.editorService.pushSessionChanges(this.timeTable.id, this.timeTable.courseSessions)
      .subscribe(s => this.timeTable.courseSessions = s);

    this.globalTableService.getSpecificTimeTable(this.timeTable.id);
  }

  saveAndGoHome(){
    this.saveChanges();

    this.router.navigate(['/user/home']).catch(message => {
      this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
    });
  }

  unassignCourse(){
    if(this.rightClickEvent?.event.title !== 'BLOCKED' && this.rightClickEvent?.event.title !== 'COMPUTER_SCIENCE') {
      const updatedSession = this.updateSession(this.rightClickEvent?.event! , false)!;
      updatedSession.roomTable = null;
      updatedSession.timing = null;
      this.dirtyData = true;

      this.dragTableEvents.push(
        this.converter.convertTimingToEventInput(updatedSession, 'editor')
      );

      this.rightClickEvent?.event.remove();
      this.nrOfEvents -= 1;
    }
  }

  getItemMenuOptions() : void {
    const session = this.timeTable.courseSessions
      .find(s => s.id.toString() === this.rightClickEvent?.event.id);

    if(session && session.fixed){
      this.items[0].label = 'free Course';
    } else {
      this.items[0].label = 'fix Course';
    }
  }

  drop(arg: DropArg) {
    this.dragTableEvents =
      this.dragTableEvents.filter(
        e => e.id !== arg.draggedEl.getAttribute('data-id'))
    this.nrOfEvents += 1;
    this.dirtyData = true;
  }

  eventReceive(args: EventReceiveArg){
    this.updateSession(args.event, true);
    this.dirtyData = true;
  }

  eventChange(args: EventChangeArg){
    this.updateSession(args.event, true);
    this.dirtyData = true;
  }

  eventDidMount(arg: EventMountArg){
    arg.el.addEventListener("contextmenu", (jsEvent)=>{
      jsEvent.preventDefault()
      this.rightClickEvent = arg;
    })
  }

  eventAllow(args: any):boolean{
    return args.start.getHours() > 7 && args.end.getHours() < 23;
  }

  private updateAllEventsList(newSessionID: string, roomTable: string | null){
    this.allEvents.find(e => e['id'] === newSessionID)!['description'] = roomTable;
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

      const room = assigned ? this.selectedRoom?.roomId! : null;
      this.updateAllEventsList(session.id.toString(), room);
    }
    return session ;
  }

  changeSessionBlockState(){
    const session = this.timeTable.courseSessions
      .find(s => s.id.toString() === this.rightClickEvent?.event.id);

    if(session){
      this.rightClickEvent?.event.setProp('editable', session.fixed);
      session.fixed = !session.fixed

      let color = '#666666';
      if(session.fixed){
        color = '#7a4444';
      }

      this.rightClickEvent?.event.setProp('backgroundColor', color);
    }
  }

  changeRoom(event:EventInput){
    if(event){
      const room = this.timeTable.roomTables
        .find(r => r.roomId === event['description']);

      if(room && this.selectedRoom === room){
        this.messageService.add({severity: 'info', summary: 'Info', detail: 'the course is in the current room'});
      } else {
        this.loadNewRoom(room!);
      }
    }
  }

  canDeactivate(): boolean {
    if (this.dirtyData) {
      return confirm('You have unsaved changes. Do you really want to leave?');
    }
    return true; // If no unsaved changes, allow navigation
  }


  // Filtered events based on search term
  get filteredEvents() {
    if (!this.searchCourse) {
      return this.dragTableEvents;  // Return all events if there's no search term
    }

    const lowerSearchTerm = this.searchCourse.toLowerCase();

    // Filter events where the title matches the search term
    return this.dragTableEvents.filter(event =>
      event.title!.toLowerCase().includes(lowerSearchTerm)
    );
  }
}
