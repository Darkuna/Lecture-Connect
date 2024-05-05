import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LocalStorageService} from "ngx-webstorage";
import {Observable} from "rxjs";
import {Room} from "../../assets/Models/room";
import {MessageService} from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  roomsApiPath = "/proxy/api/rooms";
  coursesApiPath = "/proxy/api/courses";

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

  getAllRooms() {
    return this.http.get<Room[]>(this.roomsApiPath, this.httpOptions);
  }

  createSingleRoom(room: Room) {
    this.http.post(this.roomsApiPath, room, this.httpOptions);
  }

  getSingleRoom(roomID: String): Observable<any> {
    let newUrl = `${this.roomsApiPath}/${roomID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleRoom(room: Room): Observable<any> {
    let roomID = room.id;
    let newUrl = `${this.roomsApiPath}/${roomID}`;
    return this.http.put(newUrl, room, this.httpOptions);
  }

  deleteSingleRoom(roomId: String) {
    let newUrl = `${this.roomsApiPath}/${roomId}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error});
      }
    })
  }
}
