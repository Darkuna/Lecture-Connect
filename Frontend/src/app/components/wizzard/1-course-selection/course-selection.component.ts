import {AfterViewInit, Component, Input, OnDestroy} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {getRoleOptions} from "../../../../assets/Models/enums/course-type";
import {getDegreeOptions} from "../../../../assets/Models/enums/study-type";
import {TableShareService} from "../../../services/table-share.service";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent implements OnDestroy, AfterViewInit {
  @Input() courseTable: Course[] = [];
  @Input() wizardMode: boolean = true;

  courseSub!: Subscription;
  availableCourses!: Course[];

  CreateDialogVisible: boolean = false;
  selectedCourses: Course[] | null = null;
  draggedCourse: Course | undefined | null;
  headers: any[] = [
    {field: 'id', header: 'Id'},
    {field: 'courseType', header: 'Type'},
    {field: 'name', header: 'Name'},
    {field: 'semester', header: 'Semester'}
  ];

  constructor(
    private courseService: CourseService,
    private shareService: TableShareService,
    private messageService: MessageService,
  ) {
    this.courseTable = this.shareService.initialCourses;
  }

  ngAfterViewInit(): void {
    if(this.wizardMode){
      this.courseSub = this.courseService.getAllCourses().subscribe(
        (data => this.availableCourses = data)
      );

      console.log(this.shareService.initialCourses);
    } else {
      this.courseSub = this.courseService.getUnselectedCourses().subscribe(
        (data => this.availableCourses = data)
      );
    }
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

  protected readonly getRoleOptions = getRoleOptions;
  protected readonly getDegreeOptions = getDegreeOptions;
    protected readonly window = window;
  protected readonly screen = screen;
  protected readonly String = String;
}
