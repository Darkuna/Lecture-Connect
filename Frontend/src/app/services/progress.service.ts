import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProgressService {
  percentCounter: number = 0;
  private _progressCounter: number = 0;
  private _progressMaxCounter: number = 0;
  private _loading: boolean = false;

  constructor() { }

  startLoading(){
    this._loading = true;
  }

  finishedLoading(){
    this._loading = false;
  }

  getLoading(): boolean {
    return this._loading;
  }

  get progressCounter(): number {
    return this._progressCounter;
  }

  set progressCounter(value: number) {
    this._progressCounter += value;
    this.percentCounter = Math.floor((this.progressCounter)/this._progressMaxCounter!*100);
  }



  set progressMaxCounter(value: number) {
    console.log('events length: ', value)
    this.startLoading();
    this._progressMaxCounter = value;
  }
}
