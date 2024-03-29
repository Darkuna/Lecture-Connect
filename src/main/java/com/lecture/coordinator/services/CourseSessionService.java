package com.lecture.coordinator.services;

import com.lecture.coordinator.exceptions.courseSession.CourseSessionNotAssignedException;
import com.lecture.coordinator.model.*;
import com.lecture.coordinator.repositories.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
            courseSession.setFixed(false);
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
    public CourseSession fixCourseSession(CourseSession courseSession) throws CourseSessionNotAssignedException {
        if(courseSession.isAssigned()){
            courseSession.setFixed(true);
            return courseSessionRepository.save(courseSession);
        }
        else{
            throw new CourseSessionNotAssignedException("Course session must be assigned to be fixed");
        }

    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourseSession(CourseSession courseSession){
        if(courseSession.isAssigned()){
            this.unassignCourseSession(courseSession);
        }
        courseSessionRepository.delete(courseSession);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession assignCourseSessionToRoomTable(CourseSession courseSession, RoomTable roomTable, Timing timing){
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

    public List<CourseSession> loadAllAssignedToRoomTable(RoomTable roomTable){
        return courseSessionRepository.findAllByRoomTable(roomTable);
    }

    public List<CourseSession> loadAllFromTimeTableAndCourse(TimeTable timeTable, Course course){
        return courseSessionRepository.findAllByTimeTableAndCourse(timeTable,course);
    }

    public List<CourseSession> loadAllFromCourse(Course course){
        return courseSessionRepository.findAllByCourse(course);
    }

    public List<CourseSession> loadAllFromTimeTable(TimeTable timeTable){
        return courseSessionRepository.findAllByTimeTable(timeTable);
    }

    public CourseSession loadCourseSessionByID(long id){
        return courseSessionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CourseSession not found for ID: " + id));

    }
}
