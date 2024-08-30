import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { LocalStorageService } from "ngx-webstorage";
import { MessageService } from "primeng/api";
import { Observable, switchMap, throwError, of } from "rxjs";
import { Course } from "../../assets/Models/course";
import { GlobalTableService } from "./global-table.service";

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  courseApiPath = "/proxy/api/courses";

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
    private currentTableService: GlobalTableService
  ) { }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.courseApiPath, this.httpOptions);
  }

  getUnselectedCourses(): Observable<Course[]> {
    // Verwende ein Observable, um `pipe` verwenden zu können
    return this.currentTableService.currentTimeTable
      ? of(this.currentTableService.currentTimeTable).pipe(
        switchMap((data) => {
          let id = data.id;
          let newUrl = `/proxy/api/global/courses/${id}`;
          return this.http.get<Course[]>(newUrl, this.httpOptions);
        })
      )
      : of([]); // Leeres Observable, falls kein `currentTimeTable` vorhanden ist
  }

  createSingleCourse(course: Course): Observable<Course> {
    return this.http.post<Course>(this.courseApiPath, course, this.httpOptions);
  }

  getSingleCourse(courseID: string): Observable<Course> {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    return this.http.get<Course>(newUrl, this.httpOptions);
  }

  updateSingleCourse(course: Course): Course {
    let newUrl = `${this.courseApiPath}/${course.id}`;
    this.http.put<Course>(newUrl, course, this.httpOptions)
      .subscribe(res => { course = res }
      ).unsubscribe();
    return course;
  }

  deleteSingleCourse(courseID: string): void {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({ severity: 'failure', summary: 'Failure', detail: error.toString() });
      }
    }).unsubscribe();
  }
}
