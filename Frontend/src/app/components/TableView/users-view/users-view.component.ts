import {ChangeDetectorRef, Component} from '@angular/core';
import {Userx} from "../../../../assets/Models/userx";
import {ConfirmationService, MessageService} from "primeng/api";
import {Role} from "../../../../assets/Models/enums/role";

@Component({
  selector: 'app-users-view',
  templateUrl: './users-view.component.html',
  styleUrl: '../tables.css'
})
export class UsersViewComponent {
  itemDialogVisible: boolean = false;
  singleUserx: Userx;
  users: Userx[];
  selectedUserxs!: Userx[];
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
    this.users = [];
    this.singleUserx = new Userx();
    this.headers = this.singleUserx.getTableColumns();
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

  editItem(item: Userx) {
    this.itemIsEdited = true;
    this.singleUserx = item;
    this.openNew();
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.singleUserx.updateDate = new Date();
      this.users[this.findIndexById(this.singleUserx.serialVersionUID)] = this.singleUserx;
      this.itemIsEdited = false;
      this.singleUserx = new Userx();

      this.hideDialog();
      this.setToastMessage('success', 'Change', 'Element was updated');
    } else {
      if (this.isInList(this.singleUserx)) {
        this.setToastMessage('error', 'Failure', 'Element already in List');
      } else {
        this.singleUserx.serialVersionUID = Math.floor(Math.random() * 1000000);
        this.singleUserx.createDate = new Date();
        this.singleUserx.updateDate = this.singleUserx.createDate;

        this.users.push(this.singleUserx);
        this.singleUserx = new Userx();

        this.hideDialog();
        this.setToastMessage('success', 'Upload', 'Element saved to DB');
      }
    }
    this.messageService.add({severity: this.mode, summary: this.header, detail: this.text});
  }

  deleteSingleItem(Userx: Userx) {
    if (this.isInList(Userx)) {
      this.users.forEach((item, index) => {
        if (item === Userx) this.users.splice(index, 1);
      });
    }
  }

  deleteMultipleItems() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all the selected Userxs?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.selectedUserxs.forEach(Userx => this.deleteSingleItem(Userx));
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Users deleted permanently',
          life: 2000
        });
      }
    });
  }

  setToastMessage(mode: string, message: string, text: string) {
    this.mode = mode;
    this.header = message;
    this.text = text;
  }

  isInList(item: Userx): boolean {
    for (const listItem of this.users) {
      if (item.constructor !== listItem.constructor) {
        continue;
      }

      if (item.serialVersionUID && listItem.serialVersionUID && item.serialVersionUID === listItem.serialVersionUID) {
        return true;
      }
    }
    return false;
  }

  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.users.length; i++) {
      if (this.users[i].serialVersionUID === id) {
        index = i;
        break;
      }
    }

    return index;
  }

  getRoleOptions() {
    return Object.keys(Role).filter(k => isNaN(Number(k)));
  }
}
