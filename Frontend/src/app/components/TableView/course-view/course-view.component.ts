import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {ConfirmationService, MessageService} from "primeng/api";
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";
import {getRoleOptions} from "../../../../assets/Models/enums/course-type";
import {getDegreeOptions} from "../../../../assets/Models/enums/study-type";



@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrl: '../tables.css',
})
export class CourseViewComponent implements OnInit, OnDestroy {
  itemDialogVisible: boolean = false;
  singleCourse: Course;
  courses: Course[];
  selectedCourses!: Course[];
  selectedHeaders: any;
  headers: any[];
  private courseSub?: Subscription;
  itemIsEdited = false;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private courseService: CourseService,
  ) {
    this.courses = [];
    this.singleCourse = new Course();
    this.headers = this.singleCourse.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  async ngOnInit() {
    this.courses = await this.courseService.getAllCourses();
    this.cd.markForCheck();
  }

  ngOnDestroy() {
    this.courseSub?.unsubscribe();
    this.cd.detach();
  }

  openNew() {
    this.singleCourse = new Course();
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
    this. itemIsEdited = false;
  }

  editItem(item: Course) {
    this.itemIsEdited = true;
    this.singleCourse = item;
    this.itemDialogVisible = true;
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.courses[this.findIndexById(this.singleCourse.id)] =
        this.courseService.updateSingleCourse(this.singleCourse);

      this.itemIsEdited = false;
      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Change', detail: 'Element was updated'});
    } else if (this.isInList(this.singleCourse)) {
      this.messageService.add({severity: 'error', summary: 'Failure', detail: 'Element already in List'});
    } else {
      this.singleCourse.timingConstraints = [];
      this.courseService.createSingleCourse(this.singleCourse).subscribe({
        next: value => {
          this.courses.push(value);
          this.hideDialog();
          this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
        },

        error: err => {
          this.messageService.add({severity: 'error', summary: 'Upload', detail: err.toString()});
        }
      });
    }
  }

  deleteSingleItem(course: Course) {
    if (this.isInList(course)) {
      this.courses.forEach((item, index) => {
        if (item === course) {
          this.courseService.deleteSingleCourse(course.id);
          this.courses.splice(index, 1);
        }
      });
    }
  }

  deleteMultipleItems() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all the selected Courses?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.selectedCourses.forEach(Course => this.deleteSingleItem(Course));
        this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Courses deleted permanently'});
      }
    });
  }

  isInList(item: Course): boolean {
    for (const listItem of this.courses) {
      if (item.constructor !== listItem.constructor) {
        continue;
      }

      if (item.id && listItem.id && item.id === listItem.id) {
        return true;
      }
    }
    return false;
  }

  findIndexById(id: string): number {
    let index = -1;
    for (let i = 0; i < this.courses.length; i++) {
      if (this.courses[i].id === id) {
        index = i;
        break;
      }
    }

    return index;
  }

  readonly roleOptions = getRoleOptions();
  readonly degreeOptions = getDegreeOptions();
}
