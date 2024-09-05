import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {Observable} from "rxjs";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";
import {TimeTableDTO} from "../../assets/Models/dto/time-table-dto";

@Injectable({
  providedIn: 'root'
})
export class EditorService {
  static API_PATH = "/proxy/api/global";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwtToken')
    })
  };

  constructor(
    private http: HttpClient,
    private storage: LocalStorageService,
  ) { }

  pushSessionChanges(timeTableId : number, changedSessions: CourseSessionDTO[]) {
    let newUrl = `${EditorService.API_PATH}/update-course-sessions/${timeTableId}`;
    return this.http.put<CourseSessionDTO[]>(newUrl, changedSessions, this.httpOptions);
  }
}
