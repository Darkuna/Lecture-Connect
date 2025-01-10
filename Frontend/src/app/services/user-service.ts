import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Userx, UserxDTO} from '../../assets/Models/userx';
import {MessageService} from "primeng/api";
import {UserxConverterService} from "./converter/userx-converter.service";
import {environment} from "../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userApiPath = `${environment.baseUrl}//api/users`;

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private userConverter: UserxConverterService
  ) {}

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

    getAllUsers(): Observable<UserxDTO[]> {
    return this.http.get<UserxDTO[]>(this.userApiPath, this.httpOptions);
  }

  createSingleUser(user: Userx) {
      const dto = this.userConverter.toDTO(user);
      return this.http.post<Userx>(this.userApiPath, dto, this.httpOptions);
  }

  updateSingleUser(user: Userx): Userx {
    let newUrl = `${this.userApiPath}/${user.id}`;
    this.http.put<Userx>(newUrl, user, this.httpOptions)
      .subscribe(res => { user = res }
      )
    return user;
  }

  deleteSingleUser(userID: string) {
    let newUrl = `${this.userApiPath}/${userID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error.toString()});
      }
    }).unsubscribe();
  }}
