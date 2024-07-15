import {Component, OnDestroy, OnInit, signal, ViewChild} from '@angular/core';
import {MenuItem} from "primeng/api";
import {CalendarOptions, EventInput} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {TimeTable} from "../../../assets/Models/time-table";
import {Semester} from "../../../assets/Models/enums/semester";
import {Router} from "@angular/router";
import {TableShareService} from "../../services/table-share.service";
import {Observable, Subscription} from "rxjs";
import {GlobalTableService} from "../../services/global-table.service";
import {TimeTableNames} from "../../../assets/Models/time-table-names";
import {TmpTimeTable} from "../../../assets/Models/tmp-time-table";
import {LocalStorageService} from "ngx-webstorage";
import {EventConverterService} from "../../services/event-converter.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  availableTableSubs: Subscription;
  availableTables!: TimeTableNames[];
  shownTableDD!: TimeTableNames;

  tmpTable!: TmpTimeTable;
  selectedTimeTable!: Observable<TimeTable>;
  combinedTableEvents: EventInput[] = [{
    "daysOfWeek": ["4"],
    "title": "PS Informationstheorie und Kryptologie - Group 5",
    "id": "-179",
    "startTime": "09:15:00",
    "endTime": "10:00:00"
  }];

  responsiveOptions: any[] | undefined;
  items: MenuItem[] | undefined;
  showNewTableDialog: boolean = false;

  constructor(
    private router: Router,
    private shareService: TableShareService,
    private globalTableService: GlobalTableService,
    private localStorage: LocalStorageService,
    private converter: EventConverterService,
  ) {
    this.availableTableSubs = this.globalTableService.getTimeTableByNames().subscribe(
      data => this.availableTables = [...data]
    );
  }


  ngOnDestroy(): void {
    this.availableTableSubs.unsubscribe();
  }

  showTableDialog() {
    this.shownTableDD = new TimeTableNames();
    this.showNewTableDialog = true;
  }

  hideTableDialog() {
    this.showNewTableDialog = false;
  }

  loadSpecificTable() {
    this.selectedTimeTable = this.globalTableService.getSpecificTimeTable(this.shownTableDD.id);

    this.selectedTimeTable.subscribe((timeTable: TimeTable) => {
      let sessions= timeTable.courseSessions
      sessions?.forEach(t => {
        let converted = this.converter.convertTimingToEventInput(t);
        this.combinedTableEvents.push(converted);
      })
    });
    this.calendarComponent.getApi().removeAllEvents();
    this.updateCalendar('events', this.combinedTableEvents);
  }

  isTmpTableAvailable() {
    return this.localStorage.retrieve('tmptimetable') !== null;
  }

  loadTmpTable() {
    if (this.isTmpTableAvailable()) {
      this.shareService.selectedTable = this.localStorage.retrieve("tmptimetable");
      this.router.navigate(['/wizard']);
    }
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

  getSemesterOptions() {
    return Object.keys(Semester).filter(k => isNaN(Number(k)));
  }

  calendarVisible = signal(true);
  calendarOptions = signal<CalendarOptions>({
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
  });

  updateCalendar(calendarOption: string, value: any) {
    this.calendarOptions.update(val => ({
      ...val, [calendarOption]: value
    }));
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
            label: 'edit Course list',
            icon: 'pi pi-book',
            routerLinkActiveOptions: ['/course-selection']
          },
          {
            label: 'Edit Room list',
            icon: 'pi pi-warehouse'
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

  protected readonly print = print;
}
