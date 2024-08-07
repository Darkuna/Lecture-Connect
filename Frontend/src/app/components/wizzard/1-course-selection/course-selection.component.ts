import {Component, Input, OnDestroy} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {CourseType} from "../../../../assets/Models/enums/course-type";
import {co} from "@fullcalendar/core/internal-common";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent implements OnDestroy {
  @Input() courseTable!: Course[];

  courseSub: Subscription;
  availableCourses!: Course[];

  CreateDialogVisible: boolean = false;
  selectedCourses: Course[] | null = null;
  draggedCourse: Course | undefined | null;

  headers: any[];
  stateOptions: any[] = [
    {label: 'Yes', value: true},
    {label: 'No', value: false}
  ];

  constructor(
    private courseService: CourseService,
    private messageService: MessageService,
  ) {
    this.courseSub = this.courseService.getAllCourses().subscribe(
      (data => this.availableCourses = data)
    );

    this.headers = [
      {field: 'id', header: 'Id'},
      {field: 'courseType', header: 'Type'},
      {field: 'name', header: 'Name'},
      {field: 'semester', header: 'Semester'}
    ];
  }

  ngOnDestroy(): void {
    this.courseSub.unsubscribe();
  }

  showCreateDialog(): void {
    this.draggedCourse = new Course();
    this.CreateDialogVisible = true;
  }

  hideDialog() {
    this.CreateDialogVisible = false;
  }

  saveNewItem(): void {
    if (this.draggedCourse) {
      this.draggedCourse.timingConstraints = [];
      this.draggedCourse.createdAt = undefined;
      this.draggedCourse.updatedAt = undefined;

      this.availableCourses.push(this.courseService.createSingleCourse(this.draggedCourse!));

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
      let idx = this.courseTable.indexOf(this.draggedCourse);

      if (idx > -1) {
        this.messageService.add({severity: 'error', summary: 'Duplicate', detail: 'Course is already in List'});
      } else {
        this.courseTable.push(this.draggedCourse);
        this.draggedCourse = null;
      }
    }
  }

  dragEnd() {
    this.draggedCourse = null;
  }

  deleteSingleItem(course: Course) {
    this.courseTable = this.courseTable.filter((val:Course) => val.id !== course.id);
  }

  coursesSelected() : boolean{
    if(this.selectedCourses){
      return this.selectedCourses.length === 0 || this.courseTable.length === 0;
    }
    return true; //method is called to tell if a button should be disabled or not
  }

  deleteMultipleItems() {
    this.courseTable = this.courseTable.filter((val) => !this.selectedCourses?.includes(val));
    this.selectedCourses = null;
  }

  getRoleOptions() {
    return Object.keys(CourseType).filter(k => isNaN(Number(k)));
  }
}
