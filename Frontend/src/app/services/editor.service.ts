import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";

@Injectable({
  providedIn: 'root'
})
export class EditorService {
  static API_PATH = `${environment.baseUrl}/api/global`;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
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
