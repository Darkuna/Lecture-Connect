package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;
import com.example.demo.models.TimeTable;

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
}
