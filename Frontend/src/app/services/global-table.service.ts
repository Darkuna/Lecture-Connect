import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {Observable} from "rxjs";
import {TimeTable} from "../../assets/Models/time-table";

@Injectable({
  providedIn: 'root'
})
export class GlobalTableService {
  timeApiPath = "/proxy/api/global";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwtToken')
    })
  };

  constructor(
    private http: HttpClient,
    private storage: LocalStorageService,
    private messageService: MessageService,
  ) {
  }

  getTimeTableByNames() {
    let newUrl = `${this.timeApiPath}/names`;
    return this.http.get<TimeTableNames[]>(newUrl, this.httpOptions);
  }

  getSpecificTimeTable(id: number, timeTable: TimeTable): TimeTable {
    let newUrl = `${this.timeApiPath}/${id}`;
    this.http.get<TimeTable>(newUrl, this.httpOptions).subscribe(
      data => timeTable = data
    );
    return timeTable;
  }
}
