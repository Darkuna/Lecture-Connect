package com.lecture.coordinator.services;

import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
            courseSession.setId(String.format("%s-%d",course.getId(), i));
            courseSession.setAssigned(false);
            courseSession.setTimingConstraints(course.getTimingConstraints());

            if(isSplitCourse){
                courseSession.setDuration(course.getSplitTimes().get(i));
            } else{
                courseSession.setDuration(course.getDuration());
            }

            if(course.isTimingFixed()){
                Tuple<Timing> splitTimes = course.getFixedTimings();
                courseSession.setTiming(i == 0 ? splitTimes.getL() : splitTimes.getR());
                courseSession.setAssigned(true);
            } else{
                courseSession.setTiming(timingService.createTiming(LocalTime.MIDNIGHT, LocalTime.MIDNIGHT, null));
            }

            courseSessions.add(courseSession);
        }
        return courseSessions;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession assignCourseSession(CourseSession courseSession, Room room, Timing timing){
        courseSession.setRoom(room);
        courseSession.setTiming(timing);
        courseSession.setAssigned(true);
        return courseSessionRepository.save(courseSession);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession unassignCourseSession(CourseSession courseSession){
        courseSession.setRoom(null);
        courseSession.setTiming(null);
        courseSession.setAssigned(false);
        return courseSessionRepository.save(courseSession);
    }

    public List<CourseSession> loadAllAssignedToRoom(Room room){
        return courseSessionRepository.findAllByRoom(room);
    }

    public List<CourseSession> loadAllUnassignedCourseSessionsFor(TimeTable timeTable){
        return courseSessionRepository.findAllByIsAssignedFalseAndTimeTable(timeTable);
    }
}
