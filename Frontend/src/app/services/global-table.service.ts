import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {TimeTableNames} from "../../assets/Models/time-table-names";
import {TmpTimeTableDTO} from "../../assets/Models/dto/tmp-time-table-dto";
import {Observable} from "rxjs";
import {TimeTableDTO} from "../../assets/Models/dto/time-table-dto";
import {TableLogDto} from "../../assets/Models/dto/table-log-dto";
import {Room} from "../../assets/Models/room";
import {Course} from "../../assets/Models/course";

@Injectable({
  providedIn: 'root'
})
export class GlobalTableService {
  currentTimeTable: Observable<TimeTableDTO> | null = null;
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
  ) {
  }

  getTimeTableByNames() {
    let newUrl = `${GlobalTableService.API_PATH}/names`;
    return this.http.get<TimeTableNames[]>(newUrl, this.httpOptions);
  }

  getSpecificTimeTable(id: number):Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/${id}`;
    this.currentTimeTable = this.http.get<TimeTableDTO>(newUrl, this.httpOptions)
    return this.currentTimeTable;
  }

  pushTmpTableObject(table: TmpTimeTableDTO): Promise<[boolean, string]> {
    let newUrl = `${GlobalTableService.API_PATH}/create`;

    return new Promise((resolve, reject) => {
      this.http.post<any>(newUrl, table, this.httpOptions).subscribe(
        response => {
          resolve([true, 'upload successfully']);
        },
        (error: HttpErrorResponse) => {
          reject([false, error.message]);
        }
      );
    });
  }

  getScheduledTimeTable(id: number): Observable<TimeTableDTO> {
    let newUrl = `${GlobalTableService.API_PATH}/assignment/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, this.httpOptions);
    return this.currentTimeTable;
  }

  unselectTable(){
    this.currentTimeTable = null;
  }

  //TODO change with correct api path
  updateTableRooms(id: number, rooms : Room[]){
    let newUrl = `${GlobalTableService.API_PATH}/roomupdate/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, rooms ,this.httpOptions);
    return this.currentTimeTable;
  }

  //TODO change with correct api path
  updateTableCourses(id: number, courses : Course[]){
    let newUrl = `${GlobalTableService.API_PATH}/courseupdate/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, courses, this.httpOptions);
    return this.currentTimeTable;
  }

  //TODO change with correct api path
  updateTableLogs(id: number, newLogs : TableLogDto[]){
    let newUrl = `${GlobalTableService.API_PATH}/logs/${id}`;
    this.currentTimeTable =  this.http.post<TimeTableDTO>(newUrl, newLogs, this.httpOptions);
    return this.currentTimeTable;
  }
}
