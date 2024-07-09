import {ChangeDetectorRef, Component, Input, signal, ViewChild} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {CalendarOptions, DateSelectArg, EventClickArg} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import listPlugin from "@fullcalendar/list";
import {CourseColor} from "../../../../assets/Models/enums/lecture-color";
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {Observable, Subject, Subscription} from "rxjs";

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
  tmpEvents: Subject<any[]>;

  lastUsedColor: CourseColor = CourseColor.DEFAULT;
  showTimeDialog: boolean = false;

  constructor(
    private cd: ChangeDetectorRef,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.tmpEvents = new Subject<any[]>();
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

  roomIsSelected(): boolean {
    return this.selectedRoom !== null ;
  }

  saveEventsLocally(){
    if(this.roomIsSelected()  && this.selectedRoom) {
      this.selectedRoom!.tmpCalendarTimes = this.selectedRoom.tmpCalendarTimes ?? this.tmpEvents;
    }
  }

  clearCalendar(){
    console.log(this.selectedRoom?.tmpCalendarTimes);

    this.saveEventsLocally();
    this.calendarComponent.getApi().removeAllEvents();
    this.calendarComponent.getApi().refetchEvents();
  }

  refreshCalendar(){
    this.calendarComponent.getApi().refetchEvents();
    this.calendarComponent.getApi().render();
  }


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
    slotMinTime: '07:00:00',
    slotMaxTime: '23:00:00',
    slotDuration: '00:15:00',
    slotLabelInterval: '01:00:00',
    dayHeaderFormat: {weekday: 'long'},
    eventOverlap: true,
    slotEventOverlap: false,
    nowIndicator: false,
  });

  addItem(newEvent: any): void {
    this.tmpEvents.next([newEvent]);
  }

  handleDateSelect(selectInfo: DateSelectArg) {
    const title = prompt('Please enter a new title for your event');

    if (title) {
      this.addItem({
        id: title,
        title: title,
        start: selectInfo.startStr,
        end: selectInfo.endStr,
        color: this.lastUsedColor
      });
    }

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
}
