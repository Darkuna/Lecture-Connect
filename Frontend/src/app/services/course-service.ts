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
    this.http.post(this.courseApiPath, course, this.httpOptions);
  }

  getSingleCourse(courseID: String): Observable<any> {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleCourse(course: Course): Observable<any> {
    let courseID = course.id;
    let newUrl = `${this.courseApiPath}/${courseID}`;
    return this.http.put(newUrl, course, this.httpOptions);
  }

  deleteSingleCourse(courseID: number) {
    let newUrl = `${this.courseApiPath}/${courseID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error});
      }
    })
  }
}
