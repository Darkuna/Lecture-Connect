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
  availableCourses!: Course[];
  selectedCourses: Course[];
  draggedCourse: Course | undefined | null;
  private courseSub?: Subscription;

  constructor(private courseService: CourseService) {
    this.selectedCourses = [];
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

  dragStart(product: Course) {
    this.draggedCourse = product;
  }

  drop() {
    if (this.draggedCourse) {
      let draggedCourseIndex = this.findIndex(this.draggedCourse);
      this.selectedCourses = [...(this.selectedCourses as Course[]), this.draggedCourse];
      this.availableCourses = this.availableCourses?.filter((val, i) => i != draggedCourseIndex);
      this.draggedCourse = null;
    }
  }

  dragEnd() {
    this.draggedCourse = null;
  }

  findIndex(product: Course) {
    let index = -1;
    for (let i = 0; i < (this.availableCourses as Course[]).length; i++) {
      if (product.id === (this.availableCourses as Course[])[i].id) {
        index = i;
        break;
      }
    }
    return index;
  }
}
