package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcurrentCourseLimiter <T>{
    private final Map<T, List<Integer>> assignedGroupCourseSessions;
    private final int numberOfAllowedOverlapsPerCourse;

    public ConcurrentCourseLimiter(int numberOfAllowedOverlapsPerCourse) {
        this.assignedGroupCourseSessions = new HashMap<>();
        this.numberOfAllowedOverlapsPerCourse = numberOfAllowedOverlapsPerCourse;
    }

    /**
     * This method initializes a certain group course
     * @param key to initialize
     */
    public void initGroup(T key){
        if(!assignedGroupCourseSessions.containsKey(key)){
            assignedGroupCourseSessions.put(key, new ArrayList<>());
        }
    }

    /**
     * This method checks if the numberOfAllowedOverlapsPerCourse is exceeded by a certain course
     * @param key to check
     * @param candidate is needed to calculate the correct value in the integer list
     * @return true if limit is reached, else false
     */
    public boolean isLimitExceeded(T key, Candidate candidate){
        if(!assignedGroupCourseSessions.containsKey(key)){
            return false;
        }

        return assignedGroupCourseSessions.get(key).stream()
                .filter(t -> t == candidate.getDay() * 100 + candidate.getSlot())
                .count() >= numberOfAllowedOverlapsPerCourse;
    }

    /**
     * This method add a new entry to the map. The value of the new entry is calculated by multiplying the day number by
     * 100 and adding the slot number
     * @param key to get the courseId key from
     * @param candidate to calculate and add the value from
     */
    public void addEntry(T key, Candidate candidate) {
        if(assignedGroupCourseSessions.containsKey(key)){
            List<Integer> values = assignedGroupCourseSessions.get(key);
            for(int i = candidate.getSlot(); i <= candidate.getEndSlot(); i++){
                values.add(candidate.getDay() * 100 + i);
            }
        }
    }

    /**
     * This method add a new entry to the map. The value of the new entry is calculated by multiplying the day number by
     * 100 and adding the slot number
     * @param key to get the courseId key from
     * @param candidate to calculate and add the value from
     */
    public void addEntry(T key, CourseSession courseSession) {
        if(assignedGroupCourseSessions.containsKey(key)){
            List<Integer> values = assignedGroupCourseSessions.get(key);
            for(int i = AvailabilityMatrix.timeToSlotIndex(courseSession.getTiming().getStartTime());
                i <= AvailabilityMatrix.timeToSlotIndex(courseSession.getTiming().getEndTime()); i++){
                values.add(courseSession.getTiming().getDay().ordinal() * 100 + i);
            }
        }
    }

    /**
     * This method is used to remove an entry, if the backtracking algorithm has to go one step back
     * @param key to remove latest entry from
     * @param candidate to remove latest entry from
     */
    public void removeEntry(T key, Candidate candidate) {
        if (assignedGroupCourseSessions.containsKey(key)) {
            List<Integer> values = assignedGroupCourseSessions.get(key);
            for (int i = candidate.getSlot(); i <= candidate.getEndSlot(); i++) {
                values.remove((Integer) (candidate.getDay() * 100 + i));
            }
        }
    }

    /**
     * This method is used to remove an entry, if the backtracking algorithm has to go one step back
     * @param key to remove latest entry from
     * @param candidate to remove latest entry from
     */
    public void removeEntry(T key, CourseSession courseSession) {
        if (assignedGroupCourseSessions.containsKey(key)) {
            List<Integer> values = assignedGroupCourseSessions.get(key);
            for (int i = AvailabilityMatrix.timeToSlotIndex(courseSession.getTiming().getStartTime());
                 i <= AvailabilityMatrix.timeToSlotIndex(courseSession.getTiming().getEndTime()); i++) {
                values.remove((Integer) (courseSession.getTiming().getDay().ordinal() * 100 + i));
            }
        }
    }
}
