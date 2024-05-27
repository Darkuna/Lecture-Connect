import {Component, Input, OnDestroy} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {Subscription} from "rxjs";
import {MessageService} from "primeng/api";
import {CourseType} from "../../../../assets/Models/enums/course-type";
import {tableStatus} from "../../../../assets/Models/enums/table-status";
import {TmpTimeTable} from "../../../../assets/Models/tmp-time-table";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent implements OnDestroy {
  @Input() globalTable!: TmpTimeTable;

  courseSub: Subscription;
  availableCourses!: Course[];

  CreateDialogVisible: boolean = false;
  selectedCourse!: Course;
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
      ( data => this.availableCourses = data )
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
    this.CreateDialogVisible = true;
    this.draggedCourse = new Course();
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
      let idx = this.findIndex(this.draggedCourse, this.globalTable.courseTable);

      if (idx !== -1) {
        this.messageService.add({severity: 'error', summary: 'Duplicate', detail: 'Course is already in List'});
      } else {
        this.globalTable.courseTable.push(this.draggedCourse);
        this.draggedCourse = null;
      }
    }
  }

  dragEnd() {
    this.draggedCourse = null;
    this.globalTable.tableName.status = tableStatus.EDITED;
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

  deleteSingleItem(course: Course) {
    let draggedCourseIndex = this.findIndex(course, this.globalTable.courseTable);
    this.globalTable.courseTable = this.globalTable.courseTable?.filter((val, i) => i != draggedCourseIndex);
  }

  deleteMultipleItems() {
    this.globalTable.courseTable.forEach(
      c => this.deleteSingleItem(c)
    );
  }

  getRoleOptions() {
    return Object.keys(CourseType).filter(k => isNaN(Number(k)));
  }

}
