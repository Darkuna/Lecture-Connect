import {Component, Input, Output} from '@angular/core';
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";
import {Router} from "@angular/router";
import {TableShareService} from "../../../services/table-share.service";
import {CourseService} from "../../../services/course-service";
import {Course} from "../../../../assets/Models/course";
import {MessageService} from "primeng/api";
import {Room} from "../../../../assets/Models/room";
import {RoomService} from "../../../services/room-service";

@Component({
  selector: 'app-data-wizard',
  templateUrl: './data-wizard.component.html',
  styleUrl: './data-wizard.component.css',
})
export class DataWizardComponent{
  tmpTimeTable: TmpTimeTable;
  @Input() allCourses: Course[] = [];
  @Output() initialCourses: Course[] = [];
  @Output() initialRooms: Room[] = [];

  userHasInteracted: boolean = false;
  userHasInteractedSemester: boolean = false;
  filterDataVar: boolean = true;
  filterSemesterVar: boolean = true;
  filterStudyType: [string|null, string|null, string|null] = [null, null, null];
  showIndex: number = 0;

 constructor(
   private router: Router,
   private shareService: TableShareService,
   private messageService: MessageService,
   private courseService: CourseService,
   private roomService: RoomService,
 ) {
   this.tmpTimeTable = this.shareService.selectedTable;
 }

 filterData(setFilter: boolean): void {
   if(setFilter){
     this.courseService.getAllCourses().subscribe(
       ((data:Course[]) => this.allCourses = data)
     );

     this.filterDataVar = setFilter;
     this.userHasInteracted = true;
     this.showIndex += 1;
   } else {
     this.router.navigate(['/user/wizard']).catch();
   }
 }

  filterSemester(setFilter: boolean): void{
    this.filterSemesterVar = setFilter;

   if(setFilter){
     if(this.tmpTimeTable.semester == 'SS'){
       this.initialCourses = this.allCourses
         .filter(c => c.semester! % 2 === 0);
     } else {
       this.initialCourses = this.allCourses
         .filter(c => c.semester! % 2 !== 0);
     }
   } else {
     this.initialCourses = this.allCourses;
   }

   this.userHasInteractedSemester = true;
    this.showIndex += 1;
  }

  addStudyTypeFilter(studyType: string, idx: number): void {
    this.showIndex = 3;

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
   this.initialCourses = this.initialCourses
     .filter(c =>
       c.studyType == this.filterStudyType[0]
       || c.studyType == this.filterStudyType[1]
       || c.studyType == this.filterStudyType[2]
     )
    if(this.initialCourses.length == 0){
      this.messageService.add({severity: 'error', summary: 'Initial Data Load', detail: 'There are no entries based on your selection'});
    } else {
      this.messageService.add({severity: 'success', summary: 'Initial Data Load', detail: 'Preselected data got added'});
    }

    this.shareService.selectedTable.courseTable = this.initialCourses;
    this.shareService.selectedTable.roomTables = this.initialRooms;
    this.router.navigate(['/user/wizard']).catch();
 }

 addRooms(option: boolean){
   if(option){
     this.roomService.getAllRooms().subscribe(
       ((data:Room[]) => this.initialRooms = data)
     );
   }
 }
}
