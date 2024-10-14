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
      children: TreeNode<TimeTableNames>[] = [];
      collapsedIcon: string = 'pi pi-angle-right';
      data: TimeTableNames = table;
      draggable: boolean = false;
      droppable: boolean = false;
      expanded: boolean = false;
      expandedIcon: string = 'pi pi-angle-down';
      icon: string = 'pi pi-angle-double-up';
      key: string = 'here is the key';
      label: string = table.name ?? 'EMPTY';
      type: string = 'NameTable';
    }
  }

  convertTableListToNodeList(tables: TimeTableNames[]): TreeNode<TimeTableNames>[] {
    return tables.map(this.convertNameTableToNode);
  }
}
