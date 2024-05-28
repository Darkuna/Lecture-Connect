import {ChangeDetectorRef, Component, OnDestroy, OnInit, signal} from '@angular/core';
import {MenuItem} from "primeng/api";
import {CalendarOptions, DateSelectArg, EventApi, EventClickArg} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {createEventId, INITIAL_EVENTS} from "./event-utils";
import {TimeTable} from "../../../assets/Models/time-table";
import {Semester} from "../../../assets/Models/enums/semester";
import {Router} from "@angular/router";
import {TableShareService} from "../../services/table-share.service";
import {Subscription} from "rxjs";
import {GlobalTableService} from "../../services/global-table.service";
import {TimeTableNames} from "../../../assets/Models/time-table-names";
import {TmpTimeTable} from "../../../assets/Models/tmp-time-table";
import {LocalStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',

})
export class HomeComponent implements OnInit, OnDestroy {
  availableTableSubs: Subscription;
  availableTables!: TimeTableNames[];
  shownTableDD!: TimeTableNames;
  tmpTable!: TmpTimeTable;

  selectedTimeTable!: TimeTable;
  responsiveOptions: any[] | undefined;
  items: MenuItem[] | undefined;
  showNewTableDialog: boolean = false;

  constructor(
    private cd: ChangeDetectorRef,
    private router: Router,
    private shareService: TableShareService,
    private globalTableService: GlobalTableService,
    private localStorage: LocalStorageService,
  ) {
    this.availableTableSubs = this.globalTableService.getTimeTableByNames().subscribe(
      data => this.availableTables = [...data]
    );
  }


  ngOnDestroy(): void {
    this.availableTableSubs.unsubscribe();
  }

  showTableDialog() {
    this.showNewTableDialog = true;
    this.shownTableDD = new TimeTableNames();
  }

  hideTableDialog() {
    this.showNewTableDialog = false;
  }

  loadSpecificTable(){
    this.selectedTimeTable = this.globalTableService.
    getSpecificTimeTable(this.shownTableDD.id, this.selectedTimeTable);
  }

  isTmpTableAvailable(){
    return this.localStorage.retrieve('tmptimetable') !== null;
  }

  loadTmpTable(){
    this.shareService.selectedTable = this.localStorage.retrieve("tmptimetable");
    this.router.navigate(['/wizard']);
  }

  createNewTable() {
    this.tmpTable = new TmpTimeTable();
    this.tmpTable.tableName = this.shownTableDD;
    this.tmpTable.courseTable = [];
    this.tmpTable.roomTables = [];

    //TODO backend call to get id
    this.tmpTable.tableName.id = 123;
    this.hideTableDialog();

    this.shareService.selectedTable = this.tmpTable;
    this.router.navigate(['/wizard']);
  }

  editTable() {
    if (this.shownTableDD) {
      this.selectedTimeTable = this.shownTableDD;
    }
    this.showTableDialog();
  }

  getSemesterOptions() {
    return Object.keys(Semester).filter(k => isNaN(Number(k)));
  }

  calendarVisible = signal(true);
  calendarOptions = signal<CalendarOptions>({
    events: undefined,
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
    initialEvents: INITIAL_EVENTS,
    weekends: false,
    editable: false,
    selectable: false,
    selectMirror: true,
    dayMaxEvents: true,
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventsSet: this.handleEvents.bind(this),
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
    slotEventOverlap: false,
    nowIndicator: false,
  });

  currentEvents = signal<EventApi[]>([]);

  handleDateSelect(selectInfo: DateSelectArg) {
    const title = prompt('Please enter a new title for your event');
    const calendarApi = selectInfo.view.calendar;

    calendarApi.unselect(); // clear date selection

    if (title) {
      calendarApi.addEvent({
        id: createEventId(),
        title,
        start: selectInfo.startStr,
        end: selectInfo.endStr,
        allDay: selectInfo.allDay
      });
    }
  }

  handleEventClick(clickInfo: EventClickArg) {
    if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      clickInfo.event.remove();
    }
  }

  handleEvents(events: EventApi[]) {
    this.currentEvents.set(events);
    this.cd.detectChanges(); // workaround for pressionChangedAfterItHasBeenCheckedError
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
            icon: 'pi pi-microchip'
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
            icon: 'pi pi-folder'
          },
          {
            label: 'Export Plan (each)',
            icon: 'pi pi-folder-open'
          }
        ]
      },
      {separator: true},
      {
        label: 'Data',
        items: [
          {
            label: 'Edit Room list',
            icon: 'pi pi-warehouse'
          },
          {
            label: 'edit Course list',
            icon: 'pi pi-book'
          },
          {
            label: 'Filter',
            icon: 'pi pi-filter'
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
  }

}
