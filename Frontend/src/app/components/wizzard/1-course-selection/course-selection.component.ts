import {AfterViewInit, Component, EventEmitter, Input, Output} from '@angular/core';
import {Course} from "../../../../assets/Models/course";
import {CourseService} from "../../../services/course-service";
import {calculateTableHeight} from "../wizard.component";

@Component({
  selector: 'app-course-selection',
  templateUrl: './course-selection.component.html',
  styleUrl: '../wizard.component.css',
})

export class CourseSelectionComponent implements AfterViewInit {
  @Input() courseTable: Course[] = [];
  @Input() wizardMode: boolean = true;

  @Output() addCourseInParent: EventEmitter<Course> = new EventEmitter<Course>();
  @Output() removeCourseInParent: EventEmitter<Course> = new EventEmitter<Course>();

  availableCourses: Course[] = [];

  constructor(
    private courseService: CourseService,
  ) { }

  async ngAfterViewInit(): Promise<void> {
    const allCourses = await this.courseService.getAllCourses();
    const courseTableIds = new Set(this.courseTable.map(course => course.id));
    this.availableCourses = allCourses.filter(course => !courseTableIds.has(course.id));
  }

  getTableSettings(container: HTMLDivElement) {
    return {'height': `${calculateTableHeight(container)}px`};
  }
}
