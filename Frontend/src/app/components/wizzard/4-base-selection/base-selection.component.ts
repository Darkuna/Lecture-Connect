import {Component, Input, OnInit, signal, ViewChild} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {CalendarOptions, DateSelectArg,  EventClickArg} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import rrulePlugin from '@fullcalendar/rrule'
import {CourseColor} from "../../../../assets/Models/enums/lecture-color";
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {Subject} from "rxjs";

@Component({
  selector: 'app-base-selection',
  templateUrl: './base-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class BaseSelectionComponent {
  @Input() globalTable!: TmpTimeTable;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;

  protected readonly CourseColor = CourseColor;
  selectedRoom: Room | null = null;
  tmpEvents: Subject<any[]> = new Subject<any[]>();

  lastUsedColor: CourseColor = CourseColor.DEFAULT;
  showTimeDialog: boolean = false;

  tmpStartDate: Date = new Date('2024-07-10T07:00:00');
  tmpEndDate: Date = new Date('2024-07-10T23:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:15:00');
  tmpSlotInterval: Date = new Date('2024-07-10T01:00:00');
  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }

  formatTime(date: Date): string {
    /* equal to the return value
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    */
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

  calculateTimeDistance(selectInfo: DateSelectArg){
    let s: Date = new Date(selectInfo.startStr.toString());
    let e: Date = new Date(selectInfo.endStr.toString());

    const duration = e.getTime() - s.getTime();
    const differenceInMinutes = Math.floor(duration / (1000 * 60));
    const hours = Math.floor(differenceInMinutes / 60);
    const minutes = differenceInMinutes % 60;

    console.log(`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`);
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
  }

  roomIsSelected(): boolean {
    return this.selectedRoom !== null ;
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
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
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
    eventOverlap: true,
    slotEventOverlap: false,
    nowIndicator: false,
  });

  handleDateSelect(selectInfo: DateSelectArg) {
    this.calendarComponent.getApi().addEvent({
      color: this.lastUsedColor,
      borderColor: '#D8D8D8',
      rrule: {
        freq: 'weekly',
        byweekday: this.getDayFromTime(selectInfo),
        dtstart: selectInfo.startStr.toString(),
      },
      duration: this.calculateTimeDistance(selectInfo)

    });

    this.calendarComponent.getApi().unselect();
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

  updateCalendar(calendarOption: string, value: any) {
    this.calendarOptions.update(val => ({
      ...val,
      [calendarOption]: value
    }));
  }

  private getDayFromTime(selectInfo: DateSelectArg) {
    let date: Date = new Date(selectInfo.startStr.toString());
    return `${date.getDay()}`;
  }
}

