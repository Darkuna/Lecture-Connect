import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Userx} from "../../../assets/Models/userx";
import {Room} from "../../../assets/Models/room";
import {Course} from "../../../assets/Models/course";
import {Router} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {Role} from "../../../assets/Models/enums/role";
import {TableVariables} from "../../../assets/Models/table-variables";
import {CourseType} from "../../../assets/Models/enums/course-type";
import {CalendarComponent} from "../calendar/calendar.component";

@Component({
  selector: 'app-table-view',
  templateUrl: './tableView.component.html',
  styleUrl: './tableView.component.css',
  providers: [MessageService, ConfirmationService]

})
export class TableViewComponent implements OnInit {
  itemDialog: string = "";
  itemDialogVisible: boolean = false;
  type: Userx | Room | Course;
  items: any[];
  selectedItems?: any;
  selectedHeaders: any;
  headers?: any[];
  allVariables: TableVariables;

  private mode = 'error';
  private header = 'Failure';
  private text = 'Element already in List';

  constructor(
    private router: Router,
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private calendarComponent: CalendarComponent
  ) {
    this.allVariables = new TableVariables();
    let url: string = this.router.url;
    this.items = [];

    switch (url) {
      case ("/users"):
        this.type = new Userx();

        break;
      case ("/rooms"):
        this.type = new Room();
        break;

      default:
        this.type = new Course();
        break;
    }
  }

  ngOnInit(): void {
    this.itemDialog = this.router.url;
    this.headers = this.type.getTableColumns();
    this.items = this.type.getData();
    this.selectedHeaders = this.headers;
    this.cd.markForCheck();
    console.log("lolollool");
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  editItem(selectedItem: Userx | Room | Course) {
    this.type.editItem(selectedItem);
  }

  saveNewItem() {
    let newItem = this.type.saveItem(this.allVariables);
    console.log(newItem);

    if (this.type.isInList(newItem, this.items)) {
      this.setToastMessage('error', 'Failure', 'Element already in List');
    } else {
      this.setToastMessage('success', 'Upload', 'Element saved to DB');
      this.items.push(newItem);
      this.hideDialog();
      this.cd.markForCheck();
    }
    this.messageService.add({severity: this.mode, summary: this.header, detail: this.text});
    console.log(this.items);
  }

  deleteSingleItem(selectedItem: Userx | Room | Course) {
    this.type.deleteSingleItem(selectedItem);
  }

  deleteMultipleItems() {
    this.type.deleteMultipleItems(this.selectedItems);
  }

  getRoleOptions() {
    return Object.keys(Role).filter(k => isNaN(Number(k)));
  }

  getCourseOptions() {
    return Object.keys(CourseType).filter(k => isNaN(Number(k)));
  }

  setToastMessage(mode: string, message: string, text: string) {
    this.mode = mode;
    this.header = message;
    this.text = text;
  }
}
