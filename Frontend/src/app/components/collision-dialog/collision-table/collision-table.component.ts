import {Component, Input, OnInit} from '@angular/core';
import {CollisionType, getCollisionTypes} from "../../../../assets/Models/enums/collision-type";

@Component({
  selector: 'app-collision-table',
  templateUrl: './collision-table.component.html',
  styleUrl: '../collision-dialog.component.css'
})
export class CollisionTableComponent implements OnInit{
  cols: any[] = [];
  @Input() collisions!: Record<string, CollisionType[]>;
  @Input() showAllColumns: boolean = true;

  constructor(
  ) {}

  ngOnInit(): void {
    this.cols = [
      {field: 'value', header: 'Type'},
    ];
    if (this.showAllColumns)
      this.cols.push({field: 'key', header: 'Course'})
  }

  protected readonly getCollisionTypes = getCollisionTypes;
}
