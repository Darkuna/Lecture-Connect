import {Component, Input, signal, ViewChild} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {CalendarOptions, DateSelectArg, EventClickArg, EventInput} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import rrulePlugin from '@fullcalendar/rrule'
import {CourseColor} from "../../../../assets/Models/enums/lecture-color";
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {GlobalTableService} from "../../../services/global-table.service";
import {Router} from "@angular/router";
import {TmpTimeTableDTO} from "../../../../assets/Models/dto/tmp-time-table-dto";
import {RoomToDtoConverterService} from "../../../services/converter/room-to-dto-converter.service";
import {CourseToDtoConverterService} from "../../../services/converter/course-to-dto-converter.service";
import {getStatusKey} from "../../../../assets/Models/enums/status";
import {LocalStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-base-selection',
  templateUrl: './base-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class BaseSelectionComponent{
  @Input() globalTable!: TmpTimeTable;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  selectedRoom: Room | null = null;

  protected readonly CourseColor = CourseColor;
  lastUsedColor: CourseColor = CourseColor.COMPUTER_SCIENCE;

  activeDialog: boolean = true;
  showTimeDialog: boolean = false;
  dataSelectStart!: Date;
  dataSelectEnd!: Date;

  //to be saved in the config file
  tmpStartDate: Date = new Date('2024-07-10T07:00:00');
  tmpEndDate: Date = new Date('2024-07-10T23:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:15:00');
  tmpSlotInterval: Date = new Date('2024-07-10T01:00:00');

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private globalTableService: GlobalTableService,
    private router: Router,
    private roomConverter: RoomToDtoConverterService,
    private courseConverter: CourseToDtoConverterService,
    private localStorage: LocalStorageService
  ) { }

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  getButtonStyle(color: string): { [key: string]: string } {
    return {
      'background-color': color,
      'border-color': color,
      'padding': '.5rem',
      'width': '-webkit-fill-available'
    };
  }

  setCurrentColor(color: CourseColor){
    this.lastUsedColor = color;
    this.messageService.add({severity:'info', summary:'Color Mode', detail:'color changed!'});
  }

  getCourseType(colorCode: string): string | undefined {
    for (const color in CourseColor) {
      if (CourseColor[color as keyof typeof CourseColor] === colorCode) {
        return color;
      }
    }
    return undefined;
  }

  roomIsSelected(): boolean {
    return this.selectedRoom !== null ;
  }

  showDialog(){
    this.showTimeDialog = true;
  }

  hideDialog(){
    this.showTimeDialog = false;
  }


  calendarOptions = signal<CalendarOptions>({
    plugins: [
      rrulePlugin,
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
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    dragScroll: true,
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventAdd: this.interactWithModel.bind(this),
    eventRemove: this.interactWithModel.bind(this),
    allDaySlot: false,
    height: "auto",
    eventBackgroundColor: this.lastUsedColor,
    eventBorderColor: this.lastUsedColor,
    eventTextColor: "var(--system-color-primary-white)",
    slotMinTime: this.formatTime(this.tmpStartDate),
    slotMaxTime: this.formatTime(this.tmpEndDate),
    slotDuration: this.formatTime(this.tmpDuration),
    slotLabelInterval: this.formatTime(this.tmpSlotInterval),
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: false,
    slotEventOverlap: false,
    nowIndicator: false,
    eventDurationEditable: true,
    eventStartEditable: true,

  });

  handleDateSelect(selectInfo: DateSelectArg) {
    this.dataSelectStart = selectInfo.start;
    this.dataSelectEnd = selectInfo.end;
    this.showDialog();

    if(!this.activeDialog){
      this.saveEvent();
    }
  }

  saveEvent(){
    let newEvent:EventInput = {
      title: this.getCourseType(this.lastUsedColor),
      color: this.lastUsedColor,
      borderColor: '#D8D8D8',
      start: this.dataSelectStart,
      end: this.dataSelectEnd,
      editable: true
    };

    this.calendarComponent.getApi().addEvent(newEvent);
    this.calendarComponent.getApi().unselect();
    this.hideDialog();
  }

  interactWithModel() {
    if(this.selectedRoom?.tmpEvents) {
      this.selectedRoom.tmpEvents = this.calendarComponent.getApi().getEvents();
    }
  }

  handleEventClick(clickInfo: EventClickArg) {
    let title = clickInfo.event.title;
    this.confirmationService.confirm({
      message: `Are you sure you want to delete the event '${title}'`,
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        clickInfo.event.remove();
        this.messageService.add({severity:'info', summary:'Confirmed', detail:'You have accepted'});
      },
      reject: () => {
        this.messageService.add({severity:'error', summary:'Rejected', detail:'You have rejected'});
      }
    });
  }

  updateCalendar(calendarOption: any, value: any) {
    this.calendarComponent.getApi().setOption(calendarOption, value);
  }

  saveTimeStartSelection(event: Date) {
    this.dataSelectStart = event;
  }

  saveTimeEndSelection(event: Date) {
    this.dataSelectEnd = event;
  }

  handleOptionChange(event:boolean) {
    this.activeDialog = event;
  }

  changeRoom(event:Room){
    this.selectedRoom = event;
    if(!this.selectedRoom.tmpEvents){
      this.selectedRoom.tmpEvents = [];
    }

    this.calendarComponent.getApi().removeAllEvents();
    this.updateCalendar('events', this.selectedRoom.tmpEvents);
  }

  convertGlobalTableItems(){
    let dto = new TmpTimeTableDTO();

    dto.rooms = this.roomConverter.convertRoomsToDto(this.globalTable.roomTables);
    dto.courses = this.courseConverter.convertListToDto(this.globalTable.courseTable);

    dto.name = this.globalTable.name;
    dto.year = this.globalTable.year;
    dto.semester = this.globalTable.semester;
    dto.status = getStatusKey(this.globalTable.status);

    return dto;
  }

  sendToBackend() {
    this.globalTableService.pushTmpTableObject(this.convertGlobalTableItems())
      .then((message) => {
          this.localStorage.clear('tmptimetable');
          this.messageService.add({severity: 'success', summary: 'Upload Success', detail: message});

          this.router.navigate(['/user/home']).catch(message => {
            this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
          });
      })
      .catch((message) => {
        this.messageService.add({severity: 'error', summary: 'Upload Fault', detail: message});
      });
  }
}
