import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {CollisionResponse} from "../../assets/Models/dto/collision";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {firstValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CollisionService {
  currentCollisions: CollisionResponse | null = null;
  showCollisionDialog: boolean = false;

  static API_PATH = `${environment.baseUrl}/api/global`;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage : localStorage).getItem('jwt-token')
    })
  }

  constructor(
    private http: HttpClient
  ) { }

  async handleCollisions(tableID: number):Promise<void>{
    if(this.currentCollisions == null) this.currentCollisions = await this.getCurrentCollisions(tableID);
    this.showCollisionDialog = true;
    return;
  }

  private getCurrentCollisions(tableID: number):Promise<CollisionResponse>{
    const newUrl = `${CollisionService.API_PATH}/collision/${tableID}`;
    return firstValueFrom(this.http.post<CollisionResponse>(newUrl, this.httpOptions));
  }

  getAllCollisions(): CollisionResponse{
    return this.currentCollisions!;
  }

  getCourseCollisions(courseID: string){
    return this.currentCollisions
  }

  clearCollisions(){
    this.currentCollisions = null;
  }
}
