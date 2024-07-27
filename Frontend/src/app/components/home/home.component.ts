import {Component, ElementRef, OnDestroy, OnInit, signal, ViewChild} from '@angular/core';
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
import {EventImpl} from "@fullcalendar/core/internal";
import {DropdownFilterOptions} from "primeng/dropdown";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;

  availableTableSubs: Subscription;
  availableTables!: TimeTableNames[];
  shownTableDD: TimeTableNames | null = null;
  activateLens: boolean = false;

  creationTable!: TmpTimeTable;
  selectedTimeTable!: Observable<TimeTableDTO>;
  private combinedTableEventsSubject: BehaviorSubject<EventInput[]> = new BehaviorSubject<EventInput[]>([]);
  combinedTableEvents: Observable<EventInput[]> = this.combinedTableEventsSubject.asObservable();

  responsiveOptions: any[] | undefined;
  items: MenuItem[] = [];
  contextItems: MenuItem[] = [];
  showNewTableDialog: boolean = false;
  position: any = 'topleft';

  showHoverDialogBool: boolean = false;
  hoverEventInfo: EventClickArg |null = null;
  tmpPartners : EventImpl[] = [];
  tmpRenderSelection : EventImpl[] = [];
  tmpColorSelection : EventImpl[] = [];

  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();
  showSearchDialog: boolean = false;
  searchedEvent: EventImpl | null = null;
  filterValue: string | undefined = '';

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

  unselectTable(){
    this.globalTableService.unselectTable();
    this.shownTableDD = null;

    this.clearCalendar();
  }

  updateCalendarEvents(){
    this.clearCalendar();

    this.selectedTimeTable.subscribe((timeTable: TimeTableDTO) => {
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
    this.router.navigate(['/wizard']);
  }

  getSemesterOptions() {
    return Object.keys(Semester).filter(k => isNaN(Number(k)));
  }

  loadingOn() {
    this.loadingSubject.next(true);
  }

  loadingOff() {
    this.loadingSubject.next(false);
  }

  calendarVisible = signal(true);
  calendarOptions = signal<CalendarOptions>({
      snapDuration: undefined,
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
      slotMinTime: '07:00:00',
      slotMaxTime: '23:00:00',
      slotDuration: '00:15:00',
      slotLabelInterval: '01:00:00',
      dayHeaderFormat: {weekday: 'long'},
      eventOverlap: true,
      slotEventOverlap: true,
      nowIndicator: false,
      eventClick: this.showHoverDialog.bind(this),
      eventMouseLeave: this.hideHoverDialog.bind(this),}
  );

  renderEventType(type: string){
    this.loadingOn();

    let newItems = this.calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['type'] === type);

    this.tmpRenderSelection = this.tmpRenderSelection.concat(newItems);
    newItems.forEach(e => e.setProp('display', 'none'));

    this.loadingOff();
  }

  showAllEvents(){
    this.loadingOn();
    this.tmpRenderSelection.forEach(e => {
      e.setProp('display', 'auto');
    });

    this.tmpRenderSelection = [];

    this.loadingOff();
  }

  colorEventType(type: string, color: string){
    this.loadingOn();

    let newItems = this.calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['type'] === type);

    this.tmpColorSelection = this.tmpColorSelection.concat(newItems);
    newItems.forEach(e => e.setProp('backgroundColor', color));

    this.loadingOff();
  }

  clearEvents(){
    this.loadingOff();

    this.tmpColorSelection
      .forEach(e => {
        e.setProp('backgroundColor', '#666666');
      });

    this.tmpColorSelection = [];
    this.loadingOff();
  }

  colorPartnerEvents(event: EventClickArg, color: string): EventImpl[]{
    let key = event.event.title.replace(/ - Group \d+$/, '');
    let partner = this.calendarComponent
      .getApi().getEvents()
      .filter(e => e.title.includes(key));

    partner.forEach(e => e.setProp('backgroundColor', color));
    return partner;
  }

  clearAll(){
    this.clearEvents();
    this.showAllEvents();
    this.activateLens = false;
  }

  showHoverDialog(event: EventClickArg){
    if(this.activateLens){
      this.showHoverDialogBool = true;
      this.hoverEventInfo = event;
      this.tmpPartners = this.colorPartnerEvents(event, '#ad7353');

      this.hoverEventInfo.event.setProp("backgroundColor", 'var(--system-color-primary-red)');
    }
  }

  hideHoverDialog(){
    this.showHoverDialogBool = false;

    if(this.hoverEventInfo){
      this.hoverEventInfo.event.setProp("backgroundColor", '#666666');
      this.tmpPartners.forEach(e => e.setProp('backgroundColor', '#666666'));
    }
    this.hoverEventInfo = null;
  }

  changeLensStatus(){
    this.activateLens = !this.activateLens;

    if(this.activateLens){
      this.messageService.add({severity: 'success', summary: 'Hover Mode', detail: 'Lens is activated'});
    } else {
      this.messageService.add({severity: 'error', summary: 'Hover Mode', detail: 'Lens is deactivated'});
    }
  }

  redirectToSelection(page: string){
    if(this.shownTableDD){
      this.router.navigate([page]);
    } else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }
  }

  activateSearchDialog(){
    this.showSearchDialog = true;
  }

  getCalendarEvents(){
    return this.calendarComponent.getApi().getEvents();
  }

  customFilterFunction(event: KeyboardEvent, options: DropdownFilterOptions) {
    if (options && typeof options.filter === 'function') {
      options.filter(event);
    } else {
      console.warn('Filter function is not defined');
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
          }
        ]
      },
      {separator: true},
      {
        label: 'Scheduling',
        items: [
          {
            label: 'define Status',
            icon: 'pi pi-check-square'
          }
        ]
      }
    ];

    this.contextItems = [
      {
        label: 'Filter Groups',
        icon: 'pi pi-filter',
        items: [
          {
            label: 'VO',
            command: () => this.renderEventType('VO') },
          { label: 'VU',
            command: () => this.renderEventType('VU') },
          { label: 'PS',
            command: () => this.renderEventType('PS') },
          { label: 'SE',
            command: () => this.renderEventType('SE') },
          { label: 'SL',
            command: () => this.renderEventType('SL')},
          { label: 'PR',
            command: () => this.renderEventType('PR') },
          { label: 'Clear',
            icon: 'pi pi-trash',
            command: () => this.showAllEvents()
          },
        ],
      },
      {
        label: 'Highlight Groups',
        icon: 'pi pi-filter-fill',
        items: [
          {
            label: 'VO',
            command: () => this.colorEventType('VO', '#C36049') },
          { label: 'VU',
            command: () => this.colorEventType('VU', '#985F53') },
          { label: 'PS',
            command: () => this.colorEventType('PS', '#ED5432') },
          { label: 'SE',
            command: () => this.colorEventType('SE', '#6E544E') },
          { label: 'SL',
            command: () => this.colorEventType('SL', '#433C3B')},
          { label: 'PR',
            command: () => this.colorEventType('PR', '#332927') },
          { label: 'Clear',
            icon: 'pi pi-trash',
            command: () => this.clearEvents()
          },
        ],
      },
      {
        label: 'Seach Course',
        icon: 'pi pi-search',
        command: () => this.activateSearchDialog()
      },
      {
        label: 'Lens ',
        icon: 'pi pi-bullseye',
        command: () => this.changeLensStatus()
      },
      {
        label: 'Clear',
        icon: 'pi pi-trash',
        command: () => this.clearAll()
      }
    ];
  }
}
