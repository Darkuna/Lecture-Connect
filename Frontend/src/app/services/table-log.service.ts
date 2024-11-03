import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {GlobalTableChangeDTO} from "../../assets/Models/dto/global-table-change-dto";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TableLogService {
  static API_PATH = `${environment.baseUrl}/api/global/changes`;
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage:localStorage).getItem('jwt-token')
    })
  };

  constructor(
    private http: HttpClient,
  ) { }

  getTimeTableLogs() {
    return this.http.get<GlobalTableChangeDTO[]>(TableLogService.API_PATH, this.httpOptions);
  }
}
