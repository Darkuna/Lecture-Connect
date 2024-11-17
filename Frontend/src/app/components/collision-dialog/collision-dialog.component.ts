import {Component, OnInit} from '@angular/core';
import {CollisionService} from "../../services/collision.service";
import {CollisionType, getCollisionTypes} from "../../../assets/Models/enums/collision-type";

@Component({
  selector: 'app-collision-dialog',
  templateUrl: './collision-dialog.component.html',
  styleUrl: './collision-dialog.component.css'
})
export class CollisionDialogComponent implements OnInit{
  cols: any[] = [];

  constructor(
    protected collisionService: CollisionService
  ) {}

  ngOnInit(): void {
    this.cols = [
      {field: 'key', header: 'Course'},
      {field: 'value', header: 'Type'},
    ];
  }

  getAllCollisions(): Record<string, CollisionType[]>{
    return this.collisionService.getAllCollisions();
  }

  protected readonly getCollisionTypes = getCollisionTypes;
}
