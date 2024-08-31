import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {Observable} from "rxjs";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";
import {Course} from "../../assets/Models/course";

@Injectable({
  providedIn: 'root'
})
export class EditorServiceService {
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

  pushSessionChanges(changesSessions: CourseSessionDTO[]):Observable<any> {
    let newUrl = `${EditorServiceService.API_PATH}/TO_BE_EDITED`;
    return this.http.post<CourseSessionDTO[]>(newUrl, changesSessions, this.httpOptions);
  }
}
