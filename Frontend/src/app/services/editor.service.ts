import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";

@Injectable({
  providedIn: 'root'
})
export class EditorService {
  static API_PATH = "/proxy/api/global";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('jwt-token') ?? sessionStorage.getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
  ) { }

  pushSessionChanges(timeTableId : number, changedSessions: CourseSessionDTO[]) {
    let newUrl = `${EditorService.API_PATH}/update-course-sessions/${timeTableId}`;
    return this.http.put<CourseSessionDTO[]>(newUrl, changedSessions, this.httpOptions);
  }
}
