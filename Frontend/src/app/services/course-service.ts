import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import {MessageService} from "primeng/api";
import {firstValueFrom, Observable, switchMap} from "rxjs";
import {Course} from "../../assets/Models/course";
import {GlobalTableService} from "./global-table.service";
import { environment } from '../environment/environment';
import {CourseDTO} from "../../assets/Models/dto/course-dto";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  courseApiPath = `${environment.baseUrl}/api/courses`;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage:localStorage).getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private currentTable: GlobalTableService
  ) {
  }

  async getAllCourses() {
    return firstValueFrom(this.http.get<Course[]>(this.courseApiPath, this.httpOptions));
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

  async getUnassignedCourses(tableID: number):Promise<CourseDTO[]>{
    let newUrl = `/proxy/api/global/courses/${tableID}`;
    return firstValueFrom(this.http.get<CourseDTO[]>(newUrl, this.httpOptions));
  }


  async getNewSession(tableID: number, newCourse: CourseDTO): Promise<CourseSessionDTO[]> {
    const newUrl = `/proxy/api/global/add-courses-to-timetable/${tableID}`;
    return firstValueFrom(this.http.post<CourseSessionDTO[]>(newUrl, [newCourse], this.httpOptions));
  }
}
