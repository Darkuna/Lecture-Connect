import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {RoomService} from "../../../services/room-service";
import {Room} from "../../../../assets/Models/room";
import {getTableSettings} from "../wizard.component";

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
  draggedRoom: Room | undefined | null;

  constructor(
    private roomService: RoomService,
    private messageService: MessageService,
  ) {
    this.courseSub = this.roomService.getAllRooms().subscribe(
      (data => this.availableRooms = data)
    );
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

  protected readonly String = String;
  protected readonly getTableSettings = getTableSettings;
}
