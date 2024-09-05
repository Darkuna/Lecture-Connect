import {Component, Input, Output} from '@angular/core';
import {CourseDTO} from "../../../../../assets/Models/dto/course-dto";
import {TmpTimeTable} from "../../../../../assets/Models/tmp-time-table";

@Component({
  selector: 'app-data-wizard',
  templateUrl: './data-wizard.component.html',
  styleUrl: './data-wizard.component.css'
})
export class DataWizardComponent {
  @Input() tmpTimeTable!: TmpTimeTable;
  @Input() allCourses: CourseDTO[] = [];
  @Output() initialSelection: CourseDTO[] = [];

  inputData(){

  }
}
