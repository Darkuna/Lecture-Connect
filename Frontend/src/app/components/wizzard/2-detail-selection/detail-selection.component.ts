import {Component, Input} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {calculateTableHeight, getTableSettings} from "../wizard.component";

@Component({
  selector: 'app-detail-selection',
  templateUrl: './detail-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class DetailSelectionComponent {
  @Input() courses!: Course[];
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
    return this.selectedCourse!.courseType?.toString() === 'PS'
      || this.selectedCourse!.courseType?.toString() === 'SL'
      || this.selectedCourse!.courseType?.toString() === 'SE';
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
    this.selectedCourse!.numberOfGroups = this.tmpSplitTimes;
    this.selectedCourse!.splitTimes = this.timesArray;
    this.selectedCourse!.isSplit = true;

    if (this.hasPsType()) {
      this.selectedCourse.isSplit = false;
      //this.updateTimesArray(this.selectedCourse.duration!);
    }

    if (this.hasVoType()){
      if(this.selectedCourse.numberOfGroups === 0){
        this.selectedCourse.isSplit = false;
        this.selectedCourse.splitTimes = [this.selectedCourse.duration!]
      } else {
        this.selectedCourse.numberOfGroups = 0;
      }

    }

    this.hideDialog();
  }

  updateTimesArray(fillValue: number) {
    if(this.hasVoType()){
      this.timesArray = Array(this.tmpSplitTimes + 1).fill(fillValue);
    } else {
      this.timesArray = Array(this.tmpSplitTimes).fill(fillValue);
    }
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

    protected readonly String = String;
    protected readonly screen = screen;
  protected readonly getTableSettings = getTableSettings;
  protected readonly calculateTableHeight = calculateTableHeight;
}
