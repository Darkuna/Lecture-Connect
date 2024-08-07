import {Component, Input, OnDestroy} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {Status} from "../../../../assets/Models/enums/status";
import {RoomService} from "../../../services/room-service";
import {Room} from "../../../../assets/Models/room";
import {Course} from "../../../../assets/Models/course";

@Component({
  selector: 'app-room-selection',
  templateUrl: './room-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class RoomSelectionComponent implements OnDestroy {
  @Input() globalTable!: TmpTimeTable;

  courseSub: Subscription;
  availableRooms!: Room[];

  CreateDialogVisible: boolean = false;
  selectedRooms: Room[] = [];
  draggedRoom: Room | undefined | null;

  headers: any[];
  stateOptions: any[] = [
    {label: 'Yes', value: true},
    {label: 'No', value: false}
  ];

  constructor(
    private roomService: RoomService,
    private messageService: MessageService,
  ) {
    this.courseSub = this.roomService.getAllRooms().subscribe(
      (data => this.availableRooms = data)
    );

    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'capacity', header: 'Capacity'},
      {field: 'computersAvailable', header: 'Room has PCs'},
    ];
  }

  ngOnDestroy(): void {
    this.courseSub.unsubscribe();
  }

  showCreateDialog(): void {
    this.draggedRoom = new Room();
    this.CreateDialogVisible = true;
  }

  hideDialog() {
    this.CreateDialogVisible = false;
  }

  saveNewItem(): void {
    if (this.draggedRoom) {
      this.draggedRoom.timingConstraints = [];
      this.draggedRoom.createdAt = undefined;
      this.draggedRoom.updatedAt = undefined;

      this.availableRooms.push(this.roomService.createSingleRoom(this.draggedRoom!));

      this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
      this.draggedRoom = null;
      this.hideDialog();
    }
  }

  dragStart(item: Room) {
    this.draggedRoom = item;
  }

  drag() {
  }

  drop() {
    if (this.draggedRoom) {
      let idx = this.findIndex(this.draggedRoom, this.globalTable.roomTables);

      if (idx !== -1) {
        this.messageService.add({severity: 'error', summary: 'Duplicate', detail: 'Room is already in List'});
      } else {
        this.globalTable.roomTables.push(this.draggedRoom);
        this.draggedRoom = null;
      }
    }
  }

  findIndex(product: Room, list: Room[]): number {
    let index = -1;
    for (let i = 0; i < (list).length; i++) {
      if (product.id === (list)[i].id) {
        index = i;
        break;
      }
    }
    return index;
  }

  dragEnd() {
    this.draggedRoom = null;
    this.globalTable.status = Status.EDITED;
  }

  deleteSingleItem(room: Room) {

    const index = this.selectedRooms.indexOf(room, 0);
    if (index > -1) {
      this.selectedRooms.splice(index, 1);
    }

    this.globalTable.roomTables = this.globalTable.roomTables.filter(val => val.id !== room.id);
  }

  roomsSelected() : boolean{
    return this.selectedRooms.length !== 0;
  }

  deleteMultipleItems() {
    if(this.roomsSelected()){
      this.selectedRooms.forEach(
        c => this.deleteSingleItem(c)
      );
    }
  }
}
