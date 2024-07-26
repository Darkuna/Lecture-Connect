import {Component, Input} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {Course} from "../../../../assets/Models/course";
import {C} from "@fullcalendar/core/internal-common";

@Component({
  selector: 'app-detail-selection',
  templateUrl: './detail-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class DetailSelectionComponent {
  @Input() globalTable!: TmpTimeTable;
  showEditDialog: boolean = false;
  headers: any[];
  selectedCourse!: Course;
  tmpSplitTimes: number = 0;
  timesArray: number[] = [];

  constructor() {
    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'semester', header: 'Semester'},
      {field: 'numberOfGroups', header: 'Groups(PS) / Splits(VO)'},
    ];
  }

  setSplitTimesOfCourse(course: Course): void {
    this.selectedCourse = course;
    this.showDialog();
  }

  hasPsType() {
    return this.selectedCourse!.courseType?.toString() === 'PS';
  }

  hasVoType() {
    return this.selectedCourse!.courseType?.toString() === 'VO';
  }

  getHeaderText(): string {
    if (this.selectedCourse) {
      return this.hasPsType() ? 'Groups' : 'Splits'
    } else {
      return 'error';
    }
  }

  saveCourse() {
    if (this.hasPsType()) {
      this.updateTimesArray(this.selectedCourse.duration!);
    }

    this.selectedCourse!.numberOfGroups = this.tmpSplitTimes;
    this.selectedCourse!.splitTimes = this.timesArray;
    this.selectedCourse!.isSplit = true;

    this.hideDialog();
  }

  updateTimesArray(fillValue: number) {
    if(this.hasVoType()){
      this.timesArray = Array(this.tmpSplitTimes + 1).fill(fillValue);
    } else {
      this.timesArray = Array(this.tmpSplitTimes).fill(fillValue);
    }

    console.log(this.timesArray);
  }

  getRemainingDuration() {
    this.timesArray[this.timesArray.length - 1] =
      this.timesArray
        .slice(0, this.timesArray.length - 1)
        .reduce((acc, time) => acc - time, this.selectedCourse.duration!);
  }

  checkTimesArray(){
    return this.timesArray[this.timesArray.length -1] < 0;
  }

  showDialog() {
    this.showEditDialog = true;
  }

  hideDialog() {
    this.showEditDialog = false;
    this.tmpSplitTimes = 0;
    this.timesArray = [];
  }

  splitsSumUpToDuration(): boolean {
    return this.sumOfSplits() === this.selectedCourse.duration;
  }

  sumOfSplits(): any {
    this.timesArray.reduce((accumulation, current) => {
      return accumulation + current;
    }, 0);
  }
}
