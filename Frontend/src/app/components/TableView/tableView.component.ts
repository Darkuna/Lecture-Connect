import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Userx} from "../../../assets/Models/userx";
import {Room} from "../../../assets/Models/room";
import {Course} from "../../../assets/Models/course";
import {Router} from "@angular/router";
import {CourseType} from "../../../assets/Models/enums/course-type";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-table-view',
  templateUrl: './tableView.component.html',
  styleUrl: './tableView.component.css',
  providers: [MessageService, ConfirmationService]

})
export class TableViewComponent implements OnInit {
  itemDialog: string = "";
  itemDialogVisible: boolean = false;
  private type?: Userx | Room | Course;
  items: any[];
  item: any;
  selectedItems?: any;
  selectedHeaders: any;
  headers?: any[];

  constructor(
    private http: HttpClient,
    private router: Router,
    private cd: ChangeDetectorRef,
    private confirmationService: ConfirmationService
  ) {
    this.items = [];
  }

  ngOnInit(): void {
    let url: string = this.router.url;
    switch (url) {
      case ("/users"):
        this.type = new Userx();
        break;
      case ("/rooms"):
        this.type = new Room();
        break;
      default:
        console.log(url);
        this.type = new Course();
        break;
    }
    this.itemDialog = url;

    this.headers = this.type.getTableColumns();
    this.selectedHeaders = this.headers;
    this.cd.markForCheck();
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  deleteSelectedItem() {

  }

  editItem(item: any) {

  }

  deleteItem(item: any) {

  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  saveItem() {

  }

  getOptions() {
    return Object.keys(CourseType).filter(k => isNaN(Number(k)));
  }
}
