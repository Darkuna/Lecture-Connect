import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ConfirmationService, MenuItem, MessageService} from "primeng/api";
import {CalendarOptions, EventClickArg, EventInput} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {Semester} from "../../../assets/Models/enums/semester";
import {Router} from "@angular/router";
import {TableShareService} from "../../services/table-share.service";
import {BehaviorSubject, catchError, from, Observable, of, Subscription, toArray} from "rxjs";
import {GlobalTableService} from "../../services/global-table.service";
import {TimeTableNames} from "../../../assets/Models/time-table-names";
import {TmpTimeTable} from "../../../assets/Models/tmp-time-table";
import {LocalStorageService} from "ngx-webstorage";
import {EventConverterService} from "../../services/converter/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {Status} from "../../../assets/Models/enums/status";
import {TimeTableDTO} from "../../../assets/Models/dto/time-table-dto";
import {CalendarContextMenuComponent} from "./calendar-context-menu/calendar-context-menu.component";
import {EventImpl} from "@fullcalendar/core/internal";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  availableTableSubs: Subscription;
  availableTables!: TimeTableNames[];
  shownTableDD: TimeTableNames | null = null;

  creationTable!: TmpTimeTable;
  selectedTimeTable: Observable<TimeTableDTO> | null = null;
  private combinedTableEventsSubject: BehaviorSubject<EventInput[]> = new BehaviorSubject<EventInput[]>([]);
  combinedTableEvents: Observable<EventInput[]> = this.combinedTableEventsSubject.asObservable();

  responsiveOptions: any[] | undefined;
  items: MenuItem[] = [];
  showNewTableDialog: boolean = false;
  position: any = 'topleft';

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

  lastSearchedEvent: EventImpl | null = null;
  firstSearchedEvent: EventImpl | null = null;

  @ViewChild('calendarContextMenu') calendarContextMenu! : CalendarContextMenuComponent;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  calendarOptions: CalendarOptions= ({
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
      slotMinTime: this.formatTime(this.tmpStartDate),
      slotMaxTime: this.formatTime(this.tmpEndDate),
      slotDuration: this.formatTime(this.tmpDuration),
      slotLabelInterval: this.formatTime(this.tmpSlotInterval),
      dayHeaderFormat: {weekday: 'long'},
      eventOverlap: true,
      slotEventOverlap: true,
      nowIndicator: false,
      eventClick: this.showHoverDialog.bind(this),
      eventMouseLeave: this.hideHoverDialog.bind(this),
    }
  );

  constructor(
    private router: Router,
    private shareService: TableShareService,
    private globalTableService: GlobalTableService,
    private localStorage: LocalStorageService,
    private converter: EventConverterService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
  ) {
    this.availableTableSubs = this.globalTableService.getTimeTableByNames().subscribe({
      next: (data) => {
        this.availableTables = [...data];
        this.shownTableDD = this.availableTables[0];
        this.loadSpecificTable();
      }
  });
  }

  ngAfterViewInit(): void {
    this.calendarContextMenu.calendarComponent = this.calendarComponent;
  }

  ngOnDestroy(): void {
    this.availableTableSubs.unsubscribe();
  }

  showTableDialog() {
    this.creationTable = new TmpTimeTable();
    this.showNewTableDialog = true;
  }

  hideTableDialog() {
    this.showNewTableDialog = false;
  }

  clearCalendar(){
    this.calendarComponent.getApi().removeAllEvents();
  }

  updateCalendarEvents(){
    this.clearCalendar();

    this.selectedTimeTable!.subscribe((timeTable: TimeTableDTO) => {
      let sessions = timeTable.courseSessions;
      from(sessions!).pipe(
        this.converter.convertCourseSessionToEventInput(),
        toArray(),
        catchError(error => {
          console.error('Error converting sessions:', error);
          return of([]);
        })
      ).subscribe(events => {
        this.combinedTableEventsSubject.next(events);
      });
    });
  }

  loadSpecificTable() {
    if(!this.shownTableDD!.id){
      return;
    }

    this.selectedTimeTable = this.globalTableService.getSpecificTimeTable(this.shownTableDD!.id);
    this.updateCalendarEvents();
  }

  unselectTable(){
    this.globalTableService.unselectTable();
    this.shownTableDD = null;
    this.selectedTimeTable = null;

    this.clearCalendar();
  }

  tableIsSelected():boolean{
    return this.selectedTimeTable === null;
  }

  isTmpTableAvailable(): TmpTimeTable {
    return this.localStorage.retrieve('tmptimetable');
  }

  loadTmpTable() {
    let tmpTable = this.isTmpTableAvailable();
    if (tmpTable !== null) {
      this.shareService.selectedTable = tmpTable;
      this.router.navigate(['/wizard']);
    }
  }

  deleteUnfinishedTable(){
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all the selected Rooms?', header: 'Confirm', icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.localStorage.clear('tmptimetable');
        this.messageService.add({severity: 'error', summary: 'Deleted Item', detail: 'temporary table got deleted'});
      },
      reject: () => {
        this.messageService.add({severity: 'info', summary: 'Table saved', detail: 'your table was not deleted'});
      }
    });
  }

  applyAlgorithm(){
    if(this.shownTableDD){
      this.selectedTimeTable = this.globalTableService.getScheduledTimeTable(this.shownTableDD!.id);
      this.updateCalendarEvents();

      this.messageService.add({severity: 'success', summary: 'Updated Scheduler', detail: 'algorithm was applied successfully'});
    }
    else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }
  }

  createNewTable() {
    this.creationTable.id = 999999;
    this.creationTable.status = Status.NEW;
    this.creationTable.courseTable = [];
    this.creationTable.roomTables = [];

    this.hideTableDialog();

    this.shareService.selectedTable = this.creationTable;
    this.router.navigate(['/wizard']).catch(message => {
      this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
    });
  }

  getSemesterOptions() {
    return Object.keys(Semester).filter(k => isNaN(Number(k)));
  }

  redirectToSelection(page: string){
    if(this.shownTableDD){
      this.router.navigate([page]).catch(message => {
        this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
      });
    } else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }
  }

  showHoverDialog(event: EventClickArg):void{
    if(this.calendarContextMenu.activateLens){
      this.calendarContextMenu.showHoverDialogBool = true;
      this.calendarContextMenu.hoverEventInfo = event;
      this.calendarContextMenu.tmpPartners = this.calendarContextMenu.colorPartnerEvents(event.event, '#ad7353');
      this.calendarContextMenu.hoverEventInfo.event.setProp("backgroundColor", 'var(--system-color-primary-red)');
    }
  }

  hideHoverDialog():void{
    this.calendarContextMenu.showHoverDialogBool = false;

    if(this.calendarContextMenu.hoverEventInfo){
      this.calendarContextMenu.hoverEventInfo.event.setProp("backgroundColor", '#666666');
      this.calendarContextMenu.tmpPartners.forEach(e => e.setProp('backgroundColor', '#666666'));
    }
    this.calendarContextMenu.hoverEventInfo = null;
  }

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  updateCalendar(calendarOption: any, value: string) {
    console.log(value);
    if(value === '00:00:00'){
      value = '00:00:05';
    }
    this.calendarComponent.getApi().setOption(calendarOption, value);
  }

  getCalendarEvents(): EventImpl[]{
    if(this.calendarComponent){
      return this.calendarComponent.getApi().getEvents();
    } else {
      return [];
    }
  }

  colorSpecificEvent(event: EventImpl){
    this.clearLastEvent();

    this.lastSearchedEvent = this.firstSearchedEvent;
    this.firstSearchedEvent = event;

    if(this.firstSearchedEvent){
      this.firstSearchedEvent.setProp("backgroundColor", '#53682e');
    }
  }

  clearLastEvent(){
    if(this.lastSearchedEvent){
      this.lastSearchedEvent.setProp("backgroundColor", '#666666');
    }
  }

  ngOnInit() {
    this.responsiveOptions = [
      {
        breakpoint: '1199px',
        numVisible: 1,
        numScroll: 1
      },
      {
        breakpoint: '991px',
        numVisible: 2,
        numScroll: 1
      },
      {
        breakpoint: '767px',
        numVisible: 1,
        numScroll: 1
      }
    ];

    this.items = [
      {separator: true},
      {
        label: 'Editor',
        items: [
          {
            label: 'Edit Mode',
            icon: 'pi pi-pen-to-square',
            styleClass: 'tst'
          },
          {
            label: 'Auto Fill',
            icon: 'pi pi-microchip',
            command: () => this.applyAlgorithm()
          },
          {
            label: 'Collision Check',
            icon: 'pi pi-check-circle'
          }
        ]
      },
      {separator: true},
      {
        label: 'Print',
        items: [
          {
            label: 'Export Plan (all)',
            icon: 'pi pi-folder-open'
          },
          {
            label: 'Export Plan (each)',
            icon: 'pi pi-folder'
          }
        ]
      },
      {separator: true},
      {
        label: 'Data',
        items: [
          {
            label: 'Edit Course list',
            icon: 'pi pi-book',
            command: () => this.redirectToSelection('/tt-courses')
          },
          {
            label: 'Edit Room list',
            icon: 'pi pi-warehouse',
            command: () => this.redirectToSelection('/tt-rooms')
          },
          {
            label: 'last Changes',
            icon: 'pi pi-file-edit',
            command: () => this.redirectToSelection('/tt-status')
          }
        ]
      },
      {separator: true},
      {
        label: 'Scheduling',
        items: [
          {
            label: 'define Status',
            icon: 'pi pi-check-square',
          }
        ]
      }
    ];
  }
}
