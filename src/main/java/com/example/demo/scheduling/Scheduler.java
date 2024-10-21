package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;
import com.example.demo.models.TimeTable;

import java.util.List;

public interface Scheduler {
    /**
     * Initialize the Scheduler with a specific timeTable
     * @param timeTable to initialize Scheduler with
     */
    public void setTimeTable(TimeTable timeTable);

    /**
     * This method starts the assignment algorithm.
     */
    public void assignUnassignedCourseSessions();

    /**
     * This method checks if there are any conflicting assigned courseSessions.
     * @param timeTable to check collisions for
     * @return a list of conflicting courseSessions
     */
    public List<CourseSession> collisionCheck(TimeTable timeTable);
}
