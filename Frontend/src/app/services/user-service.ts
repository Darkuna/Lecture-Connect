import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Userx } from '../../assets/Models/userx';
import { LocalStorageService } from "ngx-webstorage";
import {MessageService} from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userApiPath = '/proxy/api/users';

  constructor(
    private http: HttpClient,
    private storage: LocalStorageService,
    private messageService: MessageService,
  ) {}

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwt-token')
    })
  };

    getAllUsers(): Observable<Userx[]> {
    return this.http.get<Userx[]>(this.userApiPath, this.httpOptions);
  }

  createSingleUser(user: Userx) {
    return this.http.post<Userx>(this.userApiPath, user, this.httpOptions);
  }

  getSingleUser(userID: string): Observable<any> {
    let newUrl = `${this.userApiPath}/${userID}`;
    return this.http.get(newUrl, this.httpOptions);
  }

  updateSingleUser(user: Userx): Userx {
    let newUrl = `${this.userApiPath}/${user.serialVersionUID}`;
    this.http.put<Userx>(newUrl, user, this.httpOptions)
      .subscribe(res => { user = res }
      ).unsubscribe();
    return user;
  }

  deleteSingleUser(userID: number) {
    let newUrl = `${this.userApiPath}/${userID}`;
    this.http.delete(newUrl, this.httpOptions).subscribe({
      error: error => {
        this.messageService.add({severity: 'failure', summary: 'Failure', detail: error.toString()});
      }
    }).unsubscribe();
  }}
