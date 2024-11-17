import {Component} from '@angular/core';
import {CollisionService} from "../../services/collision.service";
import {CollisionType} from "../../../assets/Models/enums/collision-type";

@Component({
  selector: 'app-collision-dialog',
  templateUrl: './collision-dialog.component.html',
  styleUrl: './collision-dialog.component.css'
})
export class CollisionDialogComponent{
  constructor(
    protected collisionService: CollisionService
  ) {}

  getAllCollisions(): Record<string, CollisionType[]>{
    return this.collisionService.getAllCollisions();
  }

  closeView(){
    this.collisionService.showCollisionDialog = false;
  }
}
