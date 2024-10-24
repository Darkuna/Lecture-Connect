package com.example.demo.services;

import com.example.demo.exceptions.courseSession.CourseSessionNotAssignedException;
import com.example.demo.models.*;
import com.example.demo.repositories.CourseSessionRepository;
import com.example.demo.repositories.RoomTableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing course sessions.
 * Enables the creation, assignment, fixation, and deletion of course sessions, along with their association to
 * roomTables and timeTables.
 */
@Service
@Scope("session")
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;
    private final TimingService timingService;
    private static final Logger log = LoggerFactory.getLogger(CourseSessionService.class);

    @Autowired
    public CourseSessionService(CourseSessionRepository courseSessionRepository, TimingService timingService) {
        this.courseSessionRepository = courseSessionRepository;
        this.timingService = timingService;
    }

    /**
     * Creates course sessions for a given course based on its split and group settings.
     *
     * @param course The course for which sessions are to be created.
     * @return A list of created course sessions.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CourseSession> createCourseSessionsFromCourse(TimeTable timeTable, Course course){
        log.info("Course: split: {}, nrGroups: {}, splitTimes: {}", course.isSplit(), course.getNumberOfGroups(), course.getSplitTimes());
        List<CourseSession> courseSessions = new ArrayList<>();
        boolean isSplitCourse = course.isSplit();
        boolean hasGroups = course.getNumberOfGroups() > 1;
        int numberOfCourseSessionsToCreate = hasGroups ? course.getNumberOfGroups() : (isSplitCourse ? course.getSplitTimes().size() : 1);

        for(int i = 0; i < numberOfCourseSessionsToCreate; i++){
            CourseSession courseSession = new CourseSession();
            courseSession.setAssigned(false);
            courseSession.setFixed(false);
            courseSession.setLecturer(course.getLecturer());
            courseSession.setNumberOfParticipants(course.getNumberOfParticipants());
            courseSession.setSemester(course.getSemester());
            courseSession.setComputersNecessary(course.isComputersNecessary());
            courseSession.setTimingConstraints(course.getTimingConstraints());
            courseSession.setCourseId(course.getId());
            courseSession.setTimeTable(timeTable);
            courseSession.setStudyType(course.getStudyType());

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
        courseSessions = courseSessionRepository.saveAll(courseSessions);
        log.info("Created {} courseSessions from course {}", courseSessions.size(), course.getName());
        return courseSessions;
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
            log.info("Fixed course session {}", courseSession.getName());
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
        log.info("Deleted course session {}", courseSession.getName());
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
        courseSession = courseSessionRepository.save(courseSession);
        log.info("Assigned course session {} to roomTable {} at timing {}", courseSession.getName(), roomTable, timing);
        return courseSession;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CourseSession assignCourseSession(CourseSession courseSession){
        if(courseSession.getId() != null){
            CourseSession original = loadCourseSessionByID(courseSession.getId());
            original.setRoomTable(courseSession.getRoomTable());
            original.setTiming(timingService.createTiming(courseSession.getTiming()));
            original.setAssigned(true);
            original = courseSessionRepository.save(original);
            log.info("Assigned course session {} to roomTable {} at timing {}", original.getName(), original.getRoomTable(), original.getTiming());
            return courseSession;
        }
        return null;
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
        if(courseSession.getId() != null) {
            CourseSession original = loadCourseSessionByID(courseSession.getId());
            original.setRoomTable(null);
            original.setTiming(null);
            original.setAssigned(false);
            original = courseSessionRepository.save(original);
            log.info("Unassigned course session {}", courseSession.getName());
            return original;
        }
        return null;
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
        log.info("Unassigned {} course sessions", courseSessions.size());
        return courseSessionRepository.saveAll(courseSessions);
    }

    /**
     * Loads all course sessions assigned to a specific room table.
     *
     * @param roomTable The room table the course session are assigned to.
     * @return The list of course sessions assigned to the room table.
     */
    public List<CourseSession> loadAllAssignedToRoomTable(RoomTable roomTable){

        List<CourseSession> courseSessions = courseSessionRepository.findAllByRoomTable(roomTable);
        log.debug("Loaded all courseSessions assigned to roomTable {} ({})", roomTable.getRoomId(),
                courseSessions.size());
        for(CourseSession courseSession : courseSessions){
            courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        }
        return courseSessions;
    }

    /**
     * Loads all course sessions associated to a specific timetable.
     *
     * @param timeTable The timetable the course sessions are associated to.
     * @return The list of course sessions from a specific timetable.
     */
    public List<CourseSession> loadAllFromTimeTable(TimeTable timeTable){
        List<CourseSession> courseSessions = courseSessionRepository.findAllByTimeTable(timeTable);
        for(CourseSession courseSession : courseSessions){
            courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        }
        log.info("Loaded all courseSessions from timeTable {} ({})", timeTable, courseSessions.size());
        return courseSessions;
    }

    /**
     * Loads a course session from a given id.
     *
     * @param id ID of the course session to be loaded.
     * @return The course session.
     * @throws EntityNotFoundException if the course session could not be found in the database.
     */
    public CourseSession loadCourseSessionByID(long id){
        CourseSession courseSession = courseSessionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CourseSession not found for ID: " + id));
        log.info("Loaded course session {}", courseSession.getName());
        courseSession.setTimingConstraints(timingService.loadTimingConstraintsOfCourse(courseSession.getCourseId()));
        return courseSession;
    }

    public List<CourseSession> saveAll(Set<CourseSession> courseSessions) {
        List<CourseSession> courseSessionsToSave = courseSessions.stream()
                .toList();
        return courseSessionRepository.saveAll(courseSessionsToSave);
    }

    public List<CourseSession> saveAll(List<CourseSession> courseSessions) {
        return courseSessionRepository.saveAll(courseSessions);
    }

    public void moveCourseSession(CourseSession courseSession) {
        if(courseSession.getId() != null){
            CourseSession original = loadCourseSessionByID(courseSession.getId());
            if(!original.getTiming().hasSameDayAndTime(courseSession.getTiming())){
                Timing timing = original.getTiming();
                original.setTiming(timingService.createTiming(courseSession.getTiming()));
                courseSessionRepository.save(original);
                timingService.deleteTiming(timing);
            }
        }
    }
}
