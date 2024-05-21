import {Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class CourseSelectionComponent implements OnInit, OnDestroy {
  availableCourses: Course[];
  selectedCourses: Course[];
  draggedCourse: Course | undefined | null;
  private courseSub?: Subscription;
  headers: any[];

  constructor(private courseService: CourseService) {
    this.selectedCourses = [];
    this.availableCourses = [];
    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'semester', header: 'Semester'}
    ];

  }

  ngOnInit() {
    this.selectedCourses = [];
    this.courseSub = this.courseService
      .getAllCourses()
      .subscribe(data => this.availableCourses = data)
  }

  ngOnDestroy() {
    this.courseSub?.unsubscribe();
  }

  dragStart(item: Course) {
    this.draggedCourse = item;
  }

  drag() {
  }

  drop() {
    if (this.draggedCourse) {
      let draggedCourseIndex = this.findIndex(this.draggedCourse, this.availableCourses);
      this.selectedCourses = [...(this.selectedCourses as Course[]), this.draggedCourse];
      this.availableCourses = this.availableCourses?.filter((val, i) => i != draggedCourseIndex);
      this.draggedCourse = null;
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
}
