import {AfterViewInit, Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {getRoleOptions} from "../../../../assets/Models/enums/course-type";
import {getDegreeOptions} from "../../../../assets/Models/enums/study-type";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent implements OnDestroy, AfterViewInit {
  @Input() courseTable: Course[] = [];
  @Input() wizardMode: boolean = true;

  @Output() addCourseInParent: EventEmitter<Course> = new EventEmitter<Course>();
  @Output() removeCourseInParent: EventEmitter<Course> = new EventEmitter<Course>();

  courseSub!: Subscription;
  availableCourses: Course[] = [];

  CreateDialogVisible: boolean = false;
  draggedCourse: Course | undefined | null;
  headers: any[] = [
    {field: 'id', header: 'Id'},
    {field: 'courseType', header: 'Type'},
    {field: 'name', header: 'Name'},
    {field: 'semester', header: 'Semester'}
  ];

  constructor(
    private courseService: CourseService,
    private messageService: MessageService,
  ) { }

  ngAfterViewInit(): void {
    this.courseSub = (this.wizardMode ?
      this.courseService.getAllCourses() : this.courseService.getUnselectedCourses())
      .subscribe((data:Course[]) => this.availableCourses = data);
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


      this.courseService.createSingleCourse(this.draggedCourse!).subscribe({
        next: value => {
          this.availableCourses.push(value);
          this.hideDialog();
          this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
        },

        error: err => {
          this.messageService.add({severity: 'error', summary: 'Upload', detail: err.toString()});
        }
      });

      this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
      this.draggedCourse = null;
      this.hideDialog();
    }
  }

  protected readonly getRoleOptions = getRoleOptions;
  protected readonly getDegreeOptions = getDegreeOptions;
  protected readonly screen = screen;
  protected readonly String = String;
}
