import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {RoomService} from "../../../services/room-service";
import {Room} from "../../../../assets/Models/room";

@Component({
  selector: 'app-room-selection',
  templateUrl: './room-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class RoomSelectionComponent implements OnDestroy {
  @Input() roomTables!: Room[];
  @Input() wizardMode: boolean = true;

  @Output() addRoomInParent: EventEmitter<Room> = new EventEmitter<Room>();
  @Output() removeRoomInParent: EventEmitter<Room> = new EventEmitter<Room>();

  courseSub: Subscription;
  availableRooms!: Room[];

  CreateDialogVisible: boolean = false;
  selectedRooms: Room[] = [];
  draggedRoom: Room | undefined | null;
  headers: any[];

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
      {field: 'isComputersAvailable', header: 'Room has PCs'},
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
      this.addRoomInParent.emit(this.draggedRoom);
    }
  }

  dragEnd() {
    this.draggedRoom = null;
  }

  deleteSingleItem(room: Room) {
    this.removeRoomInParent.emit(room);
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

  protected readonly String = String;
  protected readonly screen = screen;
}
