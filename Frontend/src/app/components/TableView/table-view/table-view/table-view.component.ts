import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Room} from "../../../../../assets/Models/room";
import {Subscription} from "rxjs";
import {ConfirmationService, MessageService} from "primeng/api";
import {GlobalTableService} from "../../../../services/global-table.service";
import {TimeTableNames} from "../../../../../assets/Models/time-table-names";

@Component({
  selector: 'app-table-view',
  templateUrl: './table-view.component.html',
})
export class TableViewComponent implements OnInit, OnDestroy{
  itemDialogVisible: boolean = false;
  itemIsEdited = false;
  singleRoom: Room;
  tables!: TimeTableNames[];
  selectedRooms!: Room[];
  selectedHeaders: any;
  headers: any[];

  private roomsSub?: Subscription;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private tableService: GlobalTableService,
  ) {
    this.singleRoom = new Room();
    this.headers = [
      {field: 'name', header: 'Title'},
      {field: 'semester', header: 'Semester'},
      {field: 'year', header: 'Year'},
    ];
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.roomsSub = this.tableService.getTimeTableByNames()
      .subscribe(data => this.tables = [...data]);
    this.cd.markForCheck();
  }

  ngOnDestroy() {
    this.roomsSub?.unsubscribe();
    this.cd.detach();
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  editItem(item: Room) {
    this.itemIsEdited = true;
    this.singleRoom = item;
    this.openNew();
  }

  saveNewItem(): void {

  }

  deleteSingleItem(room: Room) {

  }
}
