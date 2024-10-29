import { Injectable } from '@angular/core';
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";
import {CourseDTO} from "../../assets/Models/dto/course-dto";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {GlobalTableService} from "./global-table.service";

@Injectable({
  providedIn: 'root'
})
export class CourseUpdaterService {
  currentCourses: CourseSessionDTO[] = [];
  private _newlyAddedCourses: CourseDTO[] = [];
  private _updatedCourseSessions: CourseDTO[] = [];

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage:localStorage).getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient
  ) { }

  addNewCourses(tableID: number) {
    const newUrl = `${GlobalTableService.API_PATH}/add-rooms-to-timetable${tableID}`;
    return this.http.post<TimeTableNames[]>(newUrl, this.newlyAddedCourses, this.httpOptions);
  }

  updateCourseSession(tableID: number) {
    const newUrl = `${GlobalTableService.API_PATH}/update-course-sessions${tableID}`;
    return this.http.post<TimeTableNames[]>(newUrl, this.updatedCourseSessions,this.httpOptions);
  }

  get newlyAddedCourses(): CourseDTO[] {
    return this._newlyAddedCourses;
  }

  set newlyAddedCourses(value: CourseDTO[]) {
    this._newlyAddedCourses = value;
  }

  get updatedCourseSessions(): CourseDTO[] {
    return this._updatedCourseSessions;
  }

  set updatedCourseSessions(value: CourseDTO[]) {
    this._updatedCourseSessions = value;
  }
}
