package at.uibk.leco.scheduling;

import at.uibk.leco.models.CourseSession;

/**
 * This class represents an entry in the assignment stack. This stack contains pairs of a courseSession and a
 * corresponding candidate that are ready to be assigned.
 */
public class AssignmentStackEntry {

    CourseSession courseSession;
    Candidate candidate;

    public AssignmentStackEntry(CourseSession courseSession, Candidate candidate) {
        this.courseSession = courseSession;
        this.candidate = candidate;
    }
}
