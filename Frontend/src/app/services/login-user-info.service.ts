import {Injectable, OnDestroy} from '@angular/core';
import {LocalStorageService, SessionStorageService} from "ngx-webstorage";
import * as jwt_decode from "jwt-decode";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Subscription} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginUserInfoService implements OnDestroy{
  static API_PATH = "/proxy/auth/login";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.storage.retrieve('jwtToken')
    })
  };

  userLoggedIn: boolean = false;
  private _username: string | null = null;
  private _role: string | null = null;
  private loginSub: Subscription | null = null;

  constructor(
    private sessionStorageService: SessionStorageService,
    private http: HttpClient,
    private storage: LocalStorageService,
  ) {
    this.userLoggedIn = !!sessionStorageService.retrieve("name");
  }

  ngOnDestroy(): void {
    if (!this.loginSub?.closed) {
      this.loginSub?.unsubscribe();
    }
  }

  loginUser(loginObj: any) :Promise<string>{
    return new Promise((resolve, reject) => {
      this.http.post<any>(LoginUserInfoService.API_PATH, loginObj, this.httpOptions).subscribe({
        next: (token) => {
          if (token && token['token']) {
            const decodedToken = jwt_decode.jwtDecode(token['token']) as { [key: string]: string };

            this.username = decodedToken['username'];
            this.role = decodedToken['role'][0];
            this.userLoggedIn = true;
            this.storage.store('jwtToken', token['token']);
          }

          resolve('upload successfully');
        },
        error: (err: HttpErrorResponse) => {
          reject(err.message);
        }
      });
    });
  }

  get username(): string {
    return this._username || "";
  }

  set username(value: string) {
    this.sessionStorageService.store("name", value);
    this._username = value;
  }

  get role(): string {
    this.sessionStorageService.retrieve("role");
    return this._role || "";
  }

  set role(value: string) {
    this.sessionStorageService.store("role", value);
    this._role = value;
  }
}
