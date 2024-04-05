import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class AppService {

  authenticated: boolean = false;

  constructor(private http: HttpClient) {
  }

  authenticate(credentials:any, callback:any) {
    const headers = new HttpHeaders(credentials ? {
      authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    } : {});

    interface UserResponse {
      name?: string;
    }

    this.http.get<UserResponse>('user', { headers: headers }).subscribe(response => {
      if (response.name) {
        this.authenticated = true;
      } else {
        this.authenticated = false;
      }
      return callback && callback();
    });
  }
}

