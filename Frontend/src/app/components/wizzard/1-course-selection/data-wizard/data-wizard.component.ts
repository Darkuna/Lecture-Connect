import {Component, Input, OnInit, Output} from '@angular/core';
import {CourseDTO} from "../../../../../assets/Models/dto/course-dto";
import {TmpTimeTable} from "../../../../../assets/Models/tmp-time-table";
import {n} from "@fullcalendar/core/internal-common";


export interface PageContent {
  header?: string;
  text?: string;
  buttons?: {
    text: string;
    color: string;
  }[];
}



@Component({
  selector: 'app-data-wizard',
  templateUrl: './data-wizard.component.html',
  styleUrl: './data-wizard.component.css'
})
export class DataWizardComponent{
  @Input() tmpTimeTable!: TmpTimeTable;
  @Input() allCourses: CourseDTO[] = [];
  @Output() initialSelection: CourseDTO[] = [];

  pageContent!: [PageContent, PageContent];
  isFirstSectionConfirmed = true;

 constructor() {
   this.pageContent = [
     {
       header: 'Course Degree(s)', text:'Select the degree of courses',
       buttons:[
         {text:'yes', color:'#889A3E'},
         {text:'no',  color:'#AD5353'}
       ]
     },
     {
       header: 'Semester Courses', text:'Do you want to only include the courses of the selected semester?',
       buttons:[
         {text:'Bachelor Computer Science',   color:'var(--system-color-primary-orange)'},
         {text:'Master Computer Science',     color:'var(--system-color-primary-orange)'},
         {text:'Master Software Engineering', color:'var(--system-color-primary-orange)'}
       ]
     }
   ]
 }

 showButton(val: any){
   console.log('showButton', val);
 }

  handleFirstSectionClick(buttonText: string) {
    if (buttonText === 'yes') {
      this.isFirstSectionConfirmed = true;
    }
  }
  inputData(){
  }


}
