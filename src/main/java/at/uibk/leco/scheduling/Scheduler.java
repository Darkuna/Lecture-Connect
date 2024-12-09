package at.uibk.leco.scheduling;

import at.uibk.leco.models.CourseSession;
import at.uibk.leco.models.TimeTable;

import java.util.List;
import java.util.Map;

/**
 * This interface defines a Scheduler for the assignment of courseSessions to RoomTables.
 */
public interface Scheduler {
    /**
     * Initialize the Scheduler with a specific timeTable
     * @param timeTable to initialize Scheduler with
     */
    void setTimeTable(TimeTable timeTable);

    /**
     * This method starts the assignment algorithm.
     */
    void assignUnassignedCourseSessions();

    /**
     * This method checks if there are any conflicting assigned courseSessions.
     * @param timeTable to check collisions for
     * @return a map of conflicting courseSessions
     */
    Map<CourseSession, List<CollisionType>> collisionCheck(TimeTable timeTable);

    /**
     * This method is used for the semi-automatic assignment.
     * @param timeTable to be worked on.
     * @param courseSessionsForAutoFill a list of courseSessions to be automatically assigned
     * @param courseSessionToAssign a courseSession that was manually assigned
     * @param candidateToAssign a candidate the courseSessionToAssign shall be assigned to
     * @return the updated candidates map
     */
    Map<CourseSession, List<Candidate>> updateAndReturnCandidatesMap(TimeTable timeTable, List<CourseSession> courseSessionsForAutoFill,
                                                                     CourseSession courseSessionToAssign, Candidate candidateToAssign, String roomTable);

}
