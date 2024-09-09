import {Component, Input, Output} from '@angular/core';
import {CourseDTO} from "../../../../../assets/Models/dto/course-dto";
import {TmpTimeTable} from "../../../../../assets/Models/tmp-time-table";
import {Router} from "@angular/router";
import {TableShareService} from "../../../../services/table-share.service";
import {GlobalTableService} from "../../../../services/global-table.service";
import {CourseService} from "../../../../services/course-service";
import {Course} from "../../../../../assets/Models/course";

@Component({
  selector: 'app-data-wizard',
  templateUrl: './data-wizard.component.html',
  styleUrl: './data-wizard.component.css',
})
export class DataWizardComponent{
  tmpTimeTable: TmpTimeTable;
  @Input() allCourses: Course[] = [];
  @Output() initialSelection: Course[] = [];

  userHasInteracted: boolean = false;
  userHasInteractedSemester: boolean = false;

  filterStudyType: [string, string, string] = ['', '', ''];
  showIndex: number = 0;

 constructor(
   private router: Router,
   private shareService: TableShareService,
   private courseService: CourseService,
 ) {
   this.tmpTimeTable = this.shareService.selectedTable;
     this.courseService.getAllCourses().subscribe(
       ((data:Course[]) => this.allCourses = data)
     );
 }

 filterData(setFilter: boolean): void {
   if(setFilter){
     this.userHasInteracted = true;
     this.showIndex += 1;
   } else {
     this.router.navigate(['/wizard'])
       .catch(message => {});
   }
 }

  filterSemester(setFilter: boolean): void{
   if(setFilter){
     if(this.tmpTimeTable.semester == 'SS'){
       this.initialSelection = this.allCourses
         .filter(c => c.semester! % 2 === 0);
     } else {
       this.initialSelection = this.allCourses
         .filter(c => c.semester! % 2 !== 0);
     }
   } else {
     this.initialSelection = this.allCourses;
   }

   this.userHasInteractedSemester = true;
    this.showIndex += 1;
  }

  addStudyTypeFilter(studyType: string, idx: number): void {
   this.filterStudyType[idx] = studyType;
  }

  finish(){
   this.initialSelection = this.initialSelection
     .filter(c =>
       c.studyType == this.filterStudyType[0]
       || c.studyType == this.filterStudyType[1]
       || c.studyType == this.filterStudyType[2]
     )
    this.shareService.initialCourses = this.initialSelection;
    this.router.navigate(['/wizard']).catch(message => {});
 }

}
