import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private _loading: boolean = false;

  constructor() { }

  waiting(){
    this._loading = true;
  }

  finished(){
    this._loading = false;
  }


  get loading(): boolean {
    return this._loading;
  }

  set loading(value: boolean) {
    this._loading = value;
  }
}
