import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../../assets/Models/room";
import {MessageService} from "primeng/api";
import {environment} from "../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  roomsApiPath = `${environment.baseUrl}/api/rooms`;
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
  ) {
  }

  getAllRooms() {
    return this.http.get<Room[]>(this.roomsApiPath, this.httpOptions);
  }

  createSingleRoom(room: Room) {
    this.http.post<Room>(this.roomsApiPath, room, this.httpOptions)
      .subscribe(newRoom => {
        room = newRoom;
      }).unsubscribe();
    return room;
  }

  getSingleRoom(roomID: string): Observable<any> {
    let newUrl = `${this.roomsApiPath}/${roomID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleRoom(room: Room): Room {
    let newUrl = `${this.roomsApiPath}/${room.id}`;
    this.http.put<Room>(newUrl, room, this.httpOptions)
      .subscribe(data => {
        room = data
      }).unsubscribe();
    return room;
  }

  deleteSingleRoom(roomId: string) {
    let newUrl = `${this.roomsApiPath}/${roomId}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error});
      }
    }).unsubscribe();
  }
}
