package com.example.demo.scheduling;

import com.example.demo.models.CourseSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAssignmentMap {
    private final Map<String, List<Integer>> assignedGroupCourseSessions;
    private final int numberOfAllowedOverlapsPerCourse;

    public GroupAssignmentMap(int numberOfAllowedOverlapsPerCourse) {
        this.assignedGroupCourseSessions = new HashMap<>();
        this.numberOfAllowedOverlapsPerCourse = numberOfAllowedOverlapsPerCourse;
    }

    public void initGroup(String groupID){
        assignedGroupCourseSessions.put(groupID, new ArrayList<>());
    }

    public boolean isLimitExceeded(CourseSession courseSession, Candidate candidate){
        if(!assignedGroupCourseSessions.containsKey(courseSession.getCourseId())){
            return false;
        }

        return assignedGroupCourseSessions.get(courseSession.getCourseId()).stream()
                .filter(t -> t == candidate.getDay() * 100 + candidate.getSlot())
                .count() >= numberOfAllowedOverlapsPerCourse;
    }

    public void addEntry(CourseSession courseSession, Candidate candidate) {
        if(assignedGroupCourseSessions.containsKey(courseSession.getCourseId())){
            assignedGroupCourseSessions.get(courseSession.getCourseId()).add(candidate.getDay() * 100 + candidate.getSlot());
        }
    }

    public void removeEntry(CourseSession courseSession) {
        if(assignedGroupCourseSessions.containsKey(courseSession.getCourseId())) {
            assignedGroupCourseSessions.get(courseSession.getCourseId()).removeLast();
        }
    }
}
