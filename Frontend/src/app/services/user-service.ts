import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Userx } from '../../assets/Models/userx';
import { LocalStorageService } from "ngx-webstorage";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/proxy/api/users';

  constructor(
    private http: HttpClient,
    private storage: LocalStorageService
  ) {}

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwtToken')
    })
  };

    getAllUsers(): Observable<Userx[]> {
    return this.http.get<Userx[]>(this.apiUrl, this.httpOptions);
  }
}
