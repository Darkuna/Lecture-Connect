import {ChangeDetectorRef, Component} from '@angular/core';
import {ConfirmationService, MessageService} from "primeng/api";
import {Course} from "../../../../assets/Models/course";
import {CourseType} from "../../../../assets/Models/enums/course-type";

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrl: '../tables.css'
})
export class CourseViewComponent {
  itemDialogVisible: boolean = false;
  singleCourse: Course;
  courses: Course[];
  selectedCourses!: Course[];
  selectedHeaders: any;
  headers: any[];

  stateOptions: any[] = [
    {label: 'Yes', value: true},
    {label: 'No', value: false}
  ];

  private itemIsEdited = false;

  constructor(
    private cd: ChangeDetectorRef,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {
    this.courses = [];
    this.singleCourse = new Course();
    this.headers = this.singleCourse.getTableColumns();
    this.selectedHeaders = this.headers;
  }

  ngOnInit(): void {
    this.cd.markForCheck();
  }

  openNew() {
    this.itemDialogVisible = true;
  }

  hideDialog() {
    this.itemDialogVisible = false;
  }

  editItem(item: Course) {
    this.itemIsEdited = true;
    this.singleCourse = item;
    this.openNew();
  }

  saveNewItem(): void {
    if (this.itemIsEdited) {
      this.singleCourse.updateDate = new Date();
      this.courses[this.findIndexById(this.singleCourse.id)] = this.singleCourse;
      this.itemIsEdited = false;
      this.singleCourse = new Course();

      this.hideDialog();
      this.messageService.add({severity: 'success', summary: 'Change', detail: 'Element was updated'});

    } else if (this.isInList(this.singleCourse)) {
      this.messageService.add({severity: 'error', summary: 'Failure', detail: 'Element already in List'});

    } else {
      this.singleCourse.createDate = new Date();
      this.singleCourse.updateDate = this.singleCourse.createDate;

      this.courses.push(this.singleCourse);
      this.singleCourse = new Course();

      this.hideDialog();
    }
    this.messageService.add({severity: 'success', summary: 'Upload', detail: 'Element saved to DB'});
  }

  deleteSingleItem(Course: Course) {
    if (this.isInList(Course)) {
      this.courses.forEach((item, index) => {
        if (item === Course) this.courses.splice(index, 1);
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
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Courses deleted permanently',
          life: 2000
        });
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

  getRoleOptions() {
    return Object.keys(CourseType).filter(k => isNaN(Number(k)));
  }
}