import {Component, ChangeDetectorRef, OnInit, OnDestroy} from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { UserService } from '../../../services/user-service';
import { Userx } from '../../../../assets/Models/userx';
import {getRoleOptions} from "../../../../assets/Models/enums/role";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-users-view',
  templateUrl: './users-view.component.html',
  styleUrls: ['../tables.css'],
})
export class UsersViewComponent implements OnInit, OnDestroy{
  itemDialogVisible: boolean = false;
  singleUserx: Userx;
  users: Userx[];
  selectedUserxs: Userx[] = [];
  selectedHeaders: any[];
  headers: any[];
  private usersSub?: Subscription;
  itemIsEdited = false;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private userService: UserService,
  ) {
    this.users = [];
    this.singleUserx = new Userx();
    this.headers = this.singleUserx.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.usersSub = this.userService.getAllUsers().subscribe(data => this.users = data);
    this.cd.markForCheck();
  }


  ngOnDestroy(): void {
    this.usersSub?.unsubscribe();
    this.cd.detach();
  }

  openNew() {
    this.singleUserx = new Userx();
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
    this. itemIsEdited = false;
  }

  editItem(item: Userx) {
    this.itemIsEdited = true;
    this.singleUserx = item;
    this.itemDialogVisible = true;
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.users[this.findIndexById(this.singleUserx.serialVersionUID)] =
        this.userService.updateSingleUser(this.singleUserx);

      this.itemIsEdited = false;
      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Change', detail: 'Element was updated'});
    } else if (this.isInList(this.singleUserx)) {
      this.messageService.add({severity: 'error', summary: 'Failure', detail: 'Element already in List'});
    } else {
      this.singleUserx.serialVersionUID = 5463728;
      this.userService.createSingleUser(this.singleUserx).subscribe({
        next: value => {
          this.users.push(value);
          this.hideDialog();
          this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
        },

        error: err => {
          this.messageService.add({severity: 'error', summary: 'Upload', detail: err.error});
        }
      });
    }
  }

  deleteSingleItem(user: Userx) {
    if (this.isInList(user)) {
      this.users.forEach((item, index) => {
        if (item === user) {
          this.userService.deleteSingleUser(user.serialVersionUID);
          this.users.splice(index, 1);
        }
      });
    }
  }

  deleteMultipleItems() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all the selected Users?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.selectedUserxs.forEach(user => this.deleteSingleItem(user));
        this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Users deleted permanently'});
      }
    });
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

  protected readonly roleOptions = getRoleOptions();
}
