import { Injectable } from '@angular/core';
import {LoginUserInfoService} from "./login-user-info.service";

@Injectable({
  providedIn: 'root'
})
export class TableLoggerService {
  constructor(
    private userInfoService: LoginUserInfoService
  ) { }


}
