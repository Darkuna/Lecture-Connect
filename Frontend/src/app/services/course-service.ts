import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {Course} from "../../assets/Models/course";

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
  ) {
  }

  getAllCourses() {
    return this.http.get<Course[]>(this.courseApiPath, this.httpOptions);
  }

  createSingleCourse(course: Course) {
    this.http.post<Course>(this.courseApiPath, course, this.httpOptions)
      .subscribe(data => {
        course = data
      }).unsubscribe();
    return course;
  }

  getSingleCourse(courseID: String): Observable<any> {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleCourse(course: Course): Course {
    let newUrl = `${this.courseApiPath}/${course.id}`;
    this.http.put<Course>(newUrl, course, this.httpOptions).subscribe(res => course = res)
      .unsubscribe();
    return course;
  }

  deleteSingleCourse(courseID: number) {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error});
      }
    }).unsubscribe();
  }
}
