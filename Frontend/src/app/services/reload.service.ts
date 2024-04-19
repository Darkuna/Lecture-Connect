import { Injectable } from '@angular/core';
import {BehaviorSubject, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReloadService {
  constructor() { }

  private messageSource = new BehaviorSubject('default message');
  public currentMessageSubscriber = this.messageSource.asObservable();

  notify(message: any) {
    this.messageSource.next(message)
  }
}
