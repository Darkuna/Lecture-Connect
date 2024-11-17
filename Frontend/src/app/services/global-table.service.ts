import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {TmpTimeTableDTO} from "../../assets/Models/dto/tmp-time-table-dto";
import {catchError, Observable, throwError} from "rxjs";
import {TimeTableDTO} from "../../assets/Models/dto/time-table-dto";
import {TableLogDto} from "../../assets/Models/dto/table-log-dto";
import {Room} from "../../assets/Models/room";
import {Course} from "../../assets/Models/course";
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";
import {environment} from "../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class GlobalTableService {
  currentTimeTable: Observable<TimeTableDTO> | null = null;
  tableId: number | null = null;

  static API_PATH = `${environment.baseUrl}/api/global`;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage:localStorage).getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
  ) {
  }

  getTimeTableByNames() {
    const newUrl = `${GlobalTableService.API_PATH}/names`;
    return this.http.get<TimeTableNames[]>(newUrl, this.httpOptions);
  }

  getSpecificTimeTable(id: number):Observable<TimeTableDTO> {
    const newUrl = `${GlobalTableService.API_PATH}/${id}`;
    this.currentTimeTable = this.http.get<TimeTableDTO>(newUrl, this.httpOptions)
    //this.currentTimeTable.subscribe(r => this.tableId = r.id);
    return this.currentTimeTable;
  }

  pushTmpTableObject(table: TmpTimeTableDTO): Promise<string> {
    const newUrl = `${GlobalTableService.API_PATH}/create`;

    return new Promise((resolve, reject) => {
      this.http.post<any>(newUrl, table, this.httpOptions).subscribe({
        next: () => {
        resolve('upload successfully');
      },
      error: (err: HttpErrorResponse) => {
        reject(err.message);
      }
      });
    });
  }

  getScheduledTimeTable(id: number): Observable<TimeTableDTO> {
    const newUrl = `${GlobalTableService.API_PATH}/assignment/${id}`;
    return this.http.post<TimeTableDTO>(newUrl, this.httpOptions).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  removeAll(id: number): Observable<TimeTableDTO> {
    const newUrl = `${GlobalTableService.API_PATH}/assignment/remove/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, this.httpOptions);
    return this.currentTimeTable;
  }

  removeCollisions(id: number): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/assignment/removeCollisions/${id}`;
    this.currentTimeTable = this.http.post<TimeTableDTO>(newUrl, this.httpOptions);
    return this.currentTimeTable;
  }

  getCollisions(id: number): Observable<CourseSessionDTO[]>{
    const newUrl = `${GlobalTableService.API_PATH}/collision/${id}`;
    return this.http.post<CourseSessionDTO[]>(newUrl, this.httpOptions);
  }

  unselectTable(){
    this.currentTimeTable = null;
  }

  deleteTable(id: number){
    const newUrl = `${GlobalTableService.API_PATH}/${id}`;
    return this.http.delete(newUrl, this.httpOptions);
  }

  //TODO change with correct api path
  updateTableRooms(id: number, rooms : Room[], newLogs : TableLogDto[]){
    const newUrl = `${GlobalTableService.API_PATH}/roomupdate/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, [rooms, newLogs] ,this.httpOptions);
    return this.currentTimeTable;
  }

  //TODO change with correct api path
  updateTableCourses(id: number, courses : Course[], newLogs : TableLogDto[]){
    const newUrl = `${GlobalTableService.API_PATH}/courseupdate/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, [courses, newLogs], this.httpOptions);
    return this.currentTimeTable;
  }
}
