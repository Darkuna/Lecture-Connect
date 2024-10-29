import {AfterViewInit, Component, Injectable,  OnInit} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {EventClickArg} from "@fullcalendar/core";
import {CourseSessionDTO} from "../../../../assets/Models/dto/course-session-dto";
import {EventImpl} from "@fullcalendar/core/internal";
import {StudyType} from "../../../../assets/Models/enums/study-type";

@Injectable()
@Component({
  selector: 'app-calendar-context-menu',
  templateUrl: './calendar-context-menu.component.html',
  styleUrl: './calendar-context-menu.component.css'
})
export class CalendarContextMenuComponent implements OnInit, AfterViewInit{
  private _calendarComponent!: FullCalendarComponent;
  protected readonly screen = screen;

  contextItems: MenuItem[] = [];
  showHoverDialogBool: boolean = false;

  activateLens: boolean = true;
  hoverEventInfo: EventClickArg |null = null;

  tmpPartners : EventImpl[] = [];
  tmpRenderSelection : EventImpl[] = [];
  tmpColorSelection : EventImpl[] = [];

  constructor(
    private messageService: MessageService,
  ) { }

  ngAfterViewInit(): void {
    this.activateLens = true
  }

  renderEventType(type: string){
    let newItems = this._calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['type'] === type);

    this.tmpRenderSelection = this.tmpRenderSelection.concat(newItems);
    newItems.forEach(e => e.setProp('display', 'none'));
  }

  renderSemesterType(semester: number){
    let newItems = this._calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['semester'] !== semester);

    this.tmpRenderSelection = this.tmpRenderSelection.concat(newItems);
    newItems.forEach(e => e.setProp('display', 'none'));
  }

  renderStudyType(type: string){
    let newItems = this._calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['studyType'] !== type);

    this.tmpRenderSelection = this.tmpRenderSelection.concat(newItems);
    newItems.forEach(e => e.setProp('display', 'none'));
  }

  calculateDialogPosition(coordinate:number, screenMax:number){
    return Math.min(coordinate, screenMax*0.65);
  }

  showAllEvents(){
    this.tmpRenderSelection.forEach(e => {
      e.setProp('display','auto');
    });

    this.tmpRenderSelection = [];
  }

  colorEventType(type: string, color: string){
    let newItems = this._calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['type'] === type);

    this.tmpColorSelection = this.tmpColorSelection.concat(newItems);
    newItems.forEach(e => e.setProp('backgroundColor', color));
  }

  clearEvents(){
    this.tmpColorSelection
      .forEach(e => {
        e.setProp('backgroundColor', '#666666');
      });

    this.tmpColorSelection = [];
  }

  colorPartnerEvents(event: EventImpl, color: string): EventImpl[]{
    let key = event.title!.replace(/ - (?:Group|Split) \d+$/, '');
    let partner = this._calendarComponent
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

  changeLensStatus(){
    this.activateLens = !this.activateLens;

    this.messageService.add({
      severity: this.activateLens ? 'success' : 'error',
      summary: 'Hover Mode',
      detail: `Lens is ${this.activateLens ? 'activated' : 'deactivated' }`})
  }

  colorCollisionEvents(collision: CourseSessionDTO[]) {
    const calendarApi = this.calendarComponent.getApi();
    const originalColors: { [eventId: string]: string } = {};

    collision.forEach(collisionEvent => {
      calendarApi.getEvents().forEach(event => {
        if (event.id === collisionEvent.id.toString()) {
          this.tmpColorSelection.push(event) //so i can clear the events again
          originalColors[event.id] = event.backgroundColor;
          event.setProp("backgroundColor", "#34675C");
        }
      });
    });
  }

  getItemMenuOptions() : void {
    if(this.activateLens){
      this.contextItems[4].label = 'Lens (active)'
    } else {
      this.contextItems[4].label = 'Lens (inactive)'
    }
  }

  ngOnInit(): void {
    this.activateLens = true;

    this.contextItems = [
      {
        label: 'Remove Groups',
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
        label: 'Semester Filter',
        icon: 'pi pi-filter-fill',
        items: [
          ...Array.from({ length: 6 }, (_, i) => ({
            label: (i + 1).toString(),
            command: () => this.renderSemesterType(i + 1)
          }))
        ]
      },
      {
        label: 'Study Type Filter',
        icon: 'pi pi-filter-fill',
        items: [
          ...Object.values(StudyType)
            .map(value => ({
              label: value.toString(),
              command: () => this.renderStudyType(value.toString())
            }))
        ]
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

  get calendarComponent(): FullCalendarComponent {
    return this._calendarComponent;
  }

  set calendarComponent(value: FullCalendarComponent) {
    this._calendarComponent = value;
  }

}
