import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {TimeTable} from "../../assets/Models/time-table";
import {RoomTable} from "../../assets/Models/room-table";

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

  getSpecificTimeTable(id: number):TimeTable{
    let timeTable = new TimeTable();
    let newUrl = `${this.timeApiPath}/${id}`;
    this.http.get<TimeTable>(newUrl, this.httpOptions).subscribe(
      data => {
        timeTable.id = data["id"];
        timeTable.semester = data["semester"];
        timeTable.year = data["year"];
        timeTable.status = data["status"];
        timeTable.roomTables = data["roomTables"];
        timeTable.courseSessions = data["courseSessions"];
        timeTable.createdAt = data["createdAt"];
        timeTable.updatedAt = data["updatedAt"];
      }
    );
    return timeTable;
  }
}
