import {Component, Input, ViewChild} from '@angular/core';
import {FullCalendarComponent} from "@fullcalendar/angular";

@Component({
  selector: 'app-calendar-settings',
  templateUrl: './calendar-settings.component.html',
  styleUrl: './calendar-settings.component.css'
})
export class CalendarSettingsComponent {
  @Input ('calendar') calendarComponent!: FullCalendarComponent;

  tmpStartDate: Date = new Date('2024-07-10T08:00:00');
  tmpEndDate: Date = new Date('2024-07-10T22:00:00');
  tmpDuration: Date = new Date('2024-07-10T00:20:00');
  tmpSlotInterval: Date = new Date('2024-07-10T00:30:00');

  formatTime(date: Date): string {
    // equal returns date as hour:minute:second (00:00:00)
    return date.toString().split(' ')[4];
  }

  updateCalendar(calendarOption: any, value: string) {
    if(value === '00:00:00'){
      value = '00:00:05';
    }
    this.calendarComponent.getApi().setOption(calendarOption, value);
  }

  getStart(){
    return this.formatTime(this.tmpStartDate);
  }

  getEnd(){
    return this.formatTime(this.tmpEndDate);
  }

  getDuration(){
    return this.formatTime(this.tmpDuration);
  }

  getSlotInterval(){
    return this.formatTime(this.tmpSlotInterval);
  }
}
