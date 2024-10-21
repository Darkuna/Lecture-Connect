import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ConfirmationService, MessageService} from "primeng/api";
import {GlobalTableService} from "../../../services/global-table.service";
import {TimeTableNames} from "../../../../assets/Models/time-table-names";
import {TimeTable} from "../../../../assets/Models/time-table";
import {TimeTableDTO} from "../../../../assets/Models/dto/time-table-dto";
import {LoadingInterceptor} from "../../../interceptor/loading.interceptor";
import {LoaderService} from "../../../services/loader.service";

@Component({
  selector: 'app-table-view',
  templateUrl: './table-view.component.html',
  styleUrl: '../tables.css',
})
export class TableViewComponent implements OnInit, OnDestroy{
  itemDialogVisible: boolean = false;
  loadedTable:TimeTableDTO | null = null;
  loadedTableSub: Subscription | null = null;
  tables!: TimeTableNames[];
  selectedTable: TimeTableNames | null = null;
  selectedHeaders: any;
  headers: any[];

  expandedRows = {};
  private tablesSub?: Subscription;

  constructor(
    private cd: ChangeDetectorRef,
    public loadingService: LoaderService,
  private messageService: MessageService,
    private tableService: GlobalTableService,
    private confirmationService: ConfirmationService,
  ) {
    this.headers = [
      {field: 'status', header: 'Status '},
      {field: 'name', header: 'Title'},
      {field: 'semester', header: 'Semester'},
      {field: 'year', header: 'Year'},
      {field: '', header: ''},
    ];

    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.tablesSub = this.tableService.getTimeTableByNames()
      .subscribe((data:TimeTableNames[]) => {
        this.tables = data
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

  loadTable(data: TimeTableNames): void {
    this.loadedTableSub = this.tableService.getSpecificTimeTable(data.id).subscribe(
      (data:TimeTableDTO) => { this.loadedTable = data }
    );
    this.itemDialogVisible = true;

  }


  deleteSingleItem() {
    //TODO call api to delete single table
  }

  onDialogClose(){
    this.itemDialogVisible = false;
    if(this.loadedTableSub)
      this.loadedTableSub.unsubscribe();
  }
}
