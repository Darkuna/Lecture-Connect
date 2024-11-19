import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {CollisionType} from "../../assets/Models/enums/collision-type";
import {MessageService} from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class CollisionService {
  currentCollisions: Record<string, CollisionType[]> | null = null;
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
    private http: HttpClient,
    private messageService: MessageService,
  ) { }

  async handleCollisions(tableID: number):Promise<void>{
    if(this.currentCollisions == null) this.currentCollisions = await this.getCurrentCollisions(tableID);
    if(Object.entries(this.currentCollisions).length !== 0) {
      this.showCollisionDialog = true
    } else {
    this.messageService
      .add({severity: 'success', summary: 'Collision Check', detail: 'Your table has currently no collisions'});
    }
  }

  private getCurrentCollisions(tableID: number):Promise<Record<string, CollisionType[]>>{
    const newUrl = `${CollisionService.API_PATH}/collision/${tableID}`;
    return firstValueFrom(this.http.post<Record<string, CollisionType[]>>(newUrl, this.httpOptions));
  }

  getAllCollisions(): Record<string, CollisionType[]>{
    return this.currentCollisions!;
  }

  getCourseCollisions(key: string): Record<string, CollisionType[]> {
    return Object.entries(this.currentCollisions!)
      .filter(([collisionKey, _]) => collisionKey === key) // Filter entries where the key matches
      .reduce((acc, [collisionKey, value]) => {
        acc[collisionKey] = value;
        return acc;
      }, {} as Record<string, CollisionType[]>);
  }


  clearCollisions(){
    this.showCollisionDialog = false;
    this.currentCollisions = null;
  }
}
