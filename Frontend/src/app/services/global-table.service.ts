import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {TmpTimeTableDTO} from "../../assets/Models/dto/tmp-time-table-dto";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../assets/Models/dto/time-table-dto";
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
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, this.httpOptions);
    return this.currentTimeTable;
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
}
