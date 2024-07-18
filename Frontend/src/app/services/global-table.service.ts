import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {TimeTable} from "../../assets/Models/time-table";
import {TmpTimeTableDTO} from "../../assets/Models/dto/tmp-time-table-dto";

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
  ) {
  }

  getTimeTableByNames() {
    let newUrl = `${this.timeApiPath}/names`;
    return this.http.get<TimeTableNames[]>(newUrl, this.httpOptions);
  }

  getSpecificTimeTable(id: number) {
    let newUrl = `${this.timeApiPath}/${id}`;
    return this.http.get<TimeTable>(newUrl, this.httpOptions);
  }

  pushTmpTableObject(table: TmpTimeTableDTO) :[boolean, string]{
    let newUrl = `${this.timeApiPath}/create`;
    let status = false;
    let message = "fault happened during upload";
    let returnValue: [boolean, string] = [status, message];

    console.log(table);

    this.http.post<any>(newUrl, table, this.httpOptions).subscribe(
      response => {
        console.log(response);
        status = true;
        message = "upload successfully";
      },
      (error: HttpErrorResponse) => {
        message = error.message;
      }
    );
    return returnValue;
  }

  getScheduledTimeTable(id: number) {
    let newUrl = `${this.timeApiPath}/assignment/${id}`;
    return this.http.post<TimeTable>(newUrl, this.httpOptions);
  }
}
