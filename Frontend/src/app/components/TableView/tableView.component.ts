import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Userx} from "../../../assets/Models/userx";
import {Room} from "../../../assets/Models/room";
import {Course} from "../../../assets/Models/course";
import {Router} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {Role} from "../../../assets/Models/enums/role";
import {TableVariables} from "../../../assets/Models/table-variables";
import {CourseType} from "../../../assets/Models/enums/course-type";

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

  constructor(
    private router: Router,
    private cd: ChangeDetectorRef,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.allVariables = new TableVariables();
    this.items = [];
    let url: string = this.router.url;
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
    let mode = 'error';
    let header = 'Failure';
    let text = 'Element already in List';

    let newItem = this.type.saveItem(this.allVariables);
    if(this.items.indexOf(newItem) < -1){
      this.items.push(newItem);

      mode = 'success';
      header = 'Upload';
      text = 'Element saved to DB';
    }

    this.messageService.add({severity:mode, summary:header, detail:text});
    this.hideDialog();
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

}
