import {
  AfterViewChecked,
  AfterViewInit, ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {ConfirmationService, MenuItem, MessageService} from "primeng/api";
import {CalendarOptions, EventClickArg, EventInput} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {Semester} from "../../../assets/Models/enums/semester";
import {Router} from "@angular/router";
import {TableShareService} from "../../services/table-share.service";
import {BehaviorSubject, catchError, firstValueFrom, from, Observable, of, Subscription, toArray} from "rxjs";
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
import {CourseSessionDTO} from "../../../assets/Models/dto/course-session-dto";
import { jsPDF } from 'jspdf';
import html2canvas from 'html2canvas';
import {ProgressService} from "../../services/progress.service";
import {TableLogComponent} from "../table-log/table-log.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
  availableTableSubs: Subscription;
  availableTables!: TimeTableNames[];
  shownTableDD: TimeTableNames | null = null;

  creationTable!: TmpTimeTable;
  selectedTimeTable$: Observable<TimeTableDTO> | null = null;
  selectedTableSub: Subscription | null = null;
  private combinedTableEventsSubject: BehaviorSubject<EventInput[]> = new BehaviorSubject<EventInput[]>([]);
  combinedTableEvents: Observable<EventInput[]> = this.combinedTableEventsSubject.asObservable();

  showNewTableDialog: boolean = false;
  items: MenuItem[] = [];

  exportTitle = '';
  displayTitlePrompt: boolean = false;

  lastSearchedEvent: EventImpl | null = null;
  firstSearchedEvent: EventImpl | null = null;

  @ViewChild('calendar', {read: ElementRef}) calendarElement!: ElementRef;
  @ViewChild('calendarContextMenu') calendarContextMenu! : CalendarContextMenuComponent;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @ViewChild('tableLog') tableLog!: TableLogComponent;

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

  changeCalendarView: boolean = false;
  calendarOptions :CalendarOptions = {
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
    eventClick: this.showHoverDialog.bind(this),
  };

  constructor(
    private router: Router,
    private shareService: TableShareService,
    private globalTableService: GlobalTableService,
    private localStorage: LocalStorageService,
    protected converter: EventConverterService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private progressService: ProgressService,
    private cd: ChangeDetectorRef,
  ) {
    this.availableTableSubs = this.globalTableService.getTimeTableByNames().subscribe({
      next: (data) => {
        this.availableTables = data;
        this.shownTableDD = this.availableTables
          .find(t => t.id === this.globalTableService.tableId) ??  this.availableTables[0];

        this.loadSpecificTable();
      }
  });
  }

  ngAfterViewInit(): void {
    this.calendarContextMenu.calendarComponent = this.calendarComponent;
  }

  ngOnDestroy(): void {
    this.availableTableSubs.unsubscribe();
    this.selectedTableSub?.unsubscribe();
  }

  ngAfterViewChecked() {
    this.cd.detectChanges();
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

    this.selectedTimeTable$!.subscribe((timeTable: TimeTableDTO) => {
      let sessions = timeTable.courseSessions;
      from(sessions!).pipe(
        this.converter.convertCourseSessionToEventInput('home'),
        toArray(),
        catchError(() => {
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

    this.selectedTimeTable$ = this.globalTableService.getSpecificTimeTable(this.shownTableDD!.id);
    this.updateCalendarEvents();
  }

  unselectTable(){
    this.globalTableService.unselectTable();
    this.shownTableDD = null;
    this.selectedTimeTable$ = null;

    this.clearCalendar();
  }

  changeCalenderEventView(){
    let eventMaxValue: string = '2';

    if (this.changeCalendarView){
      eventMaxValue = 'null';
    }

    this.updateCalendar('eventMaxStack', eventMaxValue);
  }

  isTmpTableAvailable(): TmpTimeTable {
    return this.localStorage.retrieve('tmptimetable');
  }

  loadTmpTable() {
    let tmpTable = this.isTmpTableAvailable();
    if (tmpTable !== null) {
      this.shareService.selectedTable = tmpTable;
      this.router.navigate(['/user/wizard']);
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
      this.selectedTimeTable$ = this.globalTableService.getScheduledTimeTable(this.shownTableDD!.id);
      this.updateCalendarEvents();

      this.messageService.add({severity: 'success', summary: 'Updated Scheduler', detail: 'algorithm was applied successfully'});
    } else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }
  }

  onCalendarClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;

    if (!target.closest('.fc-event')) {
      this.calendarContextMenu.closeDialog();
    }
  }

  removeAll(){
    if(this.shownTableDD){
      this.confirmationService.confirm(
        {
          message: 'Are you sure that you want to proceed?',
          header: 'Confirmation',
          icon: 'pi pi-exclamation-triangle',
          acceptIcon:"none",
          rejectIcon:"none",
          accept: () => {
            this.selectedTimeTable$ = this.globalTableService.removeAll(this.shownTableDD!.id);
            this.updateCalendarEvents();

            this.messageService.add({severity: 'success', summary: 'Updated Scheduler', detail: 'cleared calendar'});
          },
          reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
          }
        }
      );
    } else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }
  }

  removeCollisions(){
    if (this.shownTableDD) {
      this.selectedTimeTable$ = this.globalTableService.removeCollisions(this.shownTableDD.id);
      this.updateCalendarEvents();
    }
  }

  applyCollisionCheck() {
    if (this.shownTableDD) {
      this.globalTableService.getCollisions(this.shownTableDD.id).subscribe({
        next: (collision: CourseSessionDTO[]) => {
          if (collision.length === 0) {
            this.messageService.add({severity: 'success', summary: 'No collisions', detail: 'All collisions checks were successful'})}
          else {
            this.calendarContextMenu.colorCollisionEvents(collision);
            this.messageService.add({severity: 'warn', summary: `Collisions found`, detail: `Number of collisions: ${collision.length}`});
          }
        },

        error: err => {
          this.messageService.add({severity: 'error', summary: 'Error occurred', detail: err});
        }
      });
    } else {
      this.messageService.add({severity: 'info', summary: 'missing resources', detail: 'there is currently no table selected!'});
    }

  }

  promptTitleAndExport(): void {
    this.displayTitlePrompt = true;
  }

  exportCalendarAsPDF() {
    if (!this.exportTitle) {
      this.messageService.add({ severity: 'warn', summary: 'ERROR', detail: 'Change the title' });
      return;
    }

    const calendarHTMLElement = this.calendarElement.nativeElement as HTMLElement;
    this.adjustEventFontSize('10px');

    html2canvas(calendarHTMLElement, { scale: 2 }).then(canvas => {
      const imgData = canvas.toDataURL('image/jpeg', 0.8);

      const pdf = new jsPDF('landscape', 'mm', 'a4');
      const imgWidth = 297;
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      const textWidth = pdf.getStringUnitWidth(this.exportTitle.toString()) * pdf.getFontSize() / pdf.internal.scaleFactor;
      const xPosition = (imgWidth - textWidth) / 2;
      pdf.text(this.exportTitle.toString(), xPosition, 10);

      pdf.addImage(imgData, 'JPEG', 0, 15, imgWidth, imgHeight);
      pdf.save('calendar_export.pdf');

      this.adjustEventFontSize('');
      this.displayTitlePrompt = false;
    });
  }

  refreshCalendar(events: EventInput[]): void {
    this.clearCalendar();
    this.combinedTableEventsSubject.next(events);
  }

  async exportCalendarPerRoom(): Promise<void> {
    const pdf = new jsPDF('landscape', 'mm', 'a4');
    pdf.setFontSize(16);

    const timeTable = await firstValueFrom(this.selectedTimeTable$!);
    const rooms = timeTable.roomTables;
    const allEvents = this.converter.convertMultipleCourseSessions(timeTable.courseSessions, 'home');
    this.progressService.progressMaxCounter = allEvents.length;

    let courseCounter = 0;
    let calendarElement, canvas, imgData, imgHeight, textWidth;

    for(const [index, room] of rooms.entries()) {
      const roomEvents = allEvents.filter(e => e['description'] === room.roomId);
      courseCounter = roomEvents.length;

      if(courseCounter == 0) continue

      this.refreshCalendar(roomEvents);
      await new Promise(resolve => setTimeout(resolve, 200));

      calendarElement = this.calendarElement.nativeElement as HTMLElement;
      canvas = await html2canvas(calendarElement, { scale: 2 });

      imgData = canvas.toDataURL('image/jpeg', 1);
      imgHeight = (canvas.height * 270) / canvas.width;

      textWidth = pdf.getStringUnitWidth(room.roomId) * pdf.getFontSize() / pdf.internal.scaleFactor;

      pdf.text(room.roomId, (pdf.internal.pageSize.width - textWidth) / 2, 10);
      pdf.addImage(imgData, 'JPEG', 10, 20, 270, imgHeight);

      if (index < rooms.length - 1) pdf.addPage();
      this.progressService.progressCounter = courseCounter;
    }

    pdf.save('calendar_per_room.pdf');
    this.progressService.finishedLoading();
    this.updateCalendarEvents();
  }

  adjustEventFontSize(fontSize: string) {
    const eventElements = document.querySelectorAll('.fc-event-title, .fc-event-time');

    eventElements.forEach((element: any) => {
      element.style.fontSize = fontSize || '12px';
      element.style.lineHeight = fontSize ? '1.2' : '';
      element.style.whiteSpace = 'pre-wrap';
      element.style.wordWrap = 'break-word';
    });
  }

  createNewTable() {
    this.creationTable.id = 999999;
    this.creationTable.status = Status.NEW;
    this.creationTable.courseTable = [];
    this.creationTable.roomTables = [];

    this.hideTableDialog();

    this.shareService.selectedTable = this.creationTable;
    this.router.navigate(['/user/preselection']).catch(message => {
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

  showHoverDialog(event: EventClickArg): void {
    if (!this.calendarContextMenu.activateLens) return;

    this.calendarContextMenu.hoverEventInfo = event;
    this.calendarContextMenu.showHoverDialogBool = true;

    this.calendarContextMenu.hoverEventInfo!.event.setProp("backgroundColor", '#666666');
    this.calendarContextMenu.tmpPartners.forEach(e => e.setProp('backgroundColor', '#666666'));
    this.calendarContextMenu.tmpPartners = this.calendarContextMenu.colorPartnerEvents(event.event, '#ad7353');
    event.event.setProp("backgroundColor", 'var(--system-color-primary-red)');
  }

  updateCalendar(calendarOption: any, value: string) {
    if(value === '00:00:00'){
      value = '00:00:05';
    }
    this.calendarComponent.getApi().setOption(calendarOption, value);
  }

  getCalendarEvents(): EventImpl[]{
    return this.calendarComponent ? this.calendarComponent.getApi().getEvents() : [];
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

  private loadChanges(){
    this.tableLog.tableName = this.shownTableDD;
    this.tableLog.showChanges(this.shownTableDD!.id);
  }

  ngOnInit() {
    this.items = [
      {
        label: 'Editor',
        expanded: true,
        items: [
          {
            label: 'Edit Mode',
            icon: 'pi pi-pen-to-square',
            command: () => this.redirectToSelection('/user/editor')
          },
          {
            label: 'Auto Fill',
            icon: 'pi pi-microchip',
            badge: '3',
            command: () => this.applyAlgorithm(),
          },
          {
            label: 'Remove All',
            icon: 'pi pi-delete-left',
            command: () => this.removeAll()
          },
          {
            label: 'Collision Check',
            icon: 'pi pi-check-circle',
            command: () => this.applyCollisionCheck()
          },
          {
            label: 'Remove Collisions',
            icon: 'pi pi-eraser',
            command: () => this.removeCollisions()
          },
        ]
      },
      {
        label: 'Rooms',
        expanded: true,
        items: [
          {
            label: 'Add new Rooms',
            icon: 'pi pi-warehouse',
            command: () => this.redirectToSelection('/user/tt-rooms')
          },

          {
            label: 'Edit shown Rooms',
            icon: 'pi pi-warehouse',
            command: () => this.redirectToSelection('/user/tt-rooms')
          },
        ]
      },
      {
        label: 'Scheduling',
        expanded: true,
        items: [
          {
            label: 'last Changes',
            icon: 'pi pi-comments',
            command: () => { this.loadChanges() }
          }
        ]
      },
      {
        label: 'Print',
        expanded: true,
        items: [
          {
            label: 'Export Plan (Current)',
            icon: 'pi pi-folder-open',
            command: () => this.promptTitleAndExport()
          },
          {
            label: 'Export Plan (Rooms)',
            icon: 'pi pi-folder',
            command: () => this.exportCalendarPerRoom()
          }
        ]
      },
    ];
  }
}
