import {Component} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Observable, tap} from "rxjs";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent {
  CreateDialogVisible: boolean = false;
  availableCourses$: Observable<Course[]>;
  selectedCourses: Course[];
  tmpCourseSelection: Course[];

  draggedCourse: Course | undefined | null;
  headers: any[];

  constructor(
    private courseService: CourseService,
    private messageService: MessageService,
  ) {
    this.availableCourses$ = this.courseService.getAllCourses();

    this.selectedCourses = [];
    this.tmpCourseSelection = [];
    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'semester', header: 'Semester'}
    ];

  }

  showCreateDialog():void{
    this.CreateDialogVisible = true;
    this.draggedCourse = new Course();
  }

  hideDialog() {
    this.CreateDialogVisible = false;
  }

  saveNewItem(): void {
    if(this.draggedCourse){
      this.draggedCourse.timingConstraints = [];

      this.availableCourses$.pipe(tap(courseList => {
        courseList.push(this.courseService.createSingleCourse(this.draggedCourse!))
      }));

      this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
      this.draggedCourse = null;
      this.hideDialog();
    }


  }

  dragStart(item: Course) {
    this.draggedCourse = item;
  }

  drag() {
  }

  drop() {
    if (this.draggedCourse) {
      let idx = this.findIndex(this.draggedCourse, this.selectedCourses);

      if (idx !== -1) {
        this.messageService.add({severity: 'error', summary: 'Duplicate', detail: 'Course is already in List'});
      } else {
        this.selectedCourses = [...(this.selectedCourses), this.draggedCourse];
        this.draggedCourse = null;
      }
    }
  }

  dragEnd() {
    this.draggedCourse = null;
  }

  findIndex(product: Course, list: Course[]): number {
    let index = -1;
    for (let i = 0; i < (list).length; i++) {
      if (product.id === (list)[i].id) {
        index = i;
        break;
      }
    }
    return index;
  }

  addNewCourse() {

  }

  deleteSingleItem(course: Course) {
    let draggedCourseIndex = this.findIndex(course, this.selectedCourses);
    this.selectedCourses = this.selectedCourses?.filter((val, i) => i != draggedCourseIndex);
  }

  deleteMultipleItems() {
    this.tmpCourseSelection.forEach(
      c => this.deleteSingleItem(c)
    );
  }
}
