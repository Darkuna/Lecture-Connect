package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;

public class AssignmentStackEntry {
    CourseSession courseSession;
    Candidate candidate;
    public AssignmentStackEntry(CourseSession courseSession, Candidate candidate) {
        this.courseSession = courseSession;
        this.candidate = candidate;
    }
}
