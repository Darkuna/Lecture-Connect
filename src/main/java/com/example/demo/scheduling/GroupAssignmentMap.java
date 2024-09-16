package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to ensure that every group courseSession only overlaps with a specified number of courseSessions
 * withing the same group. For example, if the numberOfAllowedOverlapsPerCourse is 3, only three courseSessions
 * can be assigned at the same day at the same time.
 */
public class GroupAssignmentMap {
    private final Map<String, List<Integer>> assignedGroupCourseSessions;
    private final int numberOfAllowedOverlapsPerCourse;

    public GroupAssignmentMap(int numberOfAllowedOverlapsPerCourse) {
        this.assignedGroupCourseSessions = new HashMap<>();
        this.numberOfAllowedOverlapsPerCourse = numberOfAllowedOverlapsPerCourse;
    }

    /**
     * This method initializes a certain group course
     * @param courseID to initialize
     */
    public void initGroup(String courseID){
        assignedGroupCourseSessions.put(courseID, new ArrayList<>());
    }

    /**
     * This method checks if the numberOfAllowedOverlapsPerCourse is exceeded by a certain course
     * @param courseSession to check
     * @param candidate is needed to calculate the correct value in the integer list
     * @return true if limit is reached, else false
     */
    public boolean isLimitExceeded(CourseSession courseSession, Candidate candidate){
        if(!assignedGroupCourseSessions.containsKey(courseSession.getCourseId())){
            return false;
        }

        return assignedGroupCourseSessions.get(courseSession.getCourseId()).stream()
                .filter(t -> t == candidate.getDay() * 100 + candidate.getSlot())
                .count() >= numberOfAllowedOverlapsPerCourse;
    }

    /**
     * This method add a new entry to the map. The value of the new entry is calculated by multiplying the day number by
     * 100 and adding the slot number
     * @param courseSession to get the courseId key from
     * @param candidate to calculate and add the value from
     */
    public void addEntry(CourseSession courseSession, Candidate candidate) {
        if(assignedGroupCourseSessions.containsKey(courseSession.getCourseId())){
            assignedGroupCourseSessions.get(courseSession.getCourseId()).add(candidate.getDay() * 100 + candidate.getSlot());
        }
    }

    /**
     * This method is used to remove an entry, if the backtracking algorithm has to go one step back
     * @param courseSession to remove latest entry from
     */
    public void removeEntry(CourseSession courseSession) {
        if(assignedGroupCourseSessions.containsKey(courseSession.getCourseId())) {
            assignedGroupCourseSessions.get(courseSession.getCourseId()).removeLast();
        }
    }
}
