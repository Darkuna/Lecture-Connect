package com.example.demo.services;

import com.example.demo.dto.CourseSessionDTO;
import com.example.demo.exceptions.courseSession.CourseSessionNotAssignedException;
import com.example.demo.models.*;
import com.example.demo.repositories.CourseSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing course sessions.
 * Enables the creation, assignment, fixation, and deletion of course sessions, along with their association to
 * roomTables and timeTables.
 */
@Service
@Scope("session")
public class CourseSessionService {
    @Autowired
    CourseSessionRepository courseSessionRepository;
    @Autowired
    TimingService timingService;

    /**
     * Creates course sessions for a given course based on its split and group settings.
     *
     * @param course The course for which sessions are to be created.
     * @return A list of created course sessions.
     */
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
                courseSession.setName(course.getCourseType() + " " + course.getName() + " - Split " + (i+1));
            } else if (hasGroups){
                courseSession.setDuration(course.getDuration());
                courseSession.setName(course.getCourseType() + " " + course.getName() + " - Group " + (i+1));
            } else {
                courseSession.setDuration(course.getDuration());
                courseSession.setName(course.getCourseType() + " " + course.getName());
            }
            courseSessions.add(courseSession);
        }
        return courseSessionRepository.saveAll(courseSessions);
    }

    /**
     * Marks a course session as fixed if it is already assigned.
     *
     * @param courseSession The course session to be fixed.
     * @return The fixed course session.
     * @throws CourseSessionNotAssignedException if the course session is not assigned.
     */
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

    /**
     * Deletes a course session, unassigning it first if necessary.
     *
     * @param courseSession The course session to be deleted.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deleteCourseSession(CourseSession courseSession){
        courseSessionRepository.delete(courseSession);
    }

    /**
     * Assigns a course session to a room table with specific timing.
     *
     * @param courseSession The course session to assign.
     * @param roomTable The room table to assign the course session to.
     * @param timing The timing for the course session.
     * @return The assigned course session.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession assignCourseSessionToRoomTable(CourseSession courseSession, RoomTable roomTable, Timing timing){
        courseSession.setRoomTable(roomTable);
        courseSession.setTiming(timing);
        courseSession.setAssigned(true);
        return courseSessionRepository.save(courseSession);
    }

    /**
     * Unassigns a course session from its room table and timing.
     *
     * @param courseSession The course session to unassign.
     * @return The unassigned course session.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession unassignCourseSession(CourseSession courseSession){
        courseSession.setRoomTable(null);
        Timing timing = courseSession.getTiming();
        courseSession.setTiming(null);
        timingService.deleteTiming(timing);
        courseSession.setAssigned(false);
        return courseSessionRepository.save(courseSession);
    }

    /**
     * Unassigns multiple course sessions.
     *
     * @param courseSessions List of course sessions to unassign.
     * @return The list of unassigned course sessions.
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> unassignCourseSessions(List<CourseSession> courseSessions){
        for (CourseSession courseSession : courseSessions) {
            courseSession.setRoomTable(null);
            Timing timing = courseSession.getTiming();
            courseSession.setTiming(null);
            timingService.deleteTiming(timing);
            courseSession.setAssigned(false);
        }
        return courseSessionRepository.saveAll(courseSessions);
    }

    /**
     * Loads all course sessions assigned to a specific room table.
     *
     * @param roomTable The room table the course session are assigned to.
     * @return The list of course sessions assigned to the room table.
     */
    public List<CourseSession> loadAllAssignedToRoomTable(RoomTable roomTable){
        return courseSessionRepository.findAllByRoomTable(roomTable);
    }

    /**
     * Loads all course sessions created from a specific course.
     *
     * @param course The course the course sessions are created from.
     * @return The list of course sessions from a specific course.
     */
    public List<CourseSession> loadAllFromCourse(Course course){
        return courseSessionRepository.findAllByCourse(course);
    }

    /**
     * Loads all course sessions associated to a specific timetable.
     *
     * @param timeTable The timetable the course sessions are associated to.
     * @return The list of course sessions from a specific timetable.
     */
    public List<CourseSession> loadAllFromTimeTable(TimeTable timeTable){
        return courseSessionRepository.findAllByTimeTable(timeTable);
    }

    /**
     * Loads a course session from a given id.
     *
     * @param id ID of the course session to be loaded.
     * @return The course session.
     * @throws EntityNotFoundException if the course session could not be found in the database.
     */
    public CourseSession loadCourseSessionByID(long id){
        return courseSessionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CourseSession not found for ID: " + id));
    }

    /**
     * Converts a Course object into a CourseDTO object
     *
     * @param courseSession to be converted
     * @return CourseSessionDTO object
     */
    public CourseSessionDTO toDTO(CourseSession courseSession){
        CourseSessionDTO dto = new CourseSessionDTO();
        dto.setId(courseSession.getId());
        dto.setName(courseSession.getName());
        dto.setAssigned(courseSession.isAssigned());
        dto.setFixed(courseSession.isFixed());
        dto.setRoomTableId(courseSession.getRoomTable().getId());
        dto.setDuration(courseSession.getDuration());
        if(courseSession.getTiming() != null){
            dto.setTiming(timingService.toDTO(courseSession.getTiming()));
        }
        return dto;
    }

    /**
     * Converts a CourseDTO object into a Course object
     *
     * @param dto to be converted
     * @return CourseSession object
     */
    public CourseSession fromDTO(CourseSessionDTO dto) {
        CourseSession courseSession = new CourseSession();
        courseSession.setId(dto.getId());
        courseSession.setName(dto.getName());
        courseSession.setAssigned(dto.isAssigned());
        courseSession.setFixed(dto.isFixed());
        courseSession.setDuration(dto.getDuration());
        courseSession.setTiming(timingService.fromDTO(dto.getTiming()));
        return courseSession;
    }
}
