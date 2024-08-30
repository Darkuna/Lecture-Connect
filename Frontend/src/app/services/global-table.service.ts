import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { LocalStorageService } from "ngx-webstorage";
import { TimeTableNames } from "../../assets/Models/time-table-names";
import { TmpTimeTableDTO } from "../../assets/Models/dto/tmp-time-table-dto";
import { catchError, Observable, throwError } from "rxjs";
import { TimeTableDTO } from "../../assets/Models/dto/time-table-dto";
import { TableLogDto } from "../../assets/Models/dto/table-log-dto";
import { Room } from "../../assets/Models/room";
import { Course } from "../../assets/Models/course";
import { CourseSessionDTO } from "../../assets/Models/dto/course-session-dto";

@Injectable({
  providedIn: 'root'
})
export class GlobalTableService {
  currentTimeTable: TimeTableDTO | null = null;
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
  ) {}

  getTimeTableByNames(): Observable<TimeTableNames[]> {
    let newUrl = `${GlobalTableService.API_PATH}/names`;
    return this.http.get<TimeTableNames[]>(newUrl, this.httpOptions);
  }

  getSpecificTimeTable(id: number): Observable<TimeTableDTO> {
    const newUrl = `${GlobalTableService.API_PATH}/${id}`;
    return this.http.get<TimeTableDTO>(newUrl, this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching specific time table:', error);
        return throwError(() => new Error('Failed to fetch specific time table'));
      })
    );
  }

  pushTmpTableObject(table: TmpTimeTableDTO): Promise<[boolean, string]> {
    let newUrl = `${GlobalTableService.API_PATH}/create`;

    return new Promise((resolve, reject) => {
      this.http.post<any>(newUrl, table, this.httpOptions).subscribe({
        next: () => {
          resolve([true, 'upload successfully']);
        },
        error: (err: HttpErrorResponse) => {
          reject([false, err.message]);
        }
      });
    });
  }

  getScheduledTimeTable(id: number): Observable<TimeTableDTO> {
    const newUrl = `${GlobalTableService.API_PATH}/assignment/${id}`;

    return this.http.post<TimeTableDTO>(newUrl, this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Unknown error occurred';
        if (error.status === 400) {
          errorMessage = 'Bad Request: Assignment failed.';
        } else if (error.status === 404) {
          errorMessage = 'Not Found: TimeTable could not be found.';
        } else if (error.status === 500) {
          errorMessage = 'Internal Server Error.';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  removeAll(id: number): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/assignment/remove/${id}`;
    return this.http.post<TimeTableDTO>(newUrl, this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error removing all assignments:', error);
        return throwError(() => error);
      })
    );
  }

  getCollisions(id: number): Observable<CourseSessionDTO[]> {
    let newUrl = `${GlobalTableService.API_PATH}/collision/${id}`;
    return this.http.post<CourseSessionDTO[]>(newUrl, this.httpOptions);
  }

  unselectTable() {
    this.currentTimeTable = null;
  }

  updateTableRooms(id: number, rooms: Room[], newLogs: TableLogDto[]): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/roomupdate/${id}`;
    return this.http.post<TimeTableDTO>(newUrl, [rooms, newLogs], this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error updating table rooms:', error);
        return throwError(() => error);
      })
    );
  }

  updateTableCourses(id: number, courses: Course[], newLogs: TableLogDto[]): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/courseupdate/${id}`;
    return this.http.post<TimeTableDTO>(newUrl, [courses, newLogs], this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error updating table courses:', error);
        return throwError(() => error);
      })
    );
  }

  updateTableLogs(id: number, newLogs: TableLogDto[]): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/logs/${id}`;
    return this.http.post<TimeTableDTO>(newUrl, newLogs, this.httpOptions).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error updating table logs:', error);
        return throwError(() => error);
      })
    );
  }
}
