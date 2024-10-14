import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";
import {RoomService} from "../../../services/room-service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-room-view',
  templateUrl: './room-view.component.html',
  styleUrl: '../tables.css',
})
export class RoomViewComponent implements OnInit, OnDestroy {
  itemDialogVisible: boolean = false;
  itemIsEdited = false;
  singleRoom: Room;
  rooms!: Room[];
  selectedRooms!: Room[];
  selectedHeaders: any;

  headers: any[];
  private roomsSub?: Subscription;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private roomService: RoomService,
  ) {
    this.singleRoom = new Room();

    this.headers = this.singleRoom.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.roomsSub = this.roomService.getAllRooms()
      .subscribe(data => this.rooms = [...data]);
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
    if (this.itemIsEdited) {
      let tmpID = this.singleRoom.id;

      this.rooms[this.findIndexById(tmpID)] =
        this.roomService.updateSingleRoom(this.singleRoom);

      this.itemIsEdited = false;

      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Change', detail: 'Element was updated'});
    } else if (this.isInList(this.singleRoom)) {
      this.messageService.add({severity: 'error', summary: 'Failure', detail: 'Element already in List'});
    } else {
      this.rooms.push(this.roomService.createSingleRoom(this.singleRoom));

      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
    }
    this.singleRoom = new Room();
  }

  deleteSingleItem(room: Room) {
    if (this.isInList(room)) {
      this.rooms.forEach((item, index) => {
        if (item === room) {
          this.roomService.deleteSingleRoom(room.id);
          this.rooms.splice(index, 1);
          return;
        }
      });
    }
  }

  deleteMultipleItems() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all the selected Rooms?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.selectedRooms.forEach(room => this.deleteSingleItem(room));
        this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Rooms deleted permanently'});
      }
    });
  }

  isInList(item: Room): boolean {
    for (const listItem of this.rooms) {
      if (item.constructor !== listItem.constructor) {
        continue;
      }

      if (item.id && listItem.id && item.id === listItem.id) {
        return true;
      }
    }
    return false;
  }

  findIndexById(id: string): number {
    let index = -1;
    for (let i = 0; i < this.rooms.length; i++) {
      if (this.rooms[i].id === id) {
        index = i;
        break;
      }
    }

    return index;
  }
}
