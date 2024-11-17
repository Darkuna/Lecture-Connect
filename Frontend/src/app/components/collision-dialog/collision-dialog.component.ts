import { Component } from '@angular/core';
import {CollisionService} from "../../services/collision.service";

@Component({
  selector: 'app-collision-dialog',
  templateUrl: './collision-dialog.component.html',
  styleUrl: './collision-dialog.component.css'
})
export class CollisionDialogComponent {
  constructor(
    protected collisionService: CollisionService
  ) {}
}
