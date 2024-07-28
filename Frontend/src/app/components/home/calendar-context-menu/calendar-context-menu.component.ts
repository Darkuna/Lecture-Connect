import {Component, Input, OnInit} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {EventClickArg} from "@fullcalendar/core";
import {EventImpl} from "@fullcalendar/core/internal";
import {BehaviorSubject} from "rxjs";
import {DropdownFilterOptions} from "primeng/dropdown";

@Component({
  selector: 'app-calendar-context-menu',
  templateUrl: './calendar-context-menu.component.html',
  styleUrl: './calendar-context-menu.component.css'
})
export class CalendarContextMenuComponent implements OnInit{
  @Input('calendar') calendarComponent!: FullCalendarComponent;

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

  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();
  showSearchDialog: boolean = false;
  lastSearchedEvent: EventImpl | null = null;
  firstSearchedEvent: EventImpl | null = null;

  renderEventType(type: string){
    this.loadingOn();

    let newItems = this.calendarComponent.getApi().getEvents()
      .filter(e => e.extendedProps['type'] === type);

    this.tmpRenderSelection = this.tmpRenderSelection.concat(newItems);
    newItems.forEach(e => e.setProp('display', 'none'));

    this.loadingOff();
  }

  loadingOn() {
    this.loadingSubject.next(true);
  }

  loadingOff() {
    this.loadingSubject.next(false);
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

  colorPartnerEvents(event: EventImpl, color: string): EventImpl[]{
    let key = event.title.replace(/ - Group \d+$/, '');
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

  colorSpecificEvent(event: EventImpl){
    this.clearLastEvent();

    this.lastSearchedEvent = this.firstSearchedEvent;
    this.firstSearchedEvent = event;

    if(this.firstSearchedEvent){
      this.firstSearchedEvent.setProp("backgroundColor", 'var(--system-color-primary-red)');
    }
  }

  clearLastEvent(){
    if(this.lastSearchedEvent){
      this.lastSearchedEvent.setProp("backgroundColor", '#666666');
    }
  }

  showHoverDialog(event: EventClickArg){
    if(this.activateLens){
      this.showHoverDialogBool = true;
      this.hoverEventInfo = event;
      this.tmpPartners = this.colorPartnerEvents(event.event, '#ad7353');
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

  activateSearchDialog(){
    this.showSearchDialog = true;
  }

  getCalendarEvents(){
    return this.calendarComponent.getApi().getEvents();
  }

  ngOnInit(): void {
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
