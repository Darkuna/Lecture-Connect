import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environment/environment';
import { catchError, Observable, throwError } from 'rxjs';
import { CandidateDTO } from '../../assets/Models/dto/candidate-dto';

@Injectable({
  providedIn: 'root',
})
export class SemiAutoService {
  static API_PATH = `${environment.baseUrl}/api/global/semi-auto`;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' +
        (localStorage.getItem('jwt-token') === null ? sessionStorage : localStorage).getItem('jwt-token'),
    }),
  };

  constructor(private http: HttpClient) {}

  /**
   * Fetches the map of CourseSessions and their Candidates.
   */
  getCourseSessionMap(id: number): Observable<Map<String, CandidateDTO[]>> {
    const url = `${SemiAutoService.API_PATH}/${id}`;
    return this.http.get<Map<String, CandidateDTO[]>>(url, this.httpOptions).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  /**
   * Trigger auto-assign logic for selected CourseSessions.
   */
  autoAssignCourseSessions(id: number, selectedCourseSessions: String[]): Observable<any> {
    const url = `${SemiAutoService.API_PATH}/${id}/auto-assign`;
    return this.http.post<any>(url, selectedCourseSessions, this.httpOptions).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }


  /**
   * Assign a specific Candidate to a CourseSession.
   */
  assignCandidateToCourseSession(id: number, courseSession: String, candidate: CandidateDTO): Observable<any> {
    const url = `${SemiAutoService.API_PATH}/${id}/manual-assign`;
    const payload = {
      courseSession,
      candidate,
    };
    return this.http.post<any>(url, payload, this.httpOptions).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

}
