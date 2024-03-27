package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope("session")
public class CourseSessionService {
    @Autowired
    CourseSessionRepository courseSessionRepository;
    @Autowired
    TimingService timingService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> createCourseSessionsFromCourse(Course course){
        List<CourseSession> courseSessions = new ArrayList<>();
        boolean isSplitCourse = course.isSplit();
        boolean hasGroups = course.getNumberOfGroups() > 1;
        int numberOfCourseSessionsToCreate = hasGroups ? course.getNumberOfGroups() : (isSplitCourse ? 2 : 1);

        for(int i = 0; i < numberOfCourseSessionsToCreate; i++){
            CourseSession courseSession = new CourseSession();
            courseSession.setCourse(course);
            courseSession.setAssigned(false);
            courseSession.setTimingConstraints(course.getTimingConstraints());

            if(isSplitCourse){
                courseSession.setDuration(course.getSplitTimes().get(i));
            } else{
                courseSession.setDuration(course.getDuration());
            }
            courseSessions.add(courseSession);
        }
        return courseSessions;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession assignCourseSessionToRoom(CourseSession courseSession, RoomTable roomTable, Timing timing){
        courseSession.setRoomTable(roomTable);
        courseSession.setTiming(timing);
        courseSession.setAssigned(true);
        return courseSessionRepository.save(courseSession);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession unassignCourseSession(CourseSession courseSession){
        courseSession.setRoomTable(null);
        courseSession.setTiming(null);
        courseSession.setAssigned(false);
        return courseSessionRepository.save(courseSession);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> unassignCourseSessions(List<CourseSession> courseSessions){
        for (CourseSession courseSession : courseSessions) {
            courseSession.setRoomTable(null);
            courseSession.setTiming(null);
            courseSession.setAssigned(false);
        }
        return courseSessionRepository.saveAll(courseSessions);
    }

    public List<CourseSession> loadAllAssignedToRoomTableInTimeTable(TimeTable timeTable, RoomTable roomTable){
        return courseSessionRepository.findAllByTimeTableAndRoomTable(timeTable, roomTable);
    }

    public List<CourseSession> loadAllUnassignedCourseSessionsFor(TimeTable timeTable){
        return courseSessionRepository.findAllByIsAssignedFalseAndTimeTable(timeTable);
    }

    public List<CourseSession> loadAllFromTimeTableAndCourse(TimeTable timeTable, Course course){
        return courseSessionRepository.findAllByTimeTableAndCourse(timeTable,course);
    }

    public List<CourseSession> loadAllFromTimeTable(TimeTable timeTable){
        return courseSessionRepository.findAllByTimeTable(timeTable);
    }
}
