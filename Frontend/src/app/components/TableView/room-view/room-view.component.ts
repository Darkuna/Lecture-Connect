import {ChangeDetectorRef, Component} from '@angular/core';
import {Room} from "../../../../assets/Models/room";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-room-view',
  templateUrl: './room-view.component.html',
  styleUrl: './room-view.component.css'
})
export class RoomViewComponent {
  itemDialogVisible: boolean = false;
  singleRoom: Room;
  rooms: Room[];
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
    private confirmationService: ConfirmationService
  ) {
    this.rooms = [];
    this.singleRoom = new Room();
    this.headers = this.singleRoom.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
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
    this.singleRoom = item;
    this.openNew();
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.rooms[this.findIndexById(this.singleRoom.id)] = this.singleRoom;
      this.itemIsEdited = false;
      this.singleRoom = new Room();

      this.hideDialog();
      this.setToastMessage('success', 'Change', 'Element was updated');
    } else {
      if (this.isInList(this.singleRoom)) {
        this.setToastMessage('error', 'Failure', 'Element already in List');
      } else {
        this.rooms.push(this.singleRoom);
        this.singleRoom = new Room();

        this.hideDialog();
        this.setToastMessage('success', 'Upload', 'Element saved to DB');
      }
    }
    this.messageService.add({severity: this.mode, summary: this.header, detail: this.text});
  }

  deleteSingleItem(room: Room) {
    if (this.isInList(room)) {
      this.rooms.forEach((item, index) => {
        if (item === room) this.rooms.splice(index, 1);
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
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Rooms deleted permanently',
          life: 3000
        });
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
