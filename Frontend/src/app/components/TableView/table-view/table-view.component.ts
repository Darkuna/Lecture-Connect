import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ConfirmationService, MessageService} from "primeng/api";
import {GlobalTableService} from "../../../services/global-table.service";
import {TimeTableNames} from "../../../../assets/Models/time-table-names";
import {TimeTableDTO} from "../../../../assets/Models/dto/time-table-dto";
import {LoaderService} from "../../../services/loader.service";

@Component({
  selector: 'app-table-view',
  templateUrl: './table-view.component.html',
  styleUrl: '../tables.css',
})
export class TableViewComponent implements OnInit, OnDestroy{
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

  loadTable(data: TimeTableNames): void {
    this.loadedTableSub = this.tableService.getSpecificTimeTable(data.id).subscribe(
      (data:TimeTableDTO) => { this.loadedTable = data }
    );
  }


  deleteSingleItem() {
    this.confirmationService.confirm({
      header: 'Are you sure?',
      message: 'Please confirm to proceed.',
      accept: () => {
        //TODO call api to delete single table
        this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'You have accepted', life: 3000 });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000 });
      }
    });
  }

  onDialogClose(){
    if(this.loadedTableSub)
      this.loadedTableSub.unsubscribe();
  }
}
