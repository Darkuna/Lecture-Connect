import { Injectable } from '@angular/core';
import {TreeNode} from "primeng/api";
import {TimeTableNames} from "../../../assets/Models/time-table-names";

@Injectable({
  providedIn: 'root'
})
export class NodeConverterService {

  constructor() { }

  convertNameTableToNode(table: TimeTableNames): TreeNode<TimeTableNames>{
    return new class implements TreeNode<TimeTableNames> {
      children: TreeNode[] = [];
      data: TimeTableNames = table;
      expanded: boolean = false;
      label: string = table.name ?? 'EMPTY';
      type: string = 'NameTable';
    }
  }

  convertTableListToNodeList(tables: TimeTableNames[]): TreeNode<TimeTableNames>[] {
    return tables.map(this.convertNameTableToNode);
  }
}
