import {ChangeDetectorRef, Component} from '@angular/core';
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";
import {RoomService} from "../../../services/room-service";

@Component({
  selector: 'app-room-view',
  templateUrl: './room-view.component.html',
  styleUrl: '../tables.css'
})
export class RoomViewComponent {
  itemDialogVisible: boolean = false;
  singleRoom: Room;
  rooms: Room[];
  selectedRoom!: Room;
  selectedRooms!: Room[];
  selectedHeaders: any;
  headers: any[];

  stateOptions: any[] = [
    {label: 'Yes', value: true},
    {label: 'No', value: false}
  ];

  private mode = 'error';
  private header = 'Failure';
  private text = 'Element already in List';
  private itemIsEdited = false;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private roomService: RoomService,
  ) {
    this.rooms = [];
    this.singleRoom = new Room();

    this.headers = this.singleRoom.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.roomService.getAllRooms().subscribe(rooms => this.rooms = rooms);
    this.cd.markForCheck();
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  editItem(item: Room) {
    this.itemIsEdited = true;
    this.selectedRoom = item;
    this.openNew();
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.selectedRoom.updateDate = new Date();
      console.log(this.roomService.updateSingleRoom(this.selectedRoom));
      this.rooms[this.findIndexById(this.selectedRoom.id)] = this.selectedRoom;

      this.itemIsEdited = false;
      this.selectedRoom = new Room();

      this.hideDialog();
      this.setToastMessage('success', 'Change', 'Element was updated');
    } else if (this.isInList(this.singleRoom)) {
      this.setToastMessage('error', 'Failure', 'Element already in List');
    } else {
      this.singleRoom.createDate = new Date();
      this.singleRoom.updateDate = this.singleRoom.createDate;
      this.rooms.push(this.singleRoom);
      this.roomService.createSingleRoom(this.singleRoom);
      this.singleRoom = new Room();

      this.hideDialog();
      this.setToastMessage('success', 'Upload', 'Element saved to DB');
    }
    this.messageService.add({severity: this.mode, summary: this.header, detail: this.text});
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

  setToastMessage(mode: string, message: string, text: string) {
    this.mode = mode;
    this.header = message;
    this.text = text;
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
