import {Component} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Observable, Subscription} from "rxjs";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent {
  availableCourses$: Observable<Course[]>;
  selectedCourses: Course[];
  tmpCourseSelection: Course[];

  draggedCourse: Course | undefined | null;
  private courseSub?: Subscription;
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
        this.selectedCourses = [...(this.selectedCourses as Course[]), this.draggedCourse];
        this.draggedCourse = null;
      }
    }
  }

  dragEnd() {
    this.draggedCourse = null;
  }

  findIndex(product: Course, list: Course[]) {
    let index = -1;
    for (let i = 0; i < (list as Course[]).length; i++) {
      if (product.id === (list as Course[])[i].id) {
        index = i;
        break;
      }
    }
    return index;
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
