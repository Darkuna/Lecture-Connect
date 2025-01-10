import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {GlobalTableChangeDTO} from "../../assets/Models/dto/global-table-change-dto";
import {firstValueFrom} from "rxjs";
import {MessageService} from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class TableLogService {
  showChangeDialog: boolean = false;
  private changes: GlobalTableChangeDTO[] | null = null;

  static API_PATH = `${environment.baseUrl}/api/global/changes`;
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService
  ) { }

  private getTimeTableLogs(id: number) {
    const newUrl = `${TableLogService.API_PATH}/${id}`;
    return firstValueFrom(this.http.get<GlobalTableChangeDTO[]>(newUrl, this.httpOptions));
  }

  async handleChanges(tableID: number):Promise<void>{
    if(this.changes == null) this.changes = await this.getTimeTableLogs(tableID);
    if(this.changes.length !== 0) {
      this.showChangeDialog = true
    } else {
      this.messageService
        .add({severity: 'success', summary: 'Change Check', detail: 'Your table no changes so far!'});
    }
  }

  getAllChanges(): GlobalTableChangeDTO[]{
    return this.changes!;
  }

  clearChanges(){
    this.showChangeDialog = false;
    this.changes = null;
  }
}
