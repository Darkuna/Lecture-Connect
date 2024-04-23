import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Userx} from "../../../assets/Models/userx";
import {Room} from "../../../assets/Models/room";
import {Course} from "../../../assets/Models/course";
import {Router} from "@angular/router";

@Component({
  selector: 'app-table-view',
  templateUrl: './tableView.component.html',
  styleUrl: './tableView.component.css'
})
export class TableViewComponent implements OnInit {
  private type?: Userx | Room | Course;
  items: any[];
  selectedItems?: any;
  headers?: any[];

  constructor(
    private http: HttpClient,
    private router: Router,
  ) { this.items = []; }

  ngOnInit(): void {
    let url: string = this.router.url.split('4200')[1];

    switch (url){
      case ("/api/users"):
        this.type = new Userx();
        break;
      case ("/api/rooms"):
        this.type = new Room();
        break;
      default:
        this.type = new Course();
        break;
    }

    this.headers = this.type.getTableColumns();
  }

  openNew() {

  }

  deleteSelectedProducts() {

  }

  editProduct(item: any) {

  }

  deleteProduct(item: any) {

  }

}
