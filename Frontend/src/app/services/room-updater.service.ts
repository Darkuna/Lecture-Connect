import { Injectable } from '@angular/core';
import {CourseSessionDTO} from "../../assets/Models/dto/course-session-dto";
import {CourseDTO} from "../../assets/Models/dto/course-dto";
import {HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RoomUpdaterService {
    currentRooms: CourseSessionDTO[] = [];
    private _newlyAddedRooms: CourseDTO[] = [];
    private _updatedRooms: CourseDTO[] = [];

    httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' +
          (localStorage.getItem('jwt-token') === null ? sessionStorage:localStorage).getItem('jwt-token')
      })
    };

  constructor() { }
}
