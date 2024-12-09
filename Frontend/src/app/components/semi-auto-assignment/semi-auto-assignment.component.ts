import {Component, Input, OnInit} from '@angular/core';
import {CandidateDTO} from '../../../assets/Models/dto/candidate-dto';
import {SemiAutoService} from '../../services/semi-auto.service';
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-semi-auto-assignment',
  templateUrl: './semi-auto-assignment.component.html',
  styleUrl: './semi-auto-assignment.component.css',
})
export class SemiAutoAssignmentComponent implements OnInit {
  @Input() timeTableId!: number;
  courseSessionMap: Map<String, CandidateDTO[]> = new Map();
  courseSessions: String[] = [];

  selectedCourseSessions: String[] = [];
  selectedCourseSession: String | null = null;
  selectedCandidate: CandidateDTO | null = null;
  autoAssignEnabled = false;
  assignEnabled = false;

  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 0;
  courseSessionsCurrentPage: number = 1;
  courseSessionsItemsPerPage: number = 10;
  courseSessionsTotalPages: number = 0;

  constructor(
    private semiAutoService: SemiAutoService,
    private config: DynamicDialogConfig,
    private dialogRef: DynamicDialogRef) {}

  ngOnInit(): void {
    this.timeTableId = this.config.data.timeTableId;
    this.loadCourseSessionMap();
  }

  loadCourseSessionMap(): void {
    this.semiAutoService.getCourseSessionMap(this.timeTableId).subscribe({
      next: (data) => {
        this.courseSessionMap = new Map<String, CandidateDTO[]>(Object.entries(data));
        this.courseSessions = Array.from(this.courseSessionMap.keys());
      },
      error: (err) => {
        console.error('Error fetching Course Session Map:', err);
      },
    });
  }

  onCourseSessionSelect(courseSession: String): void {
    this.selectedCourseSession = courseSession;
    this.selectedCandidate = null;
    this.currentPage = 1;
    this.updateButtonStates();
  }

  onCandidateSelect(candidate: CandidateDTO): void {
    this.selectedCandidate = candidate;
    this.updateButtonStates();
  }

  onCheckboxChange(event: any, courseSession: String): void {
    if (event.checked) {
      this.selectedCourseSessions.push(courseSession);
    } else {
      this.selectedCourseSessions = this.selectedCourseSessions.filter((cs) => cs !== courseSession);
    }
    this.updateButtonStates();
  }

  updateButtonStates(): void {
    this.autoAssignEnabled = this.selectedCourseSessions.length > 0;
    this.assignEnabled = !!this.selectedCourseSession && !!this.selectedCandidate;
  }

  autoAssignSelection(): void {
    if (this.timeTableId && this.selectedCourseSessions.length > 0) {
      this.semiAutoService.autoAssignCourseSessions(this.timeTableId, this.selectedCourseSessions).subscribe({
        next: (response) => {
          console.log('Auto-Assign Success', response);
          this.loadCourseSessionMap();
        },
        error: (error) => {
          console.error('Auto-Assign Error', error);
        },
      });
      this.selectedCourseSessions = []
    }
  }

  assign(): void {
    console.log(this.timeTableId);
    console.log(this.selectedCourseSession);
    console.log(this.selectedCandidate);
    if (this.timeTableId && this.selectedCourseSession && this.selectedCandidate) {
      this.semiAutoService
        .assignCandidateToCourseSession(this.timeTableId, this.selectedCourseSession, this.selectedCandidate)
        .subscribe({
          next: (response) => {
            console.log('Assign Success', response);
            this.loadCourseSessionMap();
          },
          error: (error) => {
            console.error('Assign Error', error);
          },
        });
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  get paginatedCandidates(): CandidateDTO[] {
    const allCandidates = this.courseSessionMap.get(this.selectedCourseSession!) || [];
    this.totalPages = Math.ceil(allCandidates.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return allCandidates.slice(startIndex, endIndex);
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  get paginatedCourseSessions(): String[] {
    this.courseSessionsTotalPages = Math.ceil(this.courseSessions.length / this.courseSessionsItemsPerPage);
    const startIndex = (this.courseSessionsCurrentPage - 1) * this.courseSessionsItemsPerPage;
    const endIndex = startIndex + this.courseSessionsItemsPerPage;
    return this.courseSessions.slice(startIndex, endIndex);
  }

  goToNextCourseSessionsPage(): void {
    if (this.courseSessionsCurrentPage < this.courseSessionsTotalPages) {
      this.courseSessionsCurrentPage++;
    }
  }

  goToPreviousCourseSessionsPage(): void {
    if (this.courseSessionsCurrentPage > 1) {
      this.courseSessionsCurrentPage--;
    }
  }
}
