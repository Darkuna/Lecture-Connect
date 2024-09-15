import {Component, Input, Output} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {Router} from "@angular/router";
import {TableShareService} from "../../../services/table-share.service";
import {CourseService} from "../../../services/course-service";
import {Course} from "../../../../assets/Models/course";
import {MessageService} from "primeng/api";

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
  filterDataVar: boolean = true;
  filterSemesterVar: boolean = true;

  filterStudyType: [string|null, string|null, string|null] = [null, null, null];
  showIndex: number = 0;

 constructor(
   private router: Router,
   private shareService: TableShareService,
   private courseService: CourseService,
   private messageService: MessageService,
 ) {
   this.tmpTimeTable = this.shareService.selectedTable;
     this.courseService.getAllCourses().subscribe(
       ((data:Course[]) => this.allCourses = data)
     );
 }

 filterData(setFilter: boolean): void {
   if(setFilter){
     this.filterDataVar = setFilter;
     this.userHasInteracted = true;
     this.showIndex += 1;
   } else {
     this.router.navigate(['/user/wizard'])
       .catch();
   }
 }

  filterSemester(setFilter: boolean): void{
    this.filterSemesterVar = setFilter;

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
   if(this.filterStudyType[idx] === null){
     this.filterStudyType[idx] = studyType;
   } else {
     this.filterStudyType[idx] = null;
   }
  }

  filterIsSet(idx:number):boolean {
   return this.filterStudyType[idx] !== null;
  }

  finish(){
   this.initialSelection = this.initialSelection
     .filter(c =>
       c.studyType == this.filterStudyType[0]
       || c.studyType == this.filterStudyType[1]
       || c.studyType == this.filterStudyType[2]
     )
    if(this.initialSelection.length == 0){
      this.messageService.add({severity: 'error', summary: 'Initial Data Load', detail: 'There are no entries based on your selection'});
    } else {
      this.messageService.add({severity: 'success', summary: 'Initial Data Load', detail: 'Preselected data got added'});
    }

    this.shareService.selectedTable.courseTable = this.initialSelection;
    this.router.navigate(['/user/wizard']).catch();
 }

}
