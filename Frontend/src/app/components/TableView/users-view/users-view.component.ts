import {Component, ChangeDetectorRef, OnInit, OnDestroy} from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { UserService } from '../../../services/user-service';
import {Userx, UserxDTO} from '../../../../assets/Models/userx';
import {getRoleOptions} from "../../../../assets/Models/enums/role";
import {Subscription} from "rxjs";
import {UserxConverterService} from "../../../services/converter/userx-converter.service";

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
    private userConverter: UserxConverterService,
    private confirmationService: ConfirmationService,
    private userService: UserService,
  ) {
    this.users = [];
    this.singleUserx = new Userx();
    this.headers = this.singleUserx.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.usersSub = this.userService.getAllUsers().subscribe(
      (data:UserxDTO[]) => {
        this.users = this.userConverter.convertUserList(data);
      });
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
      const idx = this.users.findIndex(u => u.id === this.singleUserx.id);
      this.users[idx] =
        this.userService.updateSingleUser(this.singleUserx);

      this.itemIsEdited = false;
      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Change', detail: 'Element was updated'});
    } else if (this.users.find(user => user.id === this.singleUserx.id)) {
      this.messageService.add({severity: 'error', summary: 'Failure', detail: 'Element already in List'});
    } else {
      this.singleUserx.id = '5463728';
      this.singleUserx.new = true;
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

  deleteSingleItem(userx: Userx) {
    if (this.users.find(user => user.id === userx.id)) {
      this.users.forEach((item, index) => {
        if (item === userx) {
          this.userService.deleteSingleUser(userx.id);
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

  protected readonly roleOptions = getRoleOptions();
}
