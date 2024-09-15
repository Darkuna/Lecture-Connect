import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";
import {Observable, switchMap} from "rxjs";
import {Course} from "../../assets/Models/course";
import {GlobalTableService} from "./global-table.service";

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  courseApiPath = "/proxy/api/courses";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
    private storage: LocalStorageService,
    private messageService: MessageService,
    private currentTable: GlobalTableService
  ) {
  }

  getAllCourses() {
    return this.http.get<Course[]>(this.courseApiPath, this.httpOptions);
  }

  getUnselectedCourses(): Observable<Course[]> {
    return this.currentTable.currentTimeTable?.pipe(
      switchMap((data) => {
        let id = data.id;
        let newUrl = `/proxy/api/global/courses/${id}`;
        return this.http.get<Course[]>(newUrl, this.httpOptions);
      })
    ) ?? new Observable();
  }


  createSingleCourse(course: Course) {
    return this.http.post<Course>(this.courseApiPath, course, this.httpOptions);
  }

  getSingleCourse(courseID: string): Observable<any> {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleCourse(course: Course): Course {
    let newUrl = `${this.courseApiPath}/${course.id}`;
    this.http.put<Course>(newUrl, course, this.httpOptions)
      .subscribe(res => { course = res }
      ).unsubscribe();
    return course;
  }

  deleteSingleCourse(courseID: string) {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error.toString()});
      }
    }).unsubscribe();
  }
}
