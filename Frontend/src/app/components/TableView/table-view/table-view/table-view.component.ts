import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ConfirmationService, MessageService, TreeNode} from "primeng/api";
import {GlobalTableService} from "../../../../services/global-table.service";
import {TimeTableNames} from "../../../../../assets/Models/time-table-names";
import {TimeTable} from "../../../../../assets/Models/time-table";
import {NodeConverterService} from "../../../../services/converter/node-converter.service";

@Component({
  selector: 'app-table-view',
  templateUrl: './table-view.component.html',
  styleUrl: '../../tables.css',
})
export class TableViewComponent implements OnInit, OnDestroy{
  itemDialogVisible: boolean = false;
  itemIsEdited = false;
  loadedTable:TimeTable;
  tables!: TreeNode[];
  selectedHeaders: any;
  headers: any[];

  private tablesSub?: Subscription;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private tableService: GlobalTableService,
    private nodeConverter: NodeConverterService,
    private confirmationService: ConfirmationService,
  ) {
    this.loadedTable = new TimeTable();
    this.headers = [
      {field: 'name', header: 'Title'},
      {field: 'semester', header: 'Semester'},
      {field: 'year', header: 'Year'},
    ];
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.tablesSub = this.tableService.getTimeTableByNames()
      .subscribe((data:TimeTableNames[]) => {
        this.tables = this.nodeConverter.convertTableListToNodeList(data)
      });
    this.cd.markForCheck();
  }

  ngOnDestroy() {
    this.tablesSub?.unsubscribe();
    this.cd.detach();
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  editItem(item: TimeTable) {
    this.itemIsEdited = true;
    this.loadedTable = item;
    this.openNew();
  }

  saveNewItem(): void {

  }

  deleteSingleItem(id: number) {
    //TODO call api to delete single table
  }
}
